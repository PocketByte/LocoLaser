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
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.Resources.Companion.BASE_LOCALE

/**
 * @author Denis Shurygin
 */
class IosObjectiveCMResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = IosObjectiveCMResourceFile(tempFolder.newFile(), "Strings",
                "Strings")
        assertNull(resourceFile.read(ExtraParams()))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOneItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resMap[BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosObjectiveCMResourceFile(testFile, "Strings", "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Strings.h>\r\n" +
                "\r\n" +
                "@implementation Strings\r\n" +
                "\r\n" +
                "+(NSString*)key1 {\r\n" +
                "    return NSLocalizedStringFromTable(@\"key1\", @\"Strings\", @\"Comment\")\r\n" +
                "}\r\n" +
                "\r\n" +
                "@end"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWrite() {
        val resMap = ResMap()

        resMap["ru"] = ResLocale().apply {
            put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
            put(prepareResItem("key2", arrayOf(ResValue("value2_1", "value2_1", Quantity.OTHER))))
        }

        resMap[BASE_LOCALE] = ResLocale().apply {
            put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
            put(prepareResItem("key3", arrayOf(ResValue("value3_2", "value2_1", Quantity.OTHER))))
        }

        val testFile = tempFolder.newFile()
        val resourceFile = IosObjectiveCMResourceFile(testFile, "Strings", "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Strings.h>\r\n" +
                "\r\n" +
                "@implementation Strings\r\n" +
                "\r\n" +
                "+(NSString*)key1 {\r\n" +
                "    return NSLocalizedStringFromTable(@\"key1\", @\"Strings\", @\"\")\r\n" +
                "}\r\n" +
                "\r\n" +
                "+(NSString*)key3 {\r\n" +
                "    return NSLocalizedStringFromTable(@\"key3\", @\"Strings\", @\"value2_1\")\r\n" +
                "}\r\n" +
                "\r\n" +
                "@end"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteSpecialCharacterInComment() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue(
                "Some Value",
                "\\{\"ssdsd\": 333} ; ';k.,/?@'' \n {\"ssdsd\": 333} ; ';k.,/?@''",
                Quantity.OTHER))))
        resMap[BASE_LOCALE] = resLocale


        val testFile = tempFolder.newFile()
        val resourceFile = IosObjectiveCMResourceFile(testFile, "Strings", "Strings")
        resourceFile.write(resMap, null)


        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Strings.h>\r\n" +
                "\r\n" +
                "@implementation Strings\r\n" +
                "\r\n" +
                "+(NSString*)key1 {\r\n" +
                "    return NSLocalizedStringFromTable(@\"key1\", @\"Strings\", @\"\\\\{\\\"ssdsd\\\": 333} ; ';k.,/?@'' \\n {\\\"ssdsd\\\": 333} ; ';k.,/?@''\")\r\n" +
                "}\r\n" +
                "\r\n" +
                "@end"

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
