package ru.pocketbyte.locolaser.json.resource.file

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
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType

class JsonResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testReadNotExistsFile() {
        val testFile = tempFolder.newFile()
        if (testFile.exists())
            assertTrue(testFile.delete())

        val resourceFile = JsonResourceFile(testFile, "en", -1)

        assertNull(resourceFile.read(ExtraParams()))
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

        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        val resMap = resourceFile.read(ExtraParams())

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
    fun testReadFormattedValue() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                "{\r\n" +
                    "    \"string1\":\"Formatted Value1: {{string}}\",\r\n" +
                    "    \"string2\":\"Not Formatted Value2\"\r\n" +
                    "}")

        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(
                ResValue("Formatted Value1: {{string}}", null, Quantity.OTHER,
                        WebFormattingType, WebFormattingType.argumentsFromValue("{{string}}")))))
        // Values without formatting must be NoFormattingType
        resLocale.put(prepareResItem("string2", arrayOf(
                ResValue("Not Formatted Value2", null, Quantity.OTHER,
                        NoFormattingType, null))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testSaveOrderWhenRead() {
        val testLocale = "ru"
        val testFile1 = prepareTestFile(
                "{\r\n" +
                    "    \"string1\":\"Value1\",\r\n" +
                    "    \"string2\":\"Value2\",\r\n" +
                    "    \"string3\":\"Value3\"\r\n" +
                    "}")

        assertEquals(
            listOf("string1", "string2", "string3"),
            JsonResourceFile(testFile1, testLocale, -1).read(ExtraParams())!![testLocale]!!.keys.map { it }
        )

        val testFile2 = prepareTestFile(
                "{\r\n" +
                    "    \"string3\":\"Value1\",\r\n" +
                    "    \"string2\":\"Value2\",\r\n" +
                    "    \"string1\":\"Value3\"\r\n" +
                    "}")

        assertEquals(
            listOf("string3", "string2", "string1"),
            JsonResourceFile(testFile2, testLocale, -1).read(ExtraParams())!![testLocale]!!.keys.map { it }
        )
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

        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        val resMap = resourceFile.read(ExtraParams())

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
    fun testReadFormattedPlural() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                "{" +
                    "\"key2\":\"value2_1\"," +
                    "\"key1_plural_2\":\"value1_3 {{count}}\"," +
                    "\"key1_plural_5\":\"{{string}} value1_1\"," +
                    "\"key1_plural_0\":\"value1_2\"" +
                    "}")

        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
                ResValue("{{string}} value1_1", null, Quantity.OTHER,
                        WebFormattingType, WebFormattingType.argumentsFromValue("{{string}}")),
                ResValue("value1_2", null, Quantity.ONE,
                        NoFormattingType, null),
                ResValue("value1_3 {{count}}", null, Quantity.MANY,
                        WebFormattingType, WebFormattingType.argumentsFromValue("{{count}}"))
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
        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        resourceFile.write(resMap, null)

        val expectedResult = "{\"key1\": \"value1_1\",\"key2\": \"value2_1\"}"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteFormatted() {
        val testLocale = "ru"
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
                ResValue("value1_1 {{count}}.", "Comment", Quantity.OTHER,
                        WebFormattingType, WebFormattingType.argumentsFromValue("{{count}}")))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        resourceFile.write(resMap, null)

        val expectedResult = "{\"key1\": \"value1_1 {{count}}.\"}"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteJavaFormatted() {
        val testLocale = "ru"
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
                ResValue("value1_1 %s.", "Comment", Quantity.OTHER,
                    JavaFormattingType, JavaFormattingType.argumentsFromValue("%s")?.map {
                        FormattingArgument("javaString", it.index, it.parameters)
                    }))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        resourceFile.write(resMap, null)

        val expectedResult = "{\"key1\": \"value1_1 {{javaString}}.\"}"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testSaveOrderWhenWrite() {
        val testLocale = "ru"

        val testFile1 = tempFolder.newFile()

        JsonResourceFile(testFile1, testLocale, -1).write(
            ResMap().apply {
                this[testLocale] = ResLocale().apply {
                    put(prepareResItem("key1", arrayOf(ResValue("v", null))))
                    put(prepareResItem("key2", arrayOf(ResValue("v", null))))
                    put(prepareResItem("key3", arrayOf(ResValue("v", null))))
                }
            },
            null
        )

        assertEquals(
            "{\"key1\": \"v\",\"key2\": \"v\",\"key3\": \"v\"}",
            readFile(testFile1)
        )

        val testFile2 = tempFolder.newFile()

        JsonResourceFile(testFile2, testLocale, -1).write(
            ResMap().apply {
                this[testLocale] = ResLocale().apply {
                    put(prepareResItem("key3", arrayOf(ResValue("v", null))))
                    put(prepareResItem("key2", arrayOf(ResValue("v", null))))
                    put(prepareResItem("key1", arrayOf(ResValue("v", null))))
                }
            },
            null
        )

        assertEquals(
            "{\"key3\": \"v\",\"key2\": \"v\",\"key1\": \"v\"}",
            readFile(testFile2)
        )
    }

    @Test
    @Throws(IOException::class)
    fun testWritePlurals() {
        val testLocale = "ru"

        val resMap = ResMap()

        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
                ResValue("value1_0", null, Quantity.ZERO),
                ResValue("value1_many", "Comment", Quantity.MANY),
                ResValue("value1_other", "Comment", Quantity.OTHER)
        )))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", "value2_1", Quantity.OTHER))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        resourceFile.write(resMap, null)

        val expectedResult =
                "{" +
                "\"key1_plural_2\": \"value1_many\"," +
                "\"key1_plural_3\": \"value1_other\"," +
                "\"key2\": \"value2_1\"" +
                "}"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWritePluralsFormatted() {
        val testLocale = "ru"

        val resMap = ResMap()

        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
                ResValue("value1_many {{count}}", "Comment", Quantity.MANY,
                        WebFormattingType, WebFormattingType.argumentsFromValue("{{count}}")),
                ResValue("value1_other {{count}}", "Comment", Quantity.OTHER,
                        WebFormattingType, WebFormattingType.argumentsFromValue("{{count}}"))
        )))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        resourceFile.write(resMap, null)

        val expectedResult =
                "{" +
                "\"key1_plural_2\": \"value1_many {{count}}\"," +
                "\"key1_plural_3\": \"value1_other {{count}}\"" +
                "}"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWritePluralsJavaFormatted() {
        val testLocale = "ru"

        val resMap = ResMap()

        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_many %d, %s", "Comment", Quantity.MANY,
                JavaFormattingType, JavaFormattingType.argumentsFromValue("%d %s")?.map {
                    FormattingArgument(
                        "some_${it.parameters?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String}",
                        it.index, it.parameters)
                }),
            ResValue("value1_other %1\$s", "Comment", Quantity.OTHER,
                JavaFormattingType, JavaFormattingType.argumentsFromValue("%1\$s")?.map {
                    FormattingArgument("javaString", it.index, it.parameters)
                })
        )))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = JsonResourceFile(testFile, testLocale, -1)
        resourceFile.write(resMap, null)

        val expectedResult =
                "{" +
                "\"key1_plural_2\": \"value1_many {{some_d}}, {{some_s}}\"," +
                "\"key1_plural_3\": \"value1_other {{javaString}}\"" +
                "}"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testSaveIntend() {
        val testLocale = "ru"
        val testFile = tempFolder.newFile()

        val resMap = ResMap().apply {
            this[testLocale] = ResLocale().apply {
                put(prepareResItem("key1", arrayOf(ResValue("v", null))))
                put(prepareResItem("key2", arrayOf(ResValue("v", null))))
                put(prepareResItem("key3", arrayOf(ResValue("v", null))))
            }
        }

        JsonResourceFile(testFile, testLocale, -1).write(resMap, null)
        assertEquals(
                "{\"key1\": \"v\",\"key2\": \"v\",\"key3\": \"v\"}",
                readFile(testFile)
        )

        JsonResourceFile(testFile, testLocale, 0).write(resMap, null)
        assertEquals(
                "{\n" +
                        "\"key1\": \"v\",\n" +
                        "\"key2\": \"v\",\n" +
                        "\"key3\": \"v\"\n" +
                        "}",
                readFile(testFile)
        )

        JsonResourceFile(testFile, testLocale, 2).write(resMap, null)
        assertEquals(
                "{\n" +
                        "  \"key1\": \"v\",\n" +
                        "  \"key2\": \"v\",\n" +
                        "  \"key3\": \"v\"\n" +
                        "}",
                readFile(testFile)
        )

        JsonResourceFile(testFile, testLocale, 10).write(resMap, null)
        assertEquals(
                "{\n" +
                        "          \"key1\": \"v\",\n" +
                        "          \"key2\": \"v\",\n" +
                        "          \"key3\": \"v\"\n" +
                        "}",
                readFile(testFile)
        )
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
