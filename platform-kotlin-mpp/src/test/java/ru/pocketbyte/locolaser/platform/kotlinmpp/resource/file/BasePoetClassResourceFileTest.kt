package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.exception.InvalidValueException
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import java.io.File

class BasePoetClassResourceFileTest {

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    @Test
    fun testFormattedArgumentsHandling() {
        val testDirectory = tempFolder.newFolder()

        class TestClass(formattingType: FormattingType): BasePoetClassResourceFileMock(
            testDirectory, "tmp", "", formattingType
        ) {
            val formattedProperties = mutableSetOf<String>()

            override fun instantiateFormattedPropertySpecBuilder(
                    name: String, formattingArguments: List<FormattingArgument>,
                    item: ResItem, resMap: ResMap, extraParams: ExtraParams?
            ): FunSpec.Builder {
                formattedProperties.add(name)
                return super.instantiateFormattedPropertySpecBuilder(name, formattingArguments, item, resMap, extraParams)
            }

        }

        val resMap = ResMap().apply {
            put(PlatformResources.BASE_LOCALE, ResLocale().apply {
                put(ResItem("key1").apply {
                    addValue(ResValue("val 1", null, formattingArguments = null))
                })
                put(ResItem("key2").apply {
                    addValue(ResValue("val 1", null, formattingArguments = listOf(FormattingArgument())))
                })
            })
        }

        TestClass(JavaFormattingType).let {
            it.write(resMap, null)
            assertEquals(setOf("key2"), it.formattedProperties)
        }

        // NoFormattingType's should be ignored
        TestClass(NoFormattingType).let {
            it.write(resMap, null)
            assertTrue(it.formattedProperties.isEmpty())
        }

    }

    @Test
    fun testFormattedArgumentsSort() {
        val testDirectory = tempFolder.newFolder()

        class TestClass(): BasePoetClassResourceFileMock(
                testDirectory, "tmp", "", JavaFormattingType
        ) {
            var formattingArguments: List<FormattingArgument>? = null

            override fun instantiateFormattedPropertySpecBuilder(
                    name: String, formattingArguments: List<FormattingArgument>,
                    item: ResItem, resMap: ResMap, extraParams: ExtraParams?
            ): FunSpec.Builder {
                if (this.formattingArguments != null)
                    throw RuntimeException("This method should be called only once")
                this.formattingArguments = formattingArguments
                return super.instantiateFormattedPropertySpecBuilder(name, formattingArguments, item, resMap, extraParams)
            }
        }

        val prepareResMap: (value: String) -> ResMap = { value ->
            ResMap().apply {
                put(PlatformResources.BASE_LOCALE, ResLocale().apply {
                    put(ResItem("key").apply {
                        addValue(ResValue(value, null,
                            formattingType = JavaFormattingType,
                            formattingArguments = JavaFormattingType.argumentsFromValue(value)))
                    })
                })
            }
        }

        TestClass().let {
            it.write(prepareResMap("%s %f %d"), null)
            assertEquals(JavaFormattingType.argumentsFromValue("%s %f %d"), it.formattingArguments)
        }
        TestClass().let {
            it.write(prepareResMap("%1\$s %2\$f, %3\$d"), null)
            assertEquals(JavaFormattingType.argumentsFromValue("%1\$s %2\$f %3\$d"), it.formattingArguments)
        }
        TestClass().let {
            it.write(prepareResMap("%2\$s %3\$f %1\$d"), null)
            assertEquals(JavaFormattingType.argumentsFromValue("%1\$d %2\$s %3\$f"), it.formattingArguments)
        }
        TestClass().let {
            it.write(prepareResMap("%1\$s %f, %3\$d"), null)
            assertEquals(JavaFormattingType.argumentsFromValue("%1\$s %3\$d %f"), it.formattingArguments)
        }
    }

    @Test(expected = InvalidValueException::class)
    fun testInvalidPluralFormat() {
        val invalidPluralString = "Count is %s"
        val resMap = ResMap().apply {
            put(PlatformResources.BASE_LOCALE, ResLocale().apply {
                put(ResItem("key").apply {
                    addValue(ResValue(invalidPluralString, null, Quantity.OTHER, JavaFormattingType,
                            JavaFormattingType.argumentsFromValue(invalidPluralString)))
                    addValue(ResValue(invalidPluralString, null, Quantity.ONE, JavaFormattingType,
                            JavaFormattingType.argumentsFromValue(invalidPluralString)))
                })
            })
        }

        BasePoetClassResourceFileMock(
                tempFolder.newFolder(), "tmp", "", JavaFormattingType
        ).write(resMap, null)
    }

    private open class BasePoetClassResourceFileMock(
            directory: File,
            className: String,
            classPackage: String,
            formattingType: FormattingType = NoFormattingType
    ): BasePoetClassResourceFile(
            directory, className, classPackage, formattingType)
    {
        override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
            return TypeSpec.classBuilder(className)
        }
    }
}