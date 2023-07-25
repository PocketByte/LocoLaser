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
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType

class KotlinJsResourceFileTest {

    companion object {
        const val StringProviderStr =
            "    interface StringProvider {\n" +
            "        fun getString(key: String, vararg args: Pair<String, Any>): String\n" +
            "\n" +
            "        fun getPluralString(\n" +
            "                key: String,\n" +
            "                count: Long,\n" +
            "                vararg args: Pair<String, Any>\n" +
            "        ): String\n" +
            "    }\n" +
            "\n" +
            "    private class StringProviderImpl(private val i18n: I18n) : StringProvider {\n" +
            "        override fun getString(key: String, vararg args: Pair<String, Any>): String = this.i18n.t(key, dynamic(*args))\n" +
            "\n" +
            "        override fun getPluralString(\n" +
            "                key: String,\n" +
            "                count: Long,\n" +
            "                vararg args: Pair<String, Any>\n" +
            "        ): String = this.i18n.t(\"\${key}_plural\", dynamic(Pair(\"count\", count), *args))\n" +
            "\n" +
            "        private fun dynamic(vararg args: Pair<String, Any>): Any {\n" +
            "            val d: dynamic = object{}\n" +
            "            args.forEach {\n" +
            "                d[it.first] = it.second\n" +
            "            }\n" +
            "            return d as Any\n" +
            "        }\n" +
            "    }\n"
        const val SecondConstructorsStr =
            "    constructor(i18n: I18n) : this(StringProviderImpl(i18n))\n"
    }

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = KotlinJsResourceFile(tempFolder.newFile(),
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
        val resourceFile = KotlinJsResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import i18next.I18n\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
                "\n" +
                "class Str(private val stringProvider: StringProvider) {\n" +
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
        val resourceFile = KotlinJsResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import i18next.I18n\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
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
    fun testWritePluralWithMiltipleArguments() {
        val resMap = ResMap().apply {
            put(Resources.BASE_LOCALE, ResLocale().apply {
                put(prepareResItem("key1", arrayOf(
                    ResValue("Count %d %s", null, Quantity.ONE,
                            JavaFormattingType, JavaFormattingType.argumentsFromValue("%d %s")),
                    ResValue("Count %d %s", null, Quantity.OTHER,
                            JavaFormattingType, JavaFormattingType.argumentsFromValue("%d %s"))))
                )
            })
        }

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinJsResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import i18next.I18n\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
                "\n" +
                "class $className(private val stringProvider: StringProvider) {\n" +
                SecondConstructorsStr +
                "\n" +
                "    /**\n" +
                "     * Count %d %s */\n" +
                "    fun key1(count: Long, s2: String): String = this.stringProvider.getPluralString(\"key1\", count, Pair(\"s2\", s2))\n" +
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
        val resourceFile = KotlinJsResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import i18next.I18n\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
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
        val resourceFile = KotlinJsResourceFile(testDirectory, className, classPackage, "StrInterface", "com.interface")
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import com.interface.StrInterface\n" +
                "import i18next.I18n\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
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
        val resourceFile = KotlinJsResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import i18next.I18n\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
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
        val resourceFile = KotlinJsResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import i18next.I18n\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
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
