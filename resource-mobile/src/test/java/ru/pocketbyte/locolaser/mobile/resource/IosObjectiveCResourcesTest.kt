package ru.pocketbyte.locolaser.mobile.resource

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.*

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import ru.pocketbyte.locolaser.entity.Quantity

class IosObjectiveCResourcesTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testWriteObjcFile() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val className = "Strings"
        val tableName = "somAnotherTable"
        val sourceDir = tempFolder.newFolder()

        IosObjectiveCResources(sourceDir, className, tableName, null)
                .write(resMap, null)

        val objcHFile = File(sourceDir, className + IosObjectiveCResources.OBJC_H_FILE_EXTENSION)
        val objcMFile = File(sourceDir, className + IosObjectiveCResources.OBJC_M_FILE_EXTENSION)
        assertTrue(objcHFile.exists())
        assertTrue(objcMFile.exists())

        val expectedHResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Foundation/Foundation.h>\r\n" +
                "\r\n" +
                "@interface Strings : NSObject\r\n" +
                "\r\n" +
                "/// value1_2\r\n" +
                "@property (class, readonly) NSString* key3;\r\n" +
                "\r\n" +
                "@end"

        assertEquals(expectedHResult, readFile(objcHFile))

        val expectedMResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "#import <Strings.h>\r\n" +
                "\r\n" +
                "@implementation Strings\r\n" +
                "\r\n" +
                "+(NSString*)key3 {\r\n" +
                "    return NSLocalizedStringFromTable(@\"key3\", @\"somAnotherTable\", @\"\")\r\n" +
                "}\r\n" +
                "\r\n" +
                "@end"

        assertEquals(expectedMResult, readFile(objcMFile))
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }

    @Throws(IOException::class)
    private fun readFile(file: File): String {
        return String(Files.readAllBytes(Paths.get(file.absolutePath)), Charset.defaultCharset())
    }
}
