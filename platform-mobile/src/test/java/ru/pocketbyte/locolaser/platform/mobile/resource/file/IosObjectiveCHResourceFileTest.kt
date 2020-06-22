/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.*

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import ru.pocketbyte.locolaser.config.ExtraParams

/**
 * @author Denis Shurygin
 */
class IosObjectiveCHResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = IosObjectiveCHResourceFile(tempFolder.newFile(), "Strings")
        assertNull(resourceFile.read(ExtraParams()))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOneItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resMap[PlatformResources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosObjectiveCHResourceFile(testFile, "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Foundation/Foundation.h>\r\n" +
                "\r\n" +
                "@interface Strings : NSObject\r\n" +
                "\r\n" +
                "/// value1_1\r\n" +
                "@property (class, readonly) NSString* key1;\r\n" +
                "\r\n" +
                "@end"

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
        val resourceFile = IosObjectiveCHResourceFile(testFile, "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Foundation/Foundation.h>\r\n" +
                "\r\n" +
                "@interface Strings : NSObject\r\n" +
                "\r\n" +
                "/// value1_2\r\n" +
                "@property (class, readonly) NSString* key1;\r\n" +
                "\r\n" +
                "/// value3_2\r\n" +
                "@property (class, readonly) NSString* key3;\r\n" +
                "\r\n" +
                "@end"

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
        val resourceFile = IosObjectiveCHResourceFile(testFile, "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Foundation/Foundation.h>\r\n" +
                "\r\n" +
                "@interface Strings : NSObject\r\n" +
                "\r\n" +
                "/// Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                " Wery Wery Wery Wery Wery Wery\r\n" +
                "/// Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long Comment\r\n" +
                "@property (class, readonly) NSString* key1;\r\n" +
                "\r\n" +
                "@end"

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteSpecialCharacterInValue() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue(
                "\\{\"ssdsd\": 333} ; ';k.,/?@'' \n {\"ssdsd\": 333} ; ';k.,/?@''",
                "Some Comment",
                Quantity.OTHER))))
        resMap[PlatformResources.BASE_LOCALE] = resLocale


        val testFile = tempFolder.newFile()
        val resourceFile = IosObjectiveCHResourceFile(testFile, "Strings")
        resourceFile.write(resMap, null)


        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Foundation/Foundation.h>\r\n" +
                "\r\n" +
                "@interface Strings : NSObject\r\n" +
                "\r\n" +
                "/// \\{\"ssdsd\": 333} ; ';k.,/?@'' \\n {\"ssdsd\": 333} ; ';k.,/?@''\r\n" +
                "@property (class, readonly) NSString* key1;\r\n" +
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
