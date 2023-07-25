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

class IosSwiftResourcesTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testWriteSwiftFile() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val className = "Strings"
        val tableName = "somTable"
        val sourceDir = tempFolder.newFolder()

        IosSwiftResources(sourceDir, className, tableName, null)
                .write(resMap, null)

        val swiftFile = File(sourceDir, className + IosSwiftResources.SWIFT_FILE_EXTENSION)
        assertTrue(swiftFile.exists())

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\r\n\r\n" +
                "import Foundation\r\n" +
                "\r\n" +
                "public class Strings {\r\n" +
                "\r\n" +
                "    /// value1_2\r\n" +
                "    public static var key3 : String {\r\n" +
                "        get {\r\n" +
                "            return NSLocalizedString(\"key3\", tableName:\"somTable\", bundle:Bundle.main," +
                " value:\"value1_2\", comment: \"\")\r\n" +
                "        }\r\n" +
                "    }\r\n" +
                "\r\n" +
                "}"

        assertEquals(expectedResult, readFile(swiftFile))
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
