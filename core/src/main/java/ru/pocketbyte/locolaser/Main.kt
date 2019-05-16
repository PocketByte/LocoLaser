/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser

import org.json.simple.parser.ParseException
import ru.pocketbyte.locolaser.config.parser.*
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import sun.misc.Unsafe

import java.io.*
import java.net.JarURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.LinkedHashSet

/**
 * @author Denis Shurygin
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            buildParser().fromArguments(args).forEach { config ->
                if (!LocoLaser.localize(config)) {
                    System.exit(1)
                    return
                }
            }
        } catch (e: InvalidConfigException) {
            System.err.println("ERROR: " + e.message)
            System.exit(1)
        } catch (e: IOException) {
            System.err.print("ERROR: ")
            e.printStackTrace()
            System.exit(1)
        } catch (e: ParseException) {
            System.err.print("ERROR: ")
            e.printStackTrace()
            System.exit(1)
        } catch (e: IllegalAccessException) {
            System.err.print("ERROR: ")
            e.printStackTrace()
            System.exit(1)
        } catch (e: InstantiationException) {
            System.err.print("ERROR: ")
            e.printStackTrace()
            System.exit(1)
        } catch (e: ClassNotFoundException) {
            System.err.print("ERROR: ")
            e.printStackTrace()
            System.exit(1)
        }

    }

    @Throws(ClassNotFoundException::class, IllegalAccessException::class, InstantiationException::class, IOException::class)
    private fun buildParser(): ConfigParser {
        val platformConfigParsers = LinkedHashSet<PlatformConfigParser<*>>()
        val sourceConfigParsers = LinkedHashSet<SourceConfigParser<*>>()

        for (url in jarUrls) {
            readPlatformConfigParsers(url)
                    .mapTo(platformConfigParsers) {
                        Class.forName(it).newInstance() as PlatformConfigParser<*>
                    }

            readSourceConfigParsers(url)
                    .mapTo(sourceConfigParsers) {
                        Class.forName(it).newInstance() as SourceConfigParser<*>
                    }
        }

        if (platformConfigParsers.size == 0)
            throw RuntimeException("Platform Config parser not found")

        sourceConfigParsers.add(EmptySourceConfigParser())

        return ConfigParser(
                SourceSetConfigParser(sourceConfigParsers),
                PlatformSetConfigParser(platformConfigParsers))
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
                    val ucpField = classLoader.javaClass.getDeclaredField("ucp")
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

    private fun readPlatformConfigParsers(jarUrl: URL): List<String> {
        return readLines(jarUrl, "/META-INF/platform_configs")
    }

    private fun readSourceConfigParsers(jarUrl: URL): List<String> {
        return readLines(jarUrl, "/META-INF/source_configs")
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
}
