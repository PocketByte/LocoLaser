/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource.file

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.*
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Denis Shurygin
 */
class IosSwiftResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = IosSwiftResourceFile(tempFolder.newFile(), "Strings", "Strings")
        assertNull(resourceFile.read(ExtraParams()))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOneItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosSwiftResourceFile(testFile, "Strings", "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// value1_1\r\n" +
                "    public static var key1 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                " value:\"value1_1\", comment: \"Comment\")\r\n" +
                "        }\r\n" +
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
        resMap[Resources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosSwiftResourceFile(testFile, "Strings", "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// value1_2\r\n" +
                "    public static var key1 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                " value:\"value1_2\", comment: \"\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
                "\r\n" +
                "    /// value3_2\r\n" +
                "    public static var key3 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key3\", tableName:\"Strings\", bundle:Bundle.main," +
                " value:\"value3_2\", comment: \"value2_1\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
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
        resMap[Resources.BASE_LOCALE] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IosSwiftResourceFile(testFile, "Strings", "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery " +
                "Wery Wery Wery Wery Wery Wery\r\n" +
                "    /// Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long " +
                "Comment\r\n" +
                "    public static var key1 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                " value:\"Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                " Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                " Wery Wery Wery Long Comment\", comment: \"\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
                "\r\n" +
                "}"

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
        resMap[Resources.BASE_LOCALE] = resLocale


        val testFile = tempFolder.newFile()
        val resourceFile = IosSwiftResourceFile(testFile, "Strings", "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// \\{\"ssdsd\": 333} ; ';k.,/?@'' \\n {\"ssdsd\": 333} ; ';k.,/?@''\r\n" +
                "    public static var key1 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                " value:\"\\\\{\\\"ssdsd\\\": 333} ; ';k.,/?@'' \\n {\\\"ssdsd\\\": 333} ; ';k.,/?@''\", comment: \"Some Comment\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
                "\r\n" +
                "}"

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
        resMap[Resources.BASE_LOCALE] = resLocale


        val testFile = tempFolder.newFile()
        val resourceFile = IosSwiftResourceFile(testFile, "Strings", "Strings")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// Some Value\r\n" +
                "    public static var key1 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key1\", tableName:\"Strings\", bundle:Bundle.main," +
                " value:\"Some Value\", comment: \"\\\\{\\\"ssdsd\\\": 333} ; ';k.,/?@'' \\n {\\\"ssdsd\\\": 333} ; ';k.,/?@''\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
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
