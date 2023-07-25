package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.resource.Resources
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
import ru.pocketbyte.locolaser.kotlinmpp.resource.AbsKotlinResources
import ru.pocketbyte.locolaser.kotlinmpp.utils.TemplateStr

class KotlinIosResourceFileTest {

    companion object {
        const val CommonImportsStr =
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.String\n" +
                "import platform.Foundation.NSBundle\n" +
                "import platform.Foundation.NSString\n" +
                "import platform.Foundation.localizedStringWithFormat\n" +
                "import platform.Foundation.stringWithFormat\n"

        const val StringProviderStr =
            "    interface StringProvider {\n" +
            "        fun getString(key: String, vararg args: Any): String\n" +
            "\n" +
            "        fun getPluralString(\n" +
            "                key: String,\n" +
            "                count: Long,\n" +
            "                vararg args: Any\n" +
            "        ): String\n" +
            "    }\n" +
            "\n" +
            "    private class StringProviderImpl(private val bundle: NSBundle, private val tableName: String) : StringProvider {\n" +
            "        override fun getString(key: String, vararg args: Any): String {\n" +
            "            val string = this.bundle.localizedStringForKey(key, \"\", this.tableName)\n" +
            "            return when(args.size) {\n" +
            "                0 -> string\n" +
            "                1 -> NSString.stringWithFormat(string, args[0])\n" +
            "                2 -> NSString.stringWithFormat(string, args[0], args[1])\n" +
            "                3 -> NSString.stringWithFormat(string, args[0], args[1], args[2])\n" +
            "                4 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3])\n" +
            "                5 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4])\n" +
            "                6 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5])\n" +
            "                7 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6])\n" +
            "                8 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])\n" +
            "                9 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])\n" +
            "                10 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])\n" +
            "                11 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10])\n" +
            "                12 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11])\n" +
            "                13 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12])\n" +
            "                14 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13])\n" +
            "                15 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14])\n" +
            "                16 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15])\n" +
            "                17 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16])\n" +
            "                18 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17])\n" +
            "                19 -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18])\n" +
            "                else -> NSString.stringWithFormat(string, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18], args[19])\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        override fun getPluralString(\n" +
            "                key: String,\n" +
            "                count: Long,\n" +
            "                vararg args: Any\n" +
            "        ): String {\n" +
            "            val string = this.bundle.localizedStringForKey(key, \"\", this.tableName)\n" +
            "            return when(args.size) {\n" +
            "                0 -> NSString.localizedStringWithFormat(string, count)\n" +
            "                1 -> NSString.localizedStringWithFormat(string, count, args[0])\n" +
            "                2 -> NSString.localizedStringWithFormat(string, count, args[0], args[1])\n" +
            "                3 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2])\n" +
            "                4 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3])\n" +
            "                5 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4])\n" +
            "                6 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5])\n" +
            "                7 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6])\n" +
            "                8 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])\n" +
            "                9 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])\n" +
            "                10 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9])\n" +
            "                11 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10])\n" +
            "                12 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11])\n" +
            "                13 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12])\n" +
            "                14 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13])\n" +
            "                15 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14])\n" +
            "                16 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15])\n" +
            "                17 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16])\n" +
            "                18 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17])\n" +
            "                19 -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18])\n" +
            "                else -> NSString.localizedStringWithFormat(string, count, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15], args[16], args[17], args[18], args[19])\n" +
            "            }\n" +
            "        }\n" +
            "    }\n"
        const val SecondConstructorsStr =
            "    constructor(bundle: NSBundle, tableName: String) : this(StringProviderImpl(bundle, tableName))\n" +
            "\n" +
            "    constructor(bundle: NSBundle) : this(bundle, \"Localizable\")\n" +
            "\n" +
            "    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)\n" +
            "\n" +
            "    constructor() : this(NSBundle.mainBundle(), \"Localizable\")\n"
    }

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = KotlinIosResourceFile(tempFolder.newFile(),
                "Str", "com.package", null, null)
        assertNull(resourceFile.read(ExtraParams()))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOneItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinIosResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                CommonImportsStr +
                "\n" +
                "class $className(private val stringProvider: StringProvider) {\n" +
                "    /**\n" +
                "     * value1_1 */\n" +
                "    val key1: String\n" +
                "        get() = this.stringProvider.getString(\"key1\")\n" +
                "\n" +
                SecondConstructorsStr +
                "\n" +
                StringProviderStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOnePluralItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment 1", Quantity.ONE), ResValue("value1_2", "Comment 2", Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinIosResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                CommonImportsStr +
                "\n" +
                "class $className(private val stringProvider: StringProvider) {\n" +
                SecondConstructorsStr +
                "\n" +
                "    /**\n" +
                "     * value1_2 */\n" +
                "    fun key1(count: Long): String = this.stringProvider.getPluralString(\"key1\", count)\n" +
                "\n" +
                StringProviderStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
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

        val testDirectory = tempFolder.newFolder()
        val className = "StrImpl"
        val classPackage = "com.some.pcg"
        val resourceFile = KotlinIosResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                CommonImportsStr +
                "\n" +
                "class $className(private val stringProvider: StringProvider) {\n" +
                "    /**\n" +
                "     * value1_2 */\n" +
                "    val key1: String\n" +
                "        get() = this.stringProvider.getString(\"key1\")\n" +
                "\n" +
                "    /**\n" +
                "     * value3_2 */\n" +
                "    val key3: String\n" +
                "        get() = this.stringProvider.getString(\"key3\")\n" +
                "\n" +
                SecondConstructorsStr +
                "\n" +
                StringProviderStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteWithInterface() {
        val resMap = ResMap()

        var resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", "Comment", Quantity.OTHER))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value2_1", "value2_1", Quantity.OTHER))))
        resMap["ru"] = resLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value3_2", "value2_1", Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "StrImpl"
        val classPackage = "com.some.pcg"
        val resourceFile = KotlinIosResourceFile(testDirectory, className, classPackage,
                "StrInterface", "com.interface")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import com.interface.StrInterface\n" +
                CommonImportsStr +
                "\n" +
                "class $className(private val stringProvider: StringProvider) : StrInterface {\n" +
                "    override val key1: String\n" +
                "        get() = this.stringProvider.getString(\"key1\")\n" +
                "\n" +
                "    override val key3: String\n" +
                "        get() = this.stringProvider.getString(\"key3\")\n" +
                "\n" +
                SecondConstructorsStr +
                "\n" +
                StringProviderStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
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

        val testDirectory = tempFolder.newFolder()
        val className = "Strings"
        val classPackage = "ru.pocketbyte"
        val resourceFile = KotlinIosResourceFile(testDirectory, className, classPackage, null, null)

        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                CommonImportsStr +
                "\n" +
                "class $className(private val stringProvider: StringProvider) {\n" +
                "    /**\n" +
                "     * Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery" +
                " Wery Wery Wery Wery Wery Wery\n" +
                "     * Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Wery Long Comment */\n" +
                "    val key1: String\n" +
                "        get() = this.stringProvider.getString(\"key1\")\n" +
                "\n" +
                SecondConstructorsStr +
                "\n" +
                StringProviderStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteFormattedComment() {
        val testValue = "Hello %s %s"
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue(testValue, "", Quantity.OTHER))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue(testValue, "Comment 2", Quantity.OTHER), ResValue("value1_1", "Comment 1", Quantity.ONE))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinIosResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                CommonImportsStr +
                "\n" +
                "class $className(private val stringProvider: StringProvider) {\n" +
                "    /**\n" +
                "     * $testValue */\n" +
                "    val key1: String\n" +
                "        get() = this.stringProvider.getString(\"key1\")\n" +
                "\n" +
                SecondConstructorsStr +
                "\n" +
                "    /**\n" +
                "     * $testValue */\n" +
                "    fun key2(count: Long): String = this.stringProvider.getPluralString(\"key2\", count)\n" +
                "\n" +
                StringProviderStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
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

    private fun fileForClass(directory: File, className: String, classPackage: String): File {
        return File(
            File(directory, classPackage.replace(".", "/")),
            "$className${AbsKotlinResources.KOTLIN_FILE_EXTENSION}"
        )
    }
}
