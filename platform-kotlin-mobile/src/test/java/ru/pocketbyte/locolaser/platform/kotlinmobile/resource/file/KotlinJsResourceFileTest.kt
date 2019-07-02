package ru.pocketbyte.locolaser.platform.kotlinmobile.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.*

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import ru.pocketbyte.locolaser.platform.kotlinmobile.utils.TemplateStr

class KotlinJsResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = KotlinJsResourceFile(tempFolder.newFile(),
                "Str", "com.package", null, null)
        assertNull(resourceFile.read())
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOneItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resMap[PlatformResources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = KotlinJsResourceFile(testFile,
                "Str", "com.package", null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "package com.package\r\n" +
                "\r\n" +
                "import i18next.I18n\r\n" +
                "\r\n" +
                "public class Str(private val i18n: I18n) {\r\n" +
                "\r\n" +
                "    data class Plural(val count: Int)\r\n" +
                "\r\n" +
                "    /**\r\n" +
                "    * value1_1\r\n" +
                "    */\r\n" +
                "    public val key1: String\r\n" +
                "        get() = this.i18n.t(\"key1\")\r\n" +
                "\r\n" +
                "}"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOnePluralItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment 1", Quantity.ONE), ResValue("value1_2", "Comment 2", Quantity.OTHER))))
        resMap[PlatformResources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = KotlinJsResourceFile(testFile,
                "Str", "com.package", null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "package com.package\r\n" +
                "\r\n" +
                "import i18next.I18n\r\n" +
                "\r\n" +
                "public class Str(private val i18n: I18n) {\r\n" +
                "\r\n" +
                "    data class Plural(val count: Int)\r\n" +
                "\r\n" +
                "    /**\r\n" +
                "    * value1_2\r\n" +
                "    */\r\n" +
                "    public fun key1(count: Int): String {\r\n" +
                "        return this.i18n.t(\"key1\", Plural(count))\r\n" +
                "    }\r\n" +
                "\r\n" +
                "}"

        assertEquals(expectedResult, readFile(testFile))
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
        resMap[PlatformResources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = KotlinJsResourceFile(testFile,
                "StrImpl", "com.some.package", null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "package com.some.package\r\n" +
                "\r\n" +
                "import i18next.I18n\r\n" +
                "\r\n" +
                "public class StrImpl(private val i18n: I18n) {\r\n" +
                "\r\n" +
                "    data class Plural(val count: Int)\r\n" +
                "\r\n" +
                "    /**\r\n" +
                "    * value1_2\r\n" +
                "    */\r\n" +
                "    public val key1: String\r\n" +
                "        get() = this.i18n.t(\"key1\")\r\n" +
                "\r\n" +
                "    /**\r\n" +
                "    * value3_2\r\n" +
                "    */\r\n" +
                "    public val key3: String\r\n" +
                "        get() = this.i18n.t(\"key3\")\r\n" +
                "\r\n" +
                "}"

        assertEquals(expectedResult, readFile(testFile))
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
        resMap[PlatformResources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = KotlinJsResourceFile(testFile,
                "StrImpl", "com.some.package",
                "StrInterface", "com.interface")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "package com.some.package\r\n" +
                "\r\n" +
                "import i18next.I18n\r\n" +
                "import com.interface.StrInterface\r\n" +
                "\r\n" +
                "public class StrImpl(private val i18n: I18n): StrInterface {\r\n" +
                "\r\n" +
                "    data class Plural(val count: Int)\r\n" +
                "\r\n" +
                "    public override val key1: String\r\n" +
                "        get() = this.i18n.t(\"key1\")\r\n" +
                "\r\n" +
                "    public override val key3: String\r\n" +
                "        get() = this.i18n.t(\"key3\")\r\n" +
                "\r\n" +
                "}"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteLongPropertyComment() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue(
                "Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery " + "Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long Comment", null,
                Quantity.OTHER))))
        resMap[PlatformResources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = KotlinJsResourceFile(
                testFile, "Strings", "ru.pocketbyte", null, null)

        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "package ru.pocketbyte\r\n" +
                "\r\n" +
                "import i18next.I18n\r\n" +
                "\r\n" +
                "public class Strings(private val i18n: I18n) {\r\n" +
                "\r\n" +
                "    data class Plural(val count: Int)\r\n" +
                "\r\n" +
                "    /**\r\n" +
                "    * Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                " Wery Wery Wery Wery Wery Wery\r\n" +
                "    * Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long Comment\r\n" +
                "    */\r\n" +
                "    public val key1: String\r\n" +
                "        get() = this.i18n.t(\"key1\")\r\n" +
                "\r\n" +
                "}"

        assertEquals(expectedResult, readFile(testFile))
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
}
