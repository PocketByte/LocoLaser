/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.*

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.*

/**
 * @author Denis Shurygin
 */
class AndroidResourceFileTest {

    companion object {
        private const val testString =
                "?'test';:<tag>\"value\nsecond line?\" %s<tagg/> &@string"
        private const val platformTestString =
                "\\?\\'test\\';:&lt;tag>\\\"value\\nsecond line?\\\" %s&lt;tagg/> &amp;\\@string"
    }

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    fun testToPlatformValue() {
        assertEquals(platformTestString, AndroidResourceFile.toPlatformValue(testString))
    }

    @Test
    fun testFromPlatformValue() {
        assertEquals(testString, AndroidResourceFile.fromPlatformValue(platformTestString))
    }

    @Test
    @Throws(IOException::class)
    fun testReadNotExistsFile() {
        val testFile = tempFolder.newFile()
        if (testFile.exists())
            assertTrue(testFile.delete())

        val resourceFile = AndroidResourceFile(testFile, "en")

        assertNull(resourceFile.read())
    }

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                TemplateStr.XML_DECLARATION + "\r\n" +
                        TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                        "<resources>\r\n" +
                        "    /* Comment */\r\n" +
                        "    <string name=\"string1\">Value1</string>\r\n" +
                        "    <string name=\"string2\">Value2</string>\r\n" +
                        "\r\n" +
                        "    <string name=\"string3\">Value 3</string>\r\n" +
                        "</resources>")

        val resourceFile = AndroidResourceFile(testFile, testLocale)
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
    fun testReadPlurals() {
        val testLocale = "en"
        val testFile = prepareTestFile(
                (TemplateStr.XML_DECLARATION + "\r\n" +
                        TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                        "<resources>\r\n" +
                        "    /* Comment */\r\n" +
                        "    <plurals name=\"string1\">\r\n" +
                        "        <item quantity=\"other\">String</item>\r\n" +
                        "        <item quantity=\"one\">String one</item>\r\n" +
                        "        <item quantity=\"two\">String two</item>\r\n" +
                        "    </plurals>\r\n" +
                        "    <string name=\"string2\">Value2</string>\r\n" +
                        "\r\n" +
                        "    <plurals name=\"string3\">\r\n" +
                        "        /* Comment */\r\n" +
                        "        <item quantity=\"other\">String 3</item>\r\n" +
                        "        <item quantity=\"few\">String 3 few</item>\r\n" +
                        "        <item quantity=\"zero\">String 3 zero</item>\r\n" +
                        "    </plurals>\r\n" +
                        "</resources>"))

        val resourceFile = AndroidResourceFile(testFile, testLocale)
        val resMap = resourceFile.read()

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("String", null, Quantity.OTHER), ResValue("String one", null, Quantity.ONE), ResValue("String two", null, Quantity.TWO))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue("Value2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue("String 3", null, Quantity.OTHER), ResValue("String 3 few", null, Quantity.FEW), ResValue("String 3 zero", null, Quantity.ZERO))))
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
        val resourceFile = AndroidResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.XML_DECLARATION + "\r\n" +
                TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                "<resources>\r\n" +
                "    /* Comment */\r\n" +
                "    <string name=\"key1\">value1_1</string>\r\n" +
                "    /* value2_1 */\r\n" +
                "    <string name=\"key2\">value2_1</string>\r\n" +
                "</resources>")

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

        val writingConfig = WritingConfig()
        writingConfig.isDuplicateComments = false

        val testFile = tempFolder.newFile()
        val resourceFile = AndroidResourceFile(testFile, testLocale)
        resourceFile.write(resMap, writingConfig)

        val expectedResult = (TemplateStr.XML_DECLARATION + "\r\n" +
                TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                "<resources>\r\n" +
                "    /* Comment */\r\n" +
                "    <string name=\"key1\">value1_1</string>\r\n" +
                "    <string name=\"key2\">value2_1</string>\r\n" +
                "</resources>")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWritePlurals() {
        val testLocale = "en"

        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("String", "Comment", Quantity.OTHER), ResValue("String one", null, Quantity.ONE), ResValue("String two", null, Quantity.TWO))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue("Value2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue("String 3", "Comment", Quantity.OTHER), ResValue("String 3 few", null, Quantity.FEW), ResValue("String 3 zero", null, Quantity.ZERO))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = AndroidResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.XML_DECLARATION + "\r\n" +
                TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                "<resources>\r\n" +
                "    <plurals name=\"string1\">\r\n" +
                "        <item quantity=\"other\">String</item>\r\n" +
                "        <item quantity=\"one\">String one</item>\r\n" +
                "        <item quantity=\"two\">String two</item>\r\n" +
                "    </plurals>\r\n" +
                "    <string name=\"string2\">Value2</string>\r\n" +
                "    <plurals name=\"string3\">\r\n" +
                "        <item quantity=\"other\">String 3</item>\r\n" +
                "        <item quantity=\"few\">String 3 few</item>\r\n" +
                "        <item quantity=\"zero\">String 3 zero</item>\r\n" +
                "    </plurals>\r\n" +
                "</resources>")

        assertEquals(expectedResult, readFile(testFile))
    }


    @Test
    @Throws(IOException::class)
    fun testValueCorrectionWhenRead() {
        val testLocale = "en"
        val testFile = prepareTestFile(
                (TemplateStr.XML_DECLARATION + "\r\n" +
                        TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                        "<resources>\r\n" +
                        "    <plurals name=\"string1\">\r\n" +
                        "        <item quantity=\"other\">String\\'</item>\r\n" +
                        "        <item quantity=\"one\">\\?String one</item>\r\n" +
                        "    </plurals>\r\n" +
                        "    <string name=\"string2\">\\\"Value2</string>\r\n" +
                        "</resources>"))

        val resourceFile = AndroidResourceFile(testFile, testLocale)
        val resMap = resourceFile.read()

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("String'", null, Quantity.OTHER), ResValue("?String one", null, Quantity.ONE))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue("\"Value2", null, Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testValueCorrectionWhenWrite() {
        val testLocale = "en"

        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("String'", null, Quantity.OTHER), ResValue("?String one", null, Quantity.ONE))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue("\"Value2", null, Quantity.OTHER))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = AndroidResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.XML_DECLARATION + "\r\n" +
                TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                "<resources>\r\n" +
                "    <plurals name=\"string1\">\r\n" +
                "        <item quantity=\"other\">String\\'</item>\r\n" +
                "        <item quantity=\"one\">\\?String one</item>\r\n" +
                "    </plurals>\r\n" +
                "    <string name=\"string2\">\\\"Value2</string>\r\n" +
                "</resources>")

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
