package ru.pocketbyte.locolaser.gettext.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.*

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.*
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.duplicateComments

class GetTextResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testReadNotExistsFile() {
        val testFile = tempFolder.newFile()
        if (testFile.exists())
            assertTrue(testFile.delete())

        val resourceFile = GetTextResourceFile(testFile, "en")

        assertNull(resourceFile.read(ExtraParams()))
    }

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                GetTextResourceFile.GENERATED_GETTEXT_COMMENT + "\r\n\r\n" +
                        "# Comment\r\n" +
                        "msgid \"string1\"\r\n" +
                        "msgstr \"Value1\"\r\n" +
                        "msgid \"string2\"\r\n" +
                        "msgstr \"Value2\"\r\n" +
                        "\r\n" +
                        "msgid \"string3\"\r\n" +
                        "msgstr \"Value 3\"")

        val resourceFile = GetTextResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("Value1", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue("Value2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue("Value 3", null, Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testReadMultiline() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                (GetTextResourceFile.GENERATED_GETTEXT_COMMENT + "\r\n\r\n" +
                        "# Comment\r\n" +
                        "msgid \"string1\"\r\n" +
                        "msgstr \"Value1\"\r\n" +
                        " \"Value1 string 2\"\r\n" +
                        "      \"Value1 string 3\"\r\n" +
                        "msgid \"string2\"\r\n" +
                        "msgstr \"Value2\"\r\n" +
                        "\"Value2 string 2\"\r\n" +
                        "\r\n" +
                        "msgid \"string3\"\r\n" +
                        "msgstr \"Value 3\"\r\n" +
                        "       \"Value 3 string 2\"\r\n" +
                        " \"Value 3 string 3\""))

        val resourceFile = GetTextResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue(
                "Value1Value1 string 2Value1 string 3", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue(
                "Value2Value2 string 2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue(
                "Value 3Value 3 string 2Value 3 string 3", null, Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }


    @Test
    @Throws(IOException::class)
    fun testReadMultilineInvalid() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                (GetTextResourceFile.GENERATED_GETTEXT_COMMENT + "\r\n\r\n" +
                        "# Comment\r\n" +
                        "msgid \"string1\"\r\n" +
                        "msgstr \"Value1\"\r\n" +
                        "msgstr \"Value1 string 2\"\r\n" +
                        "      \"Value1 string 3\"\r\n" +
                        "msgid \"string2\"\r\n" +
                        "msgstr \"Value2\"\r\n" +
                        "   error    \"Value2 string 2\"\r\n" +
                        "       \"Value 3 string 2\""))

        val resourceFile = GetTextResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue(
                "Value1", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue(
                "Value2", null, Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }


    @Test
    @Throws(IOException::class)
    fun testWrite() {
        val testLocale = "ru"
        val redundantLocale = "base"

        val resMap = ResMap()

        var resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", "value2_1", Quantity.OTHER))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value4_1", "", Quantity.OTHER))))
        resMap[testLocale] = resLocale

        // Redundant locale. Shouldn't be written into file.
        resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value3_2", "value2_1", Quantity.OTHER))))
        resMap[redundantLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = GetTextResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (
            GetTextResourceFile.GENERATED_GETTEXT_COMMENT + "\r\n\r\n" +
                "# Comment\r\n" +
                "msgid \"key1\"\r\n" +
                "msgstr \"value1_1\"\r\n" +
                "\r\n" +
                "# value2_1\r\n" +
                "msgid \"key2\"\r\n" +
                "msgstr \"value2_1\"" +
                "\r\n" +
                "msgid \"key4\"\r\n" +
                "msgstr \"value4_1\""
        )

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteWithWritingConfig() {
        val testLocale = "ru"

        val resMap = ResMap()

        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", "value2_1", Quantity.OTHER))))
        resMap[testLocale] = resLocale

        val writingConfig = ExtraParams()
        writingConfig.duplicateComments = false

        val testFile = tempFolder.newFile()
        val resourceFile = GetTextResourceFile(testFile, testLocale)
        resourceFile.write(resMap, writingConfig)

        val expectedResult = (GetTextResourceFile.GENERATED_GETTEXT_COMMENT + "\r\n\r\n" +
                "# Comment\r\n" +
                "msgid \"key1\"\r\n" +
                "msgstr \"value1_1\"\r\n" +
                "\r\n" +
                "msgid \"key2\"\r\n" +
                "msgstr \"value2_1\"")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Throws(IOException::class)
    private fun prepareTestFile(text: String): File {
        val file = tempFolder.newFile()
        val writer = PrintWriter(file)
        writer.write(text)
        writer.flush()
        writer.close()
        return file
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
