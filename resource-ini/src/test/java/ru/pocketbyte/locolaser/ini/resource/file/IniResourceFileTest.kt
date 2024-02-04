package ru.pocketbyte.locolaser.ini.resource.file

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.duplicateComments
import ru.pocketbyte.locolaser.resource.entity.*
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class IniResourceFileTest {

    @JvmField @Rule
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testReadNotExistsFile() {
        val testFile = tempFolder.newFile()
        if (testFile.exists())
            assertTrue(testFile.delete())

        val resourceFile = IniResourceFile(testFile, localesSet("en"))

        assertNull(resourceFile.read(null))
    }

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                "[ru]\r\n" +
                        "# Comment\r\n" +
                        "string1 = Value1\r\n" +
                        "string2 = Value2\r\n" +
                        "\r\n" +
                        "string3 = Value 3")

        val resourceFile = IniResourceFile(testFile, localesSet(testLocale))
        val resMap = resourceFile.read(null)

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
    fun testReadMultiLanguages() {
        val testLocale1 = "ru"
        val testLocale2 = "en"
        val testFile = prepareTestFile(
                ("[" + testLocale2 + "]\r\n" +
                        "# Comment\r\n" +
                        "string1 = Value1 en\r\n" +
                        "string2 = Value2 en\r\n" +
                        "string3 = Value 3 en\r\n" +
                        "[" + testLocale1 + "]\r\n" +
                        "# Comment ru\r\n" +
                        "string1 = Value1 ru\r\n" +
                        "string3 = Value 3 ru"))

        val resourceFile = IniResourceFile(testFile, localesSet(testLocale1, testLocale2))
        val resMap = resourceFile.read(null)

        assertNotNull(resMap)

        val expectedMap = ResMap()
        var resLocale:ResLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("Value1 ru", "Comment ru", Quantity.OTHER))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue("Value 3 ru", null, Quantity.OTHER))))
        expectedMap[testLocale1] = resLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("string1", arrayOf(ResValue("Value1 en", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("string2", arrayOf(ResValue("Value2 en", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("string3", arrayOf(ResValue("Value 3 en", null, Quantity.OTHER))))
        expectedMap[testLocale2] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testReadWhitespaces() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                (" [ru] \r\n" +
                        "#   Comment    \r\n" +
                        "   string1    =   Value1  "))

        val resourceFile = IniResourceFile(testFile, localesSet(testLocale))
        val resMap = resourceFile.read(null)

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("   string1   ", arrayOf(ResValue("  Value1  ", "Comment", Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testReadOutOfSection() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                ("out_of_section_string = Out of Section\r\n" +
                        "[ru]\r\n" +
                        "ru_section_string = Ru Section"))

        val resourceFile = IniResourceFile(testFile, localesSet(testLocale))
        val resMap = resourceFile.read(null)

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("ru_section_string", arrayOf(ResValue("Ru Section", null, Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testReadPlural() {
        val testLocale = "en"
        val testFile = prepareTestFile(
                ("[en]\r\n" +
                        "unreadMessages = {[ plural(n) ]}\r\n" +
                        "unreadMessages[zero]  = You have no unread messages\r\n" +
                        "unreadMessages[one]   = You have one unread message\r\n" +
                        "unreadMessages[other] = You have {{n}} unread messages"))

        val resourceFile = IniResourceFile(testFile, localesSet(testLocale))
        val resMap = resourceFile.read(null)

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("unreadMessages", arrayOf(ResValue("You have no unread messages", null, Quantity.ZERO), ResValue("You have one unread message", null, Quantity.ONE), ResValue("You have {{n}} unread messages", null, Quantity.OTHER))))
        expectedMap[testLocale] = resLocale

        assertEquals(expectedMap, resMap)
    }

    @Test
    @Throws(IOException::class)
    fun testReadComments() {
        val testLocale = "ru"
        val testFile = prepareTestFile(
                (IniResourceFile.GENERATED_COMMENT + "\r\n\r\n" +
                        "[ru]\r\n" +
                        "# This comment must be ignored\r\n" +
                        "\r\n" +
                        "no_comment = No comment\r\n" +
                        "# This comment must be not ignored\r\n" +
                        "has_comment = Has comment\r\n" +
                        "# Comment line 1\r\n" +
                        "# Comment line 2\r\n" +
                        "multiline_comment = Multiline comment\r\n" +
                        "# Ignored Comment line 1\r\n" +
                        "\r\n" +
                        "# Comment line 2\r\n" +
                        "multiline_ignore_comment = Multiline comment (ignore)\r\n" +
                        "# Useless Plural comment\r\n" +
                        "plural_comment = {[ plural(n) ]}\r\n" +
                        "# Zero comment\r\n" +
                        "plural_comment[zero]  = Zero Plural\r\n" +
                        "# One comment line 1\r\n" +
                        "# One comment line 2\r\n" +
                        "plural_comment[one]   = One Plural\r\n" +
                        "plural_comment[other] = {{n}} Plural (no comment)"))

        val resourceFile = IniResourceFile(testFile, localesSet(testLocale))
        val resMap = resourceFile.read(null)

        assertNotNull(resMap)

        val expectedMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("no_comment", arrayOf(ResValue("No comment", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("has_comment", arrayOf(ResValue("Has comment", "This comment must be not ignored", Quantity.OTHER))))
        resLocale.put(prepareResItem("multiline_comment", arrayOf(ResValue("Multiline comment", "Comment line 1\nComment line 2", Quantity.OTHER))))
        resLocale.put(prepareResItem("multiline_ignore_comment", arrayOf(ResValue("Multiline comment (ignore)", "Comment line 2", Quantity.OTHER))))
        resLocale.put(prepareResItem("plural_comment", arrayOf(ResValue("Zero Plural", "Zero comment", Quantity.ZERO), ResValue("One Plural", "One comment line 1\nOne comment line 2", Quantity.ONE), ResValue("{{n}} Plural (no comment)", null, Quantity.OTHER))))
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
        val resourceFile = IniResourceFile(testFile, localesSet(testLocale))
        resourceFile.write(resMap, null)

        val expectedResult = (
            IniResourceFile.GENERATED_COMMENT + "\r\n\r\n" +
                "[ru]\r\n" +
                "# Comment\r\n" +
                "key1 = value1_1\r\n" +
                "# value2_1\r\n" +
                "key2 = value2_1\r\n" +
                "key4 = value4_1\r\n"
        )

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

        val extraParams = ExtraParams()
        extraParams.duplicateComments = false

        val testFile = tempFolder.newFile()
        val resourceFile = IniResourceFile(testFile, localesSet(testLocale))
        resourceFile.write(resMap, extraParams)

        val expectedResult = (IniResourceFile.GENERATED_COMMENT + "\r\n\r\n" +
                "[ru]\r\n" +
                "# Comment\r\n" +
                "key1 = value1_1\r\n" +
                "key2 = value2_1\r\n")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Test
    @Throws(IOException::class)
    fun testWritePlural() {
        val resMap = ResMap()
        var resLocale:ResLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("unreadMessages", arrayOf(ResValue("You have no unread messages", "Comment", Quantity.ZERO), ResValue("You have one unread message", null, Quantity.ONE), ResValue("You have {{n}} unread messages", null, Quantity.OTHER))))
        resMap["en"] = resLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("unreadMessages", arrayOf(ResValue("You have no unread messages (ru)", null, Quantity.ZERO), ResValue("You have one unread message (rus)", "Comment rus", Quantity.ONE), ResValue("You have {{n}} unread messages (RUS)", null, Quantity.OTHER))))
        resMap["ru"] = resLocale

        val testFile = tempFolder.newFile()
        val resourceFile = IniResourceFile(testFile, localesSet("en", "ru"))
        resourceFile.write(resMap, null)

        val expectedResult = (IniResourceFile.GENERATED_COMMENT + "\r\n\r\n" +
                "[en]\r\n" +
                "unreadMessages = {[ plural(n) ]}\r\n" +
                "# Comment\r\n" +
                "unreadMessages[zero] = You have no unread messages\r\n" +
                "unreadMessages[one] = You have one unread message\r\n" +
                "unreadMessages[other] = You have {{n}} unread messages\r\n" +
                "[ru]\r\n" +
                "unreadMessages = {[ plural(n) ]}\r\n" +
                "unreadMessages[zero] = You have no unread messages (ru)\r\n" +
                "# Comment rus\r\n" +
                "unreadMessages[one] = You have one unread message (rus)\r\n" +
                "unreadMessages[other] = You have {{n}} unread messages (RUS)\r\n")

        assertEquals(expectedResult, readFile(testFile))
    }

    @Throws(IOException::class)
    private fun prepareTestFile(text:String):File {
        val file = tempFolder.newFile()
        val writer = PrintWriter(file)
        writer.write(text)
        writer.flush()
        writer.close()
        return file
    }

    @Throws(IOException::class)
    private fun readFile(file:File):String {
        return String(Files.readAllBytes(Paths.get(file.absolutePath)), Charset.defaultCharset())
    }

    private fun prepareResItem(key:String, values:Array<ResValue>):ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }

    private fun localesSet(vararg locales:String):Set<String> {
        val set = LinkedHashSet<String>(locales.size)
        set.addAll(Arrays.asList(*locales))
        return set
    }
}
