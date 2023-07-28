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

import org.junit.Assert.assertNull
import org.junit.Assert.assertEquals
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.kotlinmpp.resource.AbsKotlinResources
import ru.pocketbyte.locolaser.kotlinmpp.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType

class KotlinCommonResourceFileTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = KotlinCommonResourceFile(tempFolder.newFolder(), "Str", "com.package")
        assertNull(resourceFile.read(ExtraParams()))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOneItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(resItem("key1", resValue("value1_1", "Comment", Quantity.OTHER)))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinCommonResourceFile(testDirectory, className, classPackage)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
            "package $classPackage\n" +
            "\n" +
            "import kotlin.String\n" +
            "\n" +
            "public interface $className {\n" +
            "  /**\n" +
            "   * value1_1\n" +
            "   */\n" +
            "  public val key1: String\n" +
            "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOnePluralItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(resItem("key1",
            resValue("value1_1", "Comment 1", Quantity.ONE),
            resValue("value1_2", "Comment 2", Quantity.OTHER)
        ))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinCommonResourceFile(testDirectory, className, classPackage)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
            "package $classPackage\n" +
            "\n" +
            "import kotlin.Long\n" +
            "import kotlin.String\n" +
            "\n" +
            "public interface $className {\n" +
            "  /**\n" +
            "   * value1_2\n" +
            "   */\n" +
            "  public fun key1(count: Long): String\n" +
            "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWrite() {
        val resMap = ResMap()

        var resLocale = ResLocale()
        resLocale.put(resItem("key1", resValue("value1_1", "Comment", Quantity.OTHER)))
        resLocale.put(resItem("key2", resValue("value2_1", "value2_1", Quantity.OTHER)))
        resMap["ru"] = resLocale

        resLocale = ResLocale()
        resLocale.put(resItem("key1", resValue("value1_2", null, Quantity.OTHER)))
        resLocale.put(resItem("key3", resValue("value3_2", "value2_1", Quantity.OTHER)))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "StrInterface"
        val classPackage = "com.some.pcg"
        val resourceFile = KotlinCommonResourceFile(testDirectory, className, classPackage)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
            "package $classPackage\n" +
            "\n" +
            "import kotlin.String\n" +
            "\n" +
            "public interface $className {\n" +
            "  /**\n" +
            "   * value1_2\n" +
            "   */\n" +
            "  public val key1: String\n" +
            "\n" +
            "  /**\n" +
            "   * value3_2\n" +
            "   */\n" +
            "  public val key3: String\n" +
            "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteLongPropertyComment() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(resItem("key1", resValue(
            "Wery Wery Wery Wery 1 Wery Wery Wery Wery 2 Wery Wery Wery Wery 3 Wery" +
                    " Wery Wery Wery 4 Wery Wery Wery Wery 5 Wery Long Comment", null,
                Quantity.OTHER)))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Strings"
        val classPackage = "ru.pocketbyte"
        val resourceFile = KotlinCommonResourceFile(testDirectory, className, classPackage)

        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
            "package $classPackage\n" +
            "\n" +
            "import kotlin.String\n" +
            "\n" +
            "public interface $className {\n" +
            "  /**\n" +
            "   * Wery Wery Wery Wery 1 Wery Wery Wery Wery 2 Wery Wery Wery Wery 3 Wery Wery Wery Wery 4 Wery\n" +
            "   * Wery Wery Wery 5 Wery\n" +
            "   * Long Comment\n" +
            "   */\n" +
            "  public val key1: String\n" +
            "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteFormattedComment() {
        val testValue1 = "Hello %s %s"
        val testValue2 = "Count is %d"
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(resItem("key1",
                resValue(testValue1, "", Quantity.OTHER)))
        resLocale.put(resItem("key2",
                resValue(testValue2, "Comment 2", Quantity.OTHER),
                resValue("value1_1", "Comment 1", Quantity.ONE)))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinCommonResourceFile(testDirectory, className, classPackage)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
            "package $classPackage\n" +
            "\n" +
            "import kotlin.Long\n" +
            "import kotlin.String\n" +
            "\n" +
            "public interface $className {\n" +
            "  /**\n" +
            "   * $testValue1\n" +
            "   */\n" +
            "  public val key1: String\n" +
            "\n" +
            "  /**\n" +
            "   * $testValue1\n" +
            "   */\n" +
            "  public fun key1(s1: String, s2: String): String\n" +
            "\n" +
            "  /**\n" +
            "   * $testValue2\n" +
            "   */\n" +
            "  public fun key2(count: Long): String\n" +
            "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Throws(IOException::class)
    private fun readFile(file: File): String {
        return String(Files.readAllBytes(Paths.get(file.absolutePath)), Charset.defaultCharset())
    }

    private fun resItem(key: String, vararg values: ResValue): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }

    private fun resValue(
        value: String,
        comment: String?,
        quantity: Quantity = Quantity.OTHER,
        formattingType: FormattingType = JavaFormattingType,
        meta: Map<String, String>? = null
    ): ResValue {
        return ResValue(value, comment, quantity, formattingType, formattingType.argumentsFromValue(value), meta)
    }

    private fun fileForClass(directory: File, className: String, classPackage: String): File {
        return File(
            File(directory, classPackage.replace(".", "/")),
            "$className.${AbsKotlinResources.KOTLIN_FILE_EXTENSION}"
        )
    }
}
