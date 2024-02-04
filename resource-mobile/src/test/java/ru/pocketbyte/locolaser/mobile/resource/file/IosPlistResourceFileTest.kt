package ru.pocketbyte.locolaser.mobile.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.*

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.duplicateComments
import ru.pocketbyte.locolaser.mobile.resource.file.AbsIosStringsResourceFileTest.Companion.PLATFORM_TEST_STRING
import ru.pocketbyte.locolaser.mobile.resource.file.AbsIosStringsResourceFileTest.Companion.TEST_STRING
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType

class IosPlistResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                "/* Comment */\r\n" +
                        " string1 = \"Value1\";\r\n" +
                        "string2= \"Value2\";\r\n" +
                        "\r\n" +
                        "string3 = \"Value 3\";")

        val resourceFile = IosPlistResourceFile(testFile, testLocale)
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
    fun testReadFormatted() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                "/* Comment */\r\n" +
                        " key1 = \"value1_1 %@\";\r\n" +
                        "key2= \"value2_1 %1\$.2f\";")

        val resourceFile = IosPlistResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1 %s", "Comment", Quantity.OTHER, JavaFormattingType,
                JavaFormattingType.argumentsFromValue("%s")
            ))))
        resLocale.put(prepareResItem("key2", arrayOf(
            ResValue("value2_1 %1\$.2f", null, Quantity.OTHER, JavaFormattingType,
                JavaFormattingType.argumentsFromValue("%1\$.2f")
            ))))
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
        val resourceFile = IosPlistResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.GENERATED_KEY_VALUE_PAIR_COMMENT + "\r\n\r\n" +
                "/* Comment */\r\n" +
                "key1 = \"value1_1\";\r\n" +
                "\r\n" +
                "/* value2_1 */\r\n" +
                "key2 = \"value2_1\";" +
                "key4 = \"value4_1\";")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteFormatted() {
        val testLocale = "ru"
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
                ResValue("value1_1 %s", null, Quantity.OTHER,
                        JavaFormattingType, JavaFormattingType.argumentsFromValue("%s")))))
        resLocale.put(prepareResItem("key2", arrayOf(
                ResValue("value2_1 %f", null, Quantity.OTHER,
                        JavaFormattingType, JavaFormattingType.argumentsFromValue("%f")))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosPlistResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.GENERATED_KEY_VALUE_PAIR_COMMENT + "\r\n\r\n" +
                "key1 = \"value1_1 %@\";\r\n" +
                "\r\n" +
                "key2 = \"value2_1 %f\";")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteWebFormatted() {
        val testLocale = "ru"
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
                ResValue("value1_1 {{s}}", null, Quantity.OTHER, WebFormattingType,
                        listOf(FormattingArgument(null, null, parameters("s", "")))
                ))))
        resLocale.put(prepareResItem("key2", arrayOf(
                ResValue("value2_1 {{amount}}", null, Quantity.OTHER, WebFormattingType,
                        listOf(FormattingArgument("amount", 1, parameters("f", ".2")))
                ))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosPlistResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.GENERATED_KEY_VALUE_PAIR_COMMENT + "\r\n\r\n" +
                "key1 = \"value1_1 %@\";\r\n" +
                "\r\n" +
                "key2 = \"value2_1 %1\$.2f\";")

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
        val resourceFile = IosPlistResourceFile(testFile, testLocale)
        resourceFile.write(resMap, writingConfig)

        val expectedResult = (TemplateStr.GENERATED_KEY_VALUE_PAIR_COMMENT + "\r\n\r\n" +
                "/* Comment */\r\n" +
                "key1 = \"value1_1\";\r\n" +
                "\r\n" +
                "key2 = \"value2_1\";")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testValueCorrectionWhenRead() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                (TemplateStr.GENERATED_KEY_VALUE_PAIR_COMMENT + "\r\n\r\n" +
                        "string1 = \"" + PLATFORM_TEST_STRING + "\";"))

        val resourceFile = IosPlistResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(
            ResValue(TEST_STRING, null, Quantity.OTHER,
                JavaFormattingType, JavaFormattingType.argumentsFromValue(TEST_STRING)))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testValueCorrectionWhenWrite() {
        val testLocale = "ru"

        val resMap = ResMap()

        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue(TEST_STRING, null, Quantity.OTHER))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosPlistResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.GENERATED_KEY_VALUE_PAIR_COMMENT + "\r\n\r\n" +
                "string1 = \"" + PLATFORM_TEST_STRING + "\";")

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

    private fun parameters(typeName: String, typeParameters: String): Map<String, String> {
        return mapOf(
            Pair(JavaFormattingType.PARAM_TYPE_NAME, typeName),
            Pair(JavaFormattingType.PARAM_TYPE_PARAMETERS, typeParameters)
        )
    }
}
