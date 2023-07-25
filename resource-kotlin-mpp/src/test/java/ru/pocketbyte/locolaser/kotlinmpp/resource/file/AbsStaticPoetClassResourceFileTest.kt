package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.kotlinmpp.resource.AbsKotlinResources
import ru.pocketbyte.locolaser.kotlinmpp.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.FormattingType.ArgumentsSubstitution
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

class AbsStaticPoetClassResourceFileTest {

    companion object {
        const val getQuantityFunctionStr =
            "protected abstract fun getQuantity(count: Long): Quantity"
        const val formatStringNamedFunctionStr =
            "protected abstract fun formatString(string: String, vararg args: Pair<String, Any>): String"
        const val formatStringIndexedFunctionStr =
            "protected abstract fun formatString(string: String, vararg args: Any): String"
    }

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    @Throws(IOException::class)
    fun testRead() {
        val resourceFile = KotlinAbsStaticResourceFile(tempFolder.newFile(),
            "Str", "com.package", null, null)
        Assert.assertNull(resourceFile.read(ExtraParams()))
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
        val resourceFile = KotlinAbsStaticResourceFile(
            testDirectory, className, classPackage, null, null
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
                "import ru.pocketbyte.locolaser.api.Quantity\n" +
                "\n" +
                "public abstract class $className {\n" +
                "  /**\n" +
                "   * value1_1\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() = \"value1_1\"\n" +
                "\n" +
                getAbstractFunctions(resourceFile.formattingType) +
                "}\n"

        Assert.assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
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
        val resourceFile = KotlinAbsStaticResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
                "import ru.pocketbyte.locolaser.api.Quantity\n" +
                "\n" +
                "public abstract class $className {\n" +
                getAbstractFunctions(resourceFile.formattingType) +
                "\n" +
                "  /**\n" +
                "   * value1_2\n" +
                "   */\n" +
                "  public fun key1(count: Long): String {\n" +
                "    val stringValue = when (getQuantity(count)) {\n" +
                "      Quantity.ONE -> \"value1_1\"\n" +
                "      else -> \"value1_2\"\n" +
                "    }\n" +
                "    return stringValue.let { formatString(it, Pair(\"count\", count)) }\n" +
                "  }\n" +
                "}\n"

        Assert.assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
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
        val resourceFile = KotlinAbsStaticResourceFile(testDirectory, className, classPackage, null, null)
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
                "import ru.pocketbyte.locolaser.api.Quantity\n" +
                "\n" +
                "public abstract class $className {\n" +
                "  /**\n" +
                "   * value1_2\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() = \"value1_2\"\n" +
                "\n" +
                "  /**\n" +
                "   * value3_2\n" +
                "   */\n" +
                "  public val key3: String\n" +
                "    get() = \"value3_2\"\n" +
                "\n" +
                getAbstractFunctions(resourceFile.formattingType) +
                "}\n"

        Assert.assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
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
        val resourceFile = KotlinAbsStaticResourceFile(
            testDirectory, className, classPackage,
            "StrInterface", "com.some.interfacepcg"
        )
        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import com.some.interfacepcg.StrInterface\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
                "import ru.pocketbyte.locolaser.api.Quantity\n" +
                "\n" +
                "public abstract class $className : StrInterface {\n" +
                "  public override val key1: String\n" +
                "    get() = \"value1_2\"\n" +
                "\n" +
                "  public override val key3: String\n" +
                "    get() = \"value3_2\"\n" +
                "\n" +
                getAbstractFunctions(resourceFile.formattingType) +
                "}\n"

        Assert.assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
    }

    @Test
    @Throws(IOException::class)
    fun testWriteLongPropertyComment() {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue("Wery Wery Wery Wery 1 Wery Wery Wery Wery 2 Wery Wery Wery Wery 3 Wery" +
                        " Wery Wery Wery 4 Wery Wery Wery Wery 5 Wery Long Comment", null,
            Quantity.OTHER)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val className = "Strings"
        val classPackage = "ru.pocketbyte"
        val resourceFile = KotlinAbsStaticResourceFile(testDirectory, className, classPackage, null, null)

        resourceFile.write(resMap, null)

        val expectedResult = TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
                "import ru.pocketbyte.locolaser.api.Quantity\n" +
                "\n" +
                "public abstract class $className {\n" +
                "  /**\n" +
                "   * Wery Wery Wery Wery 1 Wery Wery Wery Wery 2 Wery Wery Wery Wery 3 Wery Wery Wery Wery 4 Wery\n" +
                "   * Wery Wery Wery 5 Wery\n" +
                "   * Long Comment\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() =\n" +
                "        \"Wery Wery Wery Wery 1 Wery Wery Wery Wery 2 Wery Wery Wery Wery 3 Wery" +
                         " Wery Wery Wery 4 Wery Wery Wery Wery 5 Wery Long Comment\"\n" +
                "\n" +
                getAbstractFunctions(resourceFile.formattingType) +
                "}\n"

        Assert.assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
    }

    @Test
    @Throws(IOException::class)
    fun testWriteJavaFormattedComment() {
        val classPackage = "com.pcg"
        val className = "Str"
        val testValue = "Hello %d %s"
        testFormattedComment(
            JavaFormattingType,
            testValue, classPackage, className,
            TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.String\n" +
                "import ru.pocketbyte.locolaser.api.Quantity\n" +
                "\n" +
                "public abstract class $className {\n" +
                "  /**\n" +
                "   * $testValue\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() = \"$testValue\"\n" +
                "\n" +
                getAbstractFunctions(JavaFormattingType) +
                "\n" +
                "  /**\n" +
                "   * $testValue\n" +
                "   */\n" +
                "  public fun key1(d1: Long, s2: String): String = \"$testValue\".let { formatString(it, d1, s2) }\n" +
                "\n" +
                "  /**\n" +
                "   * $testValue\n" +
                "   */\n" +
                "  public fun key2(count: Long, s2: String): String {\n" +
                "    val stringValue = when (getQuantity(count)) {\n" +
                "      Quantity.ONE -> \"value1_1\"\n" +
                "      else -> \"$testValue\"\n" +
                "    }\n" +
                "    return stringValue.let { formatString(it, d1, s2) }\n" +
                "  }\n" +
                "}\n"
        )
    }

    @Test
    @Throws(IOException::class)
    fun testWebFormattedComment() {
        val classPackage = "com.pcg"
        val className = "Str"
        val testValue = "Hello {{count}} {{name}}"
        testFormattedComment(
            WebFormattingType,
            testValue, classPackage, className,
            TemplateStr.GENERATED_CLASS_COMMENT + "\n" +
                "package $classPackage\n" +
                "\n" +
                "import kotlin.Any\n" +
                "import kotlin.Long\n" +
                "import kotlin.Pair\n" +
                "import kotlin.String\n" +
                "import ru.pocketbyte.locolaser.api.Quantity\n" +
                "\n" +
                "public abstract class $className {\n" +
                "  /**\n" +
                "   * $testValue\n" +
                "   */\n" +
                "  public val key1: String\n" +
                "    get() = \"$testValue\"\n" +
                "\n" +
                getAbstractFunctions(WebFormattingType) +
                "\n" +
                "  /**\n" +
                "   * $testValue\n" +
                "   */\n" +
                "  public fun key1(count: Long, name: String): String = \"$testValue\".let {\n" +
                "      formatString(it, Pair(\"count\", count), Pair(\"name\", name)) }\n" +
                "\n" +
                "  /**\n" +
                "   * $testValue\n" +
                "   */\n" +
                "  public fun key2(count: Long, name: String): String {\n" +
                "    val stringValue = when (getQuantity(count)) {\n" +
                "      Quantity.ONE -> \"value1_1\"\n" +
                "      else -> \"$testValue\"\n" +
                "    }\n" +
                "    return stringValue.let { formatString(it, Pair(\"count\", count), Pair(\"name\", name)) }\n" +
                "  }\n" +
                "}\n"
        )
    }

    private fun testFormattedComment(
        formattingType: FormattingType,
        testValue: String,
        classPackage: String,
        className: String,
        expectedResult: String
    ) {
        val resMap = ResMap()
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(
            ResValue(testValue, "", Quantity.OTHER, formattingType, formattingType.argumentsFromValue(testValue))
        )))
        resLocale.put(prepareResItem("key2", arrayOf(
            ResValue(testValue, "Comment 2", Quantity.OTHER, formattingType, formattingType.argumentsFromValue(testValue)),
            ResValue("value1_1", "Comment 1", Quantity.ONE)
        )))
        resMap[Resources.BASE_LOCALE] = resLocale

        val testDirectory = tempFolder.newFolder()
        val resourceFile = KotlinAbsStaticResourceFile(testDirectory, className, classPackage, null, null, formattingType)
        resourceFile.write(resMap, null)

        Assert.assertEquals(
            expectedResult,
            readFile(fileForClass(testDirectory, className, classPackage))
        )
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

    private fun getAbstractFunctions(formattingType: FormattingType): String {
        return when (formattingType.argumentsSubstitution) {
            ArgumentsSubstitution.BY_NAME -> {
                "  $getQuantityFunctionStr\n\n" +
                "  $formatStringNamedFunctionStr\n"
            }
            else -> {
                "  $getQuantityFunctionStr\n\n" +
                "  $formatStringIndexedFunctionStr\n"
            }
        }
    }
}