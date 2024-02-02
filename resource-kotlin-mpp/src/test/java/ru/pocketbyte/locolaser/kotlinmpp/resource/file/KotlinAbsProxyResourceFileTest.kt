package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsResources
import ru.pocketbyte.locolaser.kotlinmpp.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResLocale
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.ResValue
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

class KotlinAbsProxyResourceFileTest {

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = KotlinAbsProxyResourceFile(tempFolder.newFile(),
            "StrProxy", "com.package", "Str", "com.package")
        assertNull(resourceFile.read(ExtraParams()))
    }


    @Test
    @Throws(IOException::class)
    fun testWriteOneItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1", "Comment", Quantity.OTHER)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "StrProxy"
        val interfaceName = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinAbsProxyResourceFile(
            testDirectory, className, classPackage, interfaceName, classPackage
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.String\n" +
                "\n" +
                "public abstract class $className : $interfaceName {\n" +
                "  protected abstract val stringRepository: $interfaceName\n" +
                "\n" +
                "  public override val key1: String\n" +
                "    get() = stringRepository.key1\n" +
                "}\n"

        assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOnePluralItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1", "Comment 1", Quantity.ONE),
            ResValue("value1_2", "Comment 2", Quantity.OTHER)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val interfaceName = "Str"
        val className = "StrProxy"
        val classPackage = "com.pcg"
        val resourceFile = KotlinAbsProxyResourceFile(testDirectory, className, classPackage, interfaceName, classPackage)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.Long\n" +
                "import kotlin.String\n" +
                "\n" +
                "public abstract class $className : $interfaceName {\n" +
                "  protected abstract val stringRepository: $interfaceName\n" +
                "\n" +
                "  public override fun key1(count: Long): String = stringRepository.key1(count)\n" +
                "}\n"

        assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
    }

    @Test
    @Throws(IOException::class)
    fun testWrite() {
        val resMap = ResMap()

        var resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", "value2_1", Quantity.OTHER))))
        resMap["ru"] = resLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value3_2", "value2_1", Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val interfaceName = "Str"
        val className = "StrImpl"
        val classPackage = "com.some.pcg"
        val resourceFile = KotlinAbsProxyResourceFile(testDirectory, className, classPackage, interfaceName, classPackage)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.String\n" +
                "\n" +
                "public abstract class $className : $interfaceName {\n" +
                "  protected abstract val stringRepository: $interfaceName\n" +
                "\n" +
                "  public override val key1: String\n" +
                "    get() = stringRepository.key1\n" +
                "\n" +
                "  public override val key3: String\n" +
                "    get() = stringRepository.key3\n" +
                "}\n"

        assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
    }

    @Test
    @Throws(IOException::class)
    fun testWriteWithInterface() {
        val resMap = ResMap()

        var resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", "value2_1", Quantity.OTHER))))
        resMap["ru"] = resLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value3_2", "value2_1", Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "StrImpl"
        val classPackage = "com.some.pcg"
        val interfaceName = "StrInterface"
        val interfacePackage = "com.some.interfacepcg"
        val resourceFile = KotlinAbsProxyResourceFile(
            testDirectory, className, classPackage,
            interfaceName, interfacePackage
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import $interfacePackage.$interfaceName\n" +
                "import kotlin.String\n" +
                "\n" +
                "public abstract class $className : $interfaceName {\n" +
                "  protected abstract val stringRepository: $interfaceName\n" +
                "\n" +
                "  public override val key1: String\n" +
                "    get() = stringRepository.key1\n" +
                "\n" +
                "  public override val key3: String\n" +
                "    get() = stringRepository.key3\n" +
                "}\n"

        assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
    }

    @Throws(IOException::class)
    private fun readFile(file: File): String {
        return String(Files.readAllBytes(Paths.get(file.absolutePath)), Charset.defaultCharset())
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }

    private fun fileForClass(directory: File, className: String, classPackage: String): File {
        return File(
            File(directory, classPackage.replace(".", "/")),
            "$className.${KotlinAbsResources.KOTLIN_FILE_EXTENSION}"
        )
    }
}