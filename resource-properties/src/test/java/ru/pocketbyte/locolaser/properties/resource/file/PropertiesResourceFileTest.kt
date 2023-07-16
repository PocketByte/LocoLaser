package ru.pocketbyte.locolaser.properties.resource.file

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.duplicateComments
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

class PropertiesResourceFileTest {

    companion object {
        private const val FILE_HEADER = PropertiesResourceFile.GENERATED_COMMENT + "\r\n\r\n"
    }

    @JvmField @Rule
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testReadNotExistsFile() {
        val testFile = tempFolder.newFile()
        if (testFile.exists())
            assertTrue(testFile.delete())

        val resourceFile = PropertiesResourceFile(testFile, "en")

        assertNull(resourceFile.read(null))
    }

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                        "# Comment\r\n" +
                        "string1=Value1\r\n" +
                        "string2=Value2\r\n" +
                        "\r\n" +
                        "string3=Value 3")

        val resourceFile = PropertiesResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(null)

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
    fun testReadWhitespaces() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                        "#   Comment    \r\n" +
                        "   string1   =  Value1  ")

        val resourceFile = PropertiesResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(null)

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("   string1   ", arrayOf(ResValue("  Value1  ", "Comment", Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testReadComments() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
            FILE_HEADER +
            "# This comment must be ignored\r\n" +
            "\r\n" +
            "no_comment=No comment\r\n" +
            "# This comment must be not ignored\r\n" +
            "has_comment=Has comment\r\n" +
            "# Comment line 1\r\n" +
            "# Comment line 2\r\n" +
            "multiline_comment=Multiline comment\r\n" +
            "# Ignored Comment line 1\r\n" +
            "\r\n" +
            "# Comment line 2\r\n" +
            "multiline_ignore_comment=Multiline comment (ignore)\r\n"
        )

        val resourceFile = PropertiesResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(null)

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("no_comment", arrayOf(ResValue("No comment", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("has_comment", arrayOf(ResValue("Has comment", "This comment must be not ignored", Quantity.OTHER))))
        resLocale.put(prepareResItem("multiline_comment", arrayOf(ResValue("Multiline comment", "Comment line 1\nComment line 2", Quantity.OTHER))))
        resLocale.put(prepareResItem("multiline_ignore_comment", arrayOf(ResValue("Multiline comment (ignore)", "Comment line 2", Quantity.OTHER))))
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
        resMap[testLocale] = resLocale

        // Redundant locale. Shouldn't be written into file.
        resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value3_2", "value2_1", Quantity.OTHER))))
        resMap[redundantLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = PropertiesResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = FILE_HEADER +
            "# Comment\r\n" +
            "key1=value1_1\r\n" +
            "# value2_1\r\n" +
            "key2=value2_1\r\n"

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

        val extraParams = ExtraParams()
        extraParams.duplicateComments = false

        val testFile = tempFolder.newFile()
        val resourceFile = PropertiesResourceFile(testFile, testLocale)
        resourceFile.write(resMap, extraParams)

        val expectedResult = FILE_HEADER +
            "# Comment\r\n" +
            "key1=value1_1\r\n" +
            "key2=value2_1\r\n"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWritePlurals() {
        val testLocale = "ru"

        val resMap = ResMap()

        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1", "Comment", Quantity.OTHER),
            ResValue("value1_2", "Comment One", Quantity.ONE),
            ResValue("value1_3", "Comment Few", Quantity.FEW),
        )))
        resLocale.put(prepareResItem("key2", arrayOf(
            ResValue("value2_1", "value2_1", Quantity.OTHER),
            ResValue("value2_2", "value2_2", Quantity.MANY),
        )))
        resMap[testLocale] = resLocale

        val extraParams = ExtraParams()
        extraParams.duplicateComments = false

        val testFile = tempFolder.newFile()
        val resourceFile = PropertiesResourceFile(testFile, testLocale)
        resourceFile.write(resMap, extraParams)

        val expectedResult = FILE_HEADER +
            "# Comment\r\n" +
            "key1.other=value1_1\r\n" +
            "# Comment One\r\n" +
            "key1.one=value1_2\r\n" +
            "# Comment Few\r\n" +
            "key1.few=value1_3\r\n" +
            "key2.other=value2_1\r\n" +
            "key2.many=value2_2\r\n"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testReadPlurals() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
            "# Comment\r\n" +
            "key1.other=value1_1\r\n" +
            "# Comment One\r\n" +
            "key1.one=value1_2\r\n" +
            "# Comment Few\r\n" +
            "key1.few=value1_3\r\n" +
            "key2.other=value2_1\r\n" +
            "key2.many=value2_2\r\n"
        )

        val resourceFile = PropertiesResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(null)

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()

        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1", "Comment", Quantity.OTHER, NoFormattingType, emptyList()),
            ResValue("value1_2", "Comment One", Quantity.ONE, NoFormattingType, emptyList()),
            ResValue("value1_3", "Comment Few", Quantity.FEW, NoFormattingType, emptyList()),
        )))
        resLocale.put(prepareResItem("key2", arrayOf(
            ResValue("value2_1", null, Quantity.OTHER, NoFormattingType, emptyList()),
            ResValue("value2_2", null, Quantity.MANY, NoFormattingType, emptyList()),
        )))

        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Throws(IOException::class)
    private fun prepareTestFile(text:String):File {
        val file = tempFolder.newFile()
        val writer = PrintWriter(file)
        writer.write(text)
        writer.flush()
        writer.close()
        return file
    }

    @Throws(IOException::class)
    private fun readFile(file:File):String {
        return String(Files.readAllBytes(Paths.get(file.absolutePath)), Charset.defaultCharset())
    }

    private fun prepareResItem(key:String, values:Array<ResValue>):ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }
}
