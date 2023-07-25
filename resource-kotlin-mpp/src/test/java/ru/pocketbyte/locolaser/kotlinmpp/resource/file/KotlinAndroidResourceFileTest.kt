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

class KotlinAndroidResourceFileTest {

    companion object {
        const val CommonImportsStr =
                "import kotlin.String\n" +
                "import ru.pocketbyte.locolaser.api.provider.AndroidStringProvider\n" +
                "import ru.pocketbyte.locolaser.api.provider.IndexFormattedStringProvider\n"

        const val SecondConstructorsStr =
            "  public constructor(context: Context) : this(AndroidStringProvider(context))\n"
    }

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = KotlinAndroidResourceFile(tempFolder.newFile(),
                "Str", "com.package", null, null)
        assertNull(resourceFile.read(ExtraParams()))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOneItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1", "Comment", Quantity.OTHER)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinAndroidResourceFile(
            testDirectory, className, classPackage,
            null, null
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import android.content.Context\n" +
                CommonImportsStr +
                "\n" +
                "public class $className(\n" +
                "  private val stringProvider: IndexFormattedStringProvider,\n" +
                ") {\n" +
                "  /**\n" +
                "   * value1_1\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() = stringProvider.getString(\"key1\")\n" +
                "\n" +
                SecondConstructorsStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteOnePluralItem() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1", "Comment 1", Quantity.ONE),
            ResValue("value1_2", "Comment 2", Quantity.OTHER)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinAndroidResourceFile(
            testDirectory, className, classPackage,
            null, null
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import android.content.Context\n" +
                "import kotlin.Long\n" +
                CommonImportsStr +
                "\n" +
                "public class $className(\n" +
                "  private val stringProvider: IndexFormattedStringProvider,\n" +
                ") {\n" +
                SecondConstructorsStr +
                "\n" +
                "  /**\n" +
                "   * value1_2\n" +
                "   */\n" +
                "  public fun key1(count: Long): String = stringProvider.getPluralString(\"key1\", count)\n" +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWrite() {
        val resMap = ResMap()

        var resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1", "Comment", Quantity.OTHER)
        )))
        resLocale.put(prepareResItem("key2", arrayOf(
            ResValue("value2_1", "value2_1", Quantity.OTHER)
        )))
        resMap["ru"] = resLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_2", null, Quantity.OTHER)
        )))
        resLocale.put(prepareResItem("key3", arrayOf(
            ResValue("value3_2", "value2_1", Quantity.OTHER)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "StrImpl"
        val classPackage = "com.some.pcg"
        val resourceFile = KotlinAndroidResourceFile(
            testDirectory, className, classPackage,
            null, null
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import android.content.Context\n" +
                CommonImportsStr +
                "\n" +
                "public class $className(\n" +
                "  private val stringProvider: IndexFormattedStringProvider,\n" +
                ") {\n" +
                "  /**\n" +
                "   * value1_2\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() = stringProvider.getString(\"key1\")\n" +
                "\n" +
                "  /**\n" +
                "   * value3_2\n" +
                "   */\n" +
                "  public val key3: String\n" +
                "    get() = stringProvider.getString(\"key3\")\n" +
                "\n" +
                SecondConstructorsStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteWithInterface() {
        val resMap = ResMap()

        var resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_1", "Comment", Quantity.OTHER)
        )))
        resLocale.put(prepareResItem("key2", arrayOf(
            ResValue("value2_1", "value2_1", Quantity.OTHER)
        )))
        resMap["ru"] = resLocale

        resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("value1_2", null, Quantity.OTHER)
        )))
        resLocale.put(prepareResItem("key3", arrayOf(
            ResValue("value3_2", "value2_1", Quantity.OTHER)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "StrImpl"
        val classPackage = "alternative"
        val resourceFile = KotlinAndroidResourceFile(
            testDirectory, className, classPackage,
            "StrInterface", "com.some.pcg"
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import android.content.Context\n" +
                "import com.some.pcg.StrInterface\n" +
                CommonImportsStr +
                "\n" +
                "public class $className(\n" +
                "  private val stringProvider: IndexFormattedStringProvider,\n" +
                ") : StrInterface {\n" +
                "  public override val key1: String\n" +
                "    get() = stringProvider.getString(\"key1\")\n" +
                "\n" +
                "  public override val key3: String\n" +
                "    get() = stringProvider.getString(\"key3\")\n" +
                "\n" +
                SecondConstructorsStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteLongPropertyComment() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue(
            "Wery Wery Wery Wery 1 Wery Wery Wery Wery 2 Wery Wery Wery Wery 3 Wery" +
                    " Wery Wery Wery 4 Wery Wery Wery Wery 5 Wery Long Comment", null,
                Quantity.OTHER))))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Strings"
        val classPackage = "ru.pocketbyte"
        val resourceFile = KotlinAndroidResourceFile(
            testDirectory, className, classPackage,
            null, null
        )

        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import android.content.Context\n" +
                CommonImportsStr +
                "\n" +
                "public class $className(\n" +
                "  private val stringProvider: IndexFormattedStringProvider,\n" +
                ") {\n" +
                "  /**\n" +
                "   * Wery Wery Wery Wery 1 Wery Wery Wery Wery 2 Wery Wery Wery Wery 3 Wery Wery Wery Wery 4 Wery\n" +
                "   * Wery Wery Wery 5 Wery\n" +
                "   * Long Comment\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() = stringProvider.getString(\"key1\")\n" +
                "\n" +
                SecondConstructorsStr +
                "}\n"

        assertEquals(expectedResult, readFile(fileForClass(testDirectory, className, classPackage)))
    }

    @Test
    @Throws(IOException::class)
    fun testWriteFormattedComment() {
        val testValue = "Hello %s %s"
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue(testValue, "", Quantity.OTHER)
        )))
        resLocale.put(prepareResItem("key2", arrayOf(
            ResValue(testValue, "Comment 2", Quantity.OTHER),
            ResValue("value1_1", "Comment 1", Quantity.ONE)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Str"
        val classPackage = "com.pcg"
        val resourceFile = KotlinAndroidResourceFile(
            testDirectory, className, classPackage,
            null, null
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import android.content.Context\n" +
                "import kotlin.Long\n" +
                CommonImportsStr +
                "\n" +
                "public class $className(\n" +
                "  private val stringProvider: IndexFormattedStringProvider,\n" +
                ") {\n" +
                "  /**\n" +
                "   * $testValue\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() = stringProvider.getString(\"key1\")\n" +
                "\n" +
                SecondConstructorsStr +
                "\n" +
                "  /**\n" +
                "   * $testValue\n" +
                "   */\n" +
                "  public fun key2(count: Long): String = stringProvider.getPluralString(\"key2\", count)\n" +
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
