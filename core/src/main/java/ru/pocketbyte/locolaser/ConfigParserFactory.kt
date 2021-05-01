package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.parser.*
import sun.misc.Unsafe
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.JarURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.LinkedHashSet

class ConfigParserFactory {

    companion object {
        private const val DEFAULT_JVM_VERSION = 8
        private const val VERSION_OFFSET = 44
    }

    private var jvmVersion: Int? = null

    @Throws(Exception::class)
    fun get(): ConfigParser {
        return getFromClassLoader()
    }

    @Throws(ClassNotFoundException::class, IllegalAccessException::class, InstantiationException::class, IOException::class)
    private fun getFromClassLoader(): ConfigParser {
        val configParsers = LinkedHashSet<ResourcesConfigParser<*>>()

        for (url in jarUrls) {
            readResourcesConfigParsers(url)
                    .mapTo(configParsers) {
                        Class.forName(it).newInstance() as ResourcesConfigParser<*>
                    }
        }

        if (configParsers.size == 0)
            throw RuntimeException("Resource Config parser not found")

        configParsers.add(EmptySourceConfigParser())

        return ConfigParser(
            sourceConfigParser = ResourcesSetConfigParser(configParsers, hasMainConfig = true),
            platformConfigParser = ResourcesSetConfigParser(configParsers, hasMainConfig = false)
        )
    }

    // jdk9
    // jdk.internal.loader.ClassLoaders.AppClassLoader.ucp
    // jdk.internal.loader.URLClassPath.path
    private val jarUrls: Array<URL>
        get() {
            val classLoader = Main::class.java.classLoader ?: throw RuntimeException("ClassLoader is null")

            if (classLoader is URLClassLoader)
                return classLoader.urLs
            if (classLoader.javaClass.name.startsWith("jdk.internal.loader.ClassLoaders$")) {
                try {
                    val fieldUnsafe = Unsafe::class.java.getDeclaredField("theUnsafe")
                    fieldUnsafe.isAccessible = true
                    val unsafe = fieldUnsafe.get(null) as Unsafe

                    var ucpOwner: Class<*> = classLoader.javaClass;
                    // Field removed in 16, but still exists in parent class "BuiltinClassLoader"
                    if (getJvmVersion() >= 16)
                        ucpOwner = ucpOwner.superclass;

                    val ucpField = ucpOwner.getDeclaredField("ucp");

                    val ucpFieldOffset = unsafe.objectFieldOffset(ucpField)
                    val ucpObject = unsafe.getObject(classLoader, ucpFieldOffset)
                    val pathField = ucpField.type.getDeclaredField("path")
                    val pathFieldOffset = unsafe.objectFieldOffset(pathField)
                    val path = unsafe.getObject(ucpObject, pathFieldOffset) as ArrayList<URL>

                    return path.toTypedArray()
                } catch (e: Exception) {
                    throw RuntimeException("Failed to read list of execution jars", e)
                }

            }

            throw RuntimeException("Failed to read list of execution jars")
        }

    private fun readResourcesConfigParsers(jarUrl: URL): List<String> {
        return readLines(jarUrl, "/META-INF/resources_configs")
    }

    private fun readLines(jarUrl: URL, filePath: String): List<String> {
        val lines = mutableListOf<String>()

        val inputFilePath = "jar:file:${jarUrl.path}!$filePath"

        var reader: BufferedReader? = null
        try {
            val inputURL = URL(inputFilePath)
            val conn = inputURL.openConnection() as JarURLConnection
            reader = BufferedReader(InputStreamReader(conn.inputStream))

            reader.lines().forEach { lines.add(it) }
        } catch (e: MalformedURLException) {
            System.err.println("Malformed input URL: $inputFilePath")
        } catch (e: IOException) {
            // Do nothing
        }
        finally {
            reader?.close()
        }

        return lines
    }

    private fun getJvmVersion(): Int {
        if (jvmVersion == null) {
            // Check for class version, ez
            var property = System.getProperty("java.class.version", "")
            if (property.isNotEmpty()) {
                jvmVersion = property.toFloatOrNull()?.let { (it - VERSION_OFFSET).toInt() }
            }

            if (jvmVersion == null) {
                property = System.getProperty("java.vm.specification.version", "")
                if (property.contains(".")) {
                    jvmVersion = property.substring(property.indexOf('.') + 1).toFloat().toInt()
                } else if (property.isNotEmpty()) {
                    jvmVersion = property.toInt()
                }
            }
        }

        if (jvmVersion == null) {
            jvmVersion = DEFAULT_JVM_VERSION
        }

        return jvmVersion ?: DEFAULT_JVM_VERSION
    }
}