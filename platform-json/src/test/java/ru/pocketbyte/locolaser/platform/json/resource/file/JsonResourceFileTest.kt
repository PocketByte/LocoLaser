package ru.pocketbyte.locolaser.platform.json.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.entity.*

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.*
import ru.pocketbyte.locolaser.platform.json.resource.file.JsonResourceFile

class JsonResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testReadNotExistsFile() {
        val testFile = tempFolder.newFile()
        if (testFile.exists())
            assertTrue(testFile.delete())

        val resourceFile = JsonResourceFile(testFile, "en")

        assertNull(resourceFile.read())
    }

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                        "{\r\n" +
                            "    \"string1\":\"Value1\",\r\n" +
                            "    \"string2\":\"Value2\",\r\n" +
                            "    \"string3\":\"Value 3\"\r\n" +
                            "}")

        val resourceFile = JsonResourceFile(testFile, testLocale)
        val resMap = resourceFile.read()

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("Value1", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue("Value2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue("Value 3", null, Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testReadPlural() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                "{" +
                    "\"key2\":\"value2_1\"," +
                    "\"key1_plural_2\":\"value1_3\"," +
                    "\"key1_plural_5\":\"value1_1\"," +
                    "\"key1_plural_0\":\"value1_2\"" +
                    "}")

        val resourceFile = JsonResourceFile(testFile, testLocale)
        val resMap = resourceFile.read()

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
                ResValue("value1_1", null, Quantity.OTHER),
                ResValue("value1_2", null, Quantity.ONE),
                ResValue("value1_3", null, Quantity.MANY)
        )))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", null, Quantity.OTHER))))
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
        val resourceFile = JsonResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = "{\"key1\":\"value1_1\",\"key2\":\"value2_1\"}"

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
                ResValue("value1_2", null, Quantity.ZERO),
                ResValue("value1_3", "Comment", Quantity.MANY)
        )))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", "value2_1", Quantity.OTHER))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = JsonResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult =
                "{" +
                "\"key2\":\"value2_1\"," +
                "\"key1_plural_2\":\"value1_3\"," +
                "\"key1_plural_3\":\"value1_1\"" +
                "}"

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
