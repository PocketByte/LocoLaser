/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
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
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType

/**
 * @author Denis Shurygin
 */
class IosPluralResourceFileTest {

    companion object {
        private const val testString = "?'test';:<tag>\"value\nsecond line\" %d<tagg/>"
        private const val platformTestString = "?\\'test\\';:&lt;tag>\\\"value\\nsecond line\\\" %d&lt;tagg/>"
    }

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    fun testToPlatformValue() {
        assertEquals(platformTestString, IosPluralResourceFile.toPlatformValue(testString))
    }

    @Test
    fun testFromPlatformValue() {
        assertEquals(testString, IosPluralResourceFile.fromPlatformValue(platformTestString))
    }

    @Test
    @Throws(IOException::class)
    fun testReadPlurals() {
        val testLocale = "en"
        val testFile = prepareTestFile(
                TemplateStr.XML_DECLARATION + "\r\n" +
                        TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                        "<plist version=\"1.0\">\r\n" +
                        "<dict>\r\n" +
                        "    <key>string1</key>\r\n" +
                        "    <dict>\r\n" +
                        "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                        "        <string>%#@value@</string>\r\n" +
                        "        <key>value</key>\r\n" +
                        "        <dict>\r\n" +
                        "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                        "            <string>NSStringPluralRuleType</string>\r\n" +
                        "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                        "            <string>d</string>\r\n" +
                        "            <key>one</key>\r\n" +
                        "            <string>String one</string>\r\n" +
                        "            <key>two</key>\r\n" +
                        "            <string>String two</string>\r\n" +
                        "            <key>other</key>\r\n" +
                        "            <string>String</string>\r\n" +
                        "        </dict>\r\n" +
                        "    </dict>\r\n" +
                        "    <key>string3</key>\r\n" +
                        "    <dict>\r\n" +
                        "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                        "        <string>%#@value@</string>\r\n" +
                        "        <key>value</key>\r\n" +
                        "        <dict>\r\n" +
                        "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                        "            <string>NSStringPluralRuleType</string>\r\n" +
                        "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                        "            <string>d</string>\r\n" +
                        "            <key>zero</key>\r\n" +
                        "            <string>String 3 zero</string>\r\n" +
                        "            <key>few</key>\r\n" +
                        "            <string>String 3 few</string>\r\n" +
                        "            <key>other</key>\r\n" +
                        "            <string>String 3</string>\r\n" +
                        "        </dict>\r\n" +
                        "    </dict>\r\n" +
                        "</dict>\r\n" +
                        "</plist>")

        val resourceFile = IosPluralResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("String", null, Quantity.OTHER), ResValue("String one", null, Quantity.ONE), ResValue("String two", null, Quantity.TWO))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue("String 3", null, Quantity.OTHER), ResValue("String 3 few", null, Quantity.FEW), ResValue("String 3 zero", null, Quantity.ZERO))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testReadPluralsFormatted() {
        val testLocale = "en"
        val testFile = prepareTestFile(
                TemplateStr.XML_DECLARATION + "\r\n" +
                    TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                    "<plist version=\"1.0\">\r\n" +
                    "<dict>\r\n" +
                    "    <key>string1</key>\r\n" +
                    "    <dict>\r\n" +
                    "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                    "        <string>%#@value@</string>\r\n" +
                    "        <key>value</key>\r\n" +
                    "        <dict>\r\n" +
                    "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                    "            <string>NSStringPluralRuleType</string>\r\n" +
                    "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                    "            <string>f</string>\r\n" +
                    "            <key>one</key>\r\n" +
                    "            <string>String one</string>\r\n" +
                    "            <key>two</key>\r\n" +
                    "            <string>String two</string>\r\n" +
                    "            <key>other</key>\r\n" +
                    "            <string>String %@</string>\r\n" +
                    "        </dict>\r\n" +
                    "    </dict>\r\n" +
                    "    <key>string3</key>\r\n" +
                    "    <dict>\r\n" +
                    "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                    "        <string>%#@value@</string>\r\n" +
                    "        <key>value</key>\r\n" +
                    "        <dict>\r\n" +
                    "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                    "            <string>NSStringPluralRuleType</string>\r\n" +
                    "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                    "            <string>d</string>\r\n" +
                    "            <key>zero</key>\r\n" +
                    "            <string>String 3 zero</string>\r\n" +
                    "            <key>few</key>\r\n" +
                    "            <string>String %d few</string>\r\n" +
                    "            <key>other</key>\r\n" +
                    "            <string>String 3 %1\$d.</string>\r\n" +
                    "        </dict>\r\n" +
                    "    </dict>\r\n" +
                    "</dict>\r\n" +
                    "</plist>")

        val resourceFile = IosPluralResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(
            ResValue("String one", null, Quantity.ONE),
            ResValue("String two", null, Quantity.TWO),
            ResValue("String %s", null, Quantity.OTHER, JavaFormattingType,
                JavaFormattingType.argumentsFromValue("%s")
            ))))
        resLocale.put(prepareResItem("string3", arrayOf(
            ResValue("String 3 zero", null, Quantity.ZERO),
            ResValue("String %d few", null, Quantity.FEW, JavaFormattingType,
                JavaFormattingType.argumentsFromValue("%d")
            ),
            ResValue("String 3 %1\$d.", null, Quantity.OTHER, JavaFormattingType,
                JavaFormattingType.argumentsFromValue("%1\$d")
            ))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testWritePlurals() {
        val testLocale = "en"
        val redundantLocale = "base"

        val resMap = ResMap()
        var resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("String", "Comment", Quantity.OTHER), ResValue("String one", null, Quantity.ONE), ResValue("String two", null, Quantity.TWO))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue("String 3", "Comment", Quantity.OTHER), ResValue("String 3 few", null, Quantity.FEW), ResValue("String 3 zero", null, Quantity.ZERO))))

        // Not plural values shouldn't be written into file
        resLocale.put(prepareResItem("string2", arrayOf(ResValue("Value2", null, Quantity.OTHER))))
        resMap[testLocale] = resLocale

        // Redundant locale. Shouldn't be written into file.
        resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value3_2", "value2_1", Quantity.OTHER))))
        resMap[redundantLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosPluralResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.XML_DECLARATION + "\r\n" +
                TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                "<plist version=\"1.0\">\r\n" +
                "<dict>\r\n" +
                "    <key>string1</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>d</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>String</string>\r\n" +
                "            <key>one</key>\r\n" +
                "            <string>String one</string>\r\n" +
                "            <key>two</key>\r\n" +
                "            <string>String two</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "    <key>string3</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>d</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>String 3</string>\r\n" +
                "            <key>few</key>\r\n" +
                "            <string>String 3 few</string>\r\n" +
                "            <key>zero</key>\r\n" +
                "            <string>String 3 zero</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "</dict>\r\n" +
                "</plist>")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWritePluralsFormatted() {
        val testLocale = "base"

        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(
            prepareResItem(
                "string1", arrayOf(
                    ResValue("String", "Comment", Quantity.OTHER),
                    ResValue("String %d", null, Quantity.ONE,
                        JavaFormattingType, JavaFormattingType.argumentsFromValue("%d")
                    )
                )
            )
        )
        resLocale.put(
            prepareResItem(
                "string2", arrayOf(
                    ResValue("String", "Comment", Quantity.OTHER),
                    ResValue("String %f", null, Quantity.ONE,
                        JavaFormattingType, JavaFormattingType.argumentsFromValue("%f")
                    )
                )
            )
        )
        resLocale.put(
            prepareResItem(
                "string3", arrayOf(
                    ResValue("String %1\$d", "Comment", Quantity.OTHER,
                        JavaFormattingType, JavaFormattingType.argumentsFromValue("%1\$d")
                    ),
                    ResValue("String two %1\$d", null, Quantity.TWO,
                        JavaFormattingType, JavaFormattingType.argumentsFromValue("%1\$d")
                    )
                )
            )
        )
        resLocale.put(
            prepareResItem(
                "string4", arrayOf(
                    ResValue("String %1\$f.2", "Comment", Quantity.OTHER,
                        JavaFormattingType, JavaFormattingType.argumentsFromValue("%1\$f.2")
                    ),
                    ResValue("String few %1\$f.2", null, Quantity.FEW,
                        JavaFormattingType, JavaFormattingType.argumentsFromValue("%1\$f.2")
                    )
                )
            )
        )
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosPluralResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.XML_DECLARATION + "\r\n" +
                TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                "<plist version=\"1.0\">\r\n" +
                "<dict>\r\n" +
                "    <key>string1</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>d</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>String</string>\r\n" +
                "            <key>one</key>\r\n" +
                "            <string>String %d</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "    <key>string2</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>f</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>String</string>\r\n" +
                "            <key>one</key>\r\n" +
                "            <string>String %f</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "    <key>string3</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>d</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>String %1\$d</string>\r\n" +
                "            <key>two</key>\r\n" +
                "            <string>String two %1\$d</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "    <key>string4</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>f</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>String %1\$f.2</string>\r\n" +
                "            <key>few</key>\r\n" +
                "            <string>String few %1\$f.2</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "</dict>\r\n" +
                "</plist>")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWritePluralsWebFormatted() {
        val testLocale = "en"
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(
            ResValue("String one", null, Quantity.ONE),
            ResValue("String two", null, Quantity.TWO),
            ResValue("String {{str}}", "Comment", Quantity.OTHER, WebFormattingType,
                WebFormattingType.argumentsFromValue("{{str}}")
            ))))
        resLocale.put(prepareResItem("string3", arrayOf(
            ResValue("String 3 zero", null, Quantity.ZERO),
            ResValue("String {{count}} few", null, Quantity.FEW, WebFormattingType,
                listOf(FormattingArgument("count", null, parameters("d", "")))
            ),
            ResValue("String 3 {{count}}.", "Comment", Quantity.OTHER, WebFormattingType,
                listOf(FormattingArgument("count", null, parameters("d", "")))
            ))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosPluralResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.XML_DECLARATION + "\r\n" +
                TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                "<plist version=\"1.0\">\r\n" +
                "<dict>\r\n" +
                "    <key>string1</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>@</string>\r\n" +
                "            <key>one</key>\r\n" +
                "            <string>String one</string>\r\n" +
                "            <key>two</key>\r\n" +
                "            <string>String two</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>String %@</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "    <key>string3</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>d</string>\r\n" +
                "            <key>zero</key>\r\n" +
                "            <string>String 3 zero</string>\r\n" +
                "            <key>few</key>\r\n" +
                "            <string>String %d few</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>String 3 %d.</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "</dict>\r\n" +
                "</plist>")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testValueCorrectionWhenRead() {
        val testLocale = "en"
        val testFile = prepareTestFile(
                (TemplateStr.XML_DECLARATION + "\r\n" +
                        TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                        "<plist version=\"1.0\">\r\n" +
                        "<dict>\r\n" +
                        "    <key>string1</key>\r\n" +
                        "    <dict>\r\n" +
                        "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                        "        <string>%#@value@</string>\r\n" +
                        "        <key>value</key>\r\n" +
                        "        <dict>\r\n" +
                        "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                        "            <string>NSStringPluralRuleType</string>\r\n" +
                        "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                        "            <string>d</string>\r\n" +
                        "            <key>other</key>\r\n" +
                        "            <string>" + platformTestString + "</string>\r\n" +
                        "        </dict>\r\n" +
                        "    </dict>\r\n" +
                        "</dict>\r\n" +
                        "</plist>"))

        val resourceFile = IosPluralResourceFile(testFile, testLocale)
        val resMap = resourceFile.read(ExtraParams())

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(
            ResValue(testString, null, Quantity.OTHER,
                JavaFormattingType, JavaFormattingType.argumentsFromValue(testString)))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testValueCorrectionWhenWrite() {
        val testLocale = "en"

        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue(testString, null, Quantity.OTHER), ResValue("one", null, Quantity.ONE))))
        resMap[testLocale] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosPluralResourceFile(testFile, testLocale)
        resourceFile.write(resMap, null)

        val expectedResult = (TemplateStr.XML_DECLARATION + "\r\n" +
                TemplateStr.GENERATED_XML_COMMENT + "\r\n\r\n" +
                "<plist version=\"1.0\">\r\n" +
                "<dict>\r\n" +
                "    <key>string1</key>\r\n" +
                "    <dict>\r\n" +
                "        <key>NSStringLocalizedFormatKey</key>\r\n" +
                "        <string>%#@value@</string>\r\n" +
                "        <key>value</key>\r\n" +
                "        <dict>\r\n" +
                "            <key>NSStringFormatSpecTypeKey</key>\r\n" +
                "            <string>NSStringPluralRuleType</string>\r\n" +
                "            <key>NSStringFormatValueTypeKey</key>\r\n" +
                "            <string>d</string>\r\n" +
                "            <key>other</key>\r\n" +
                "            <string>" + platformTestString + "</string>\r\n" +
                "            <key>one</key>\r\n" +
                "            <string>one</string>\r\n" +
                "        </dict>\r\n" +
                "    </dict>\r\n" +
                "</dict>\r\n" +
                "</plist>")

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
