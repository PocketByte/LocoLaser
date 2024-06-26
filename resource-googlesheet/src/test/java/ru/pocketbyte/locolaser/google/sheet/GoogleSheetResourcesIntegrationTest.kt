package ru.pocketbyte.locolaser.google.sheet

import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

//@Ignore
class GoogleSheetResourcesIntegrationTest {

    private val sheetId = "1G-hI9MilctvIm5-5m0z9YgUEIRzS9lIl9d1mXXMJHEg"

    @Test
    fun testRead() {
        val workDir = File("./")
        val sourceConfig = GoogleSheetResourcesConfig(
            workDir = workDir,
            id = sheetId,
            keyColumn = "key",
            quantityColumn = "quantity",
            worksheetTitle = "Strings",
            credentialFile = "../playground/service_account.json",
            formattingType = JavaFormattingType
        )
        val config = Config(
            workDir = workDir,
            source = sourceConfig
        )

        val result = sourceConfig.resources.read(setOf("base", "ru"), null)
        Assert.assertNotNull(result)
    }

    @Test
    fun testWrite() {
        val workDir = File("./")
        val sourceConfig = GoogleSheetResourcesConfig(
            workDir = workDir,
            id = sheetId,
            keyColumn = "key",
            quantityColumn = "quantity",
            credentialFile = "../playground/service_account.json",
            formattingType = JavaFormattingType
        )

        val config = Config(
            workDir = workDir,
            source = sourceConfig
        )

        val resMap = ResMap().apply {
            put("base",
                ResLocale().apply {
                    put(prepareResItem("app_name",
                        arrayOf(ResValue("LocoLaser example", null, Quantity.OTHER))
                    ))

                    put(prepareResItem("screen_main_app_description",
                        arrayOf(ResValue("This is example application of how to use the LocoLser.", null, Quantity.OTHER))
                    ))

                    put(prepareResItem("screen_main_plural_example_title",
                        arrayOf(ResValue("Plural string examples", null, Quantity.OTHER))
                    ))

                    put(prepareResItem("screen_main_plural_string",
                        arrayOf(
                            ResValue("Other: %d values", null, Quantity.OTHER),
                            ResValue("One: 1 value", null, Quantity.ONE),
                            ResValue("Zero: no values", null, Quantity.ZERO)
                        )
                    ))
                }
            )
            put("ru",
                ResLocale().apply {
                    put(prepareResItem("app_name",
                        arrayOf(ResValue("LocoLaser пример", null, Quantity.OTHER))
                    ))

                    put(prepareResItem("screen_main_app_description",
                        arrayOf(ResValue("Это пример приложения по использованию LocoLaser.", null, Quantity.OTHER))
                    ))

                    put(prepareResItem("screen_main_plural_example_title",
                        arrayOf(ResValue("Примеры Plural строк", null, Quantity.OTHER))
                    ))

                    put(prepareResItem("screen_main_plural_string",
                        arrayOf(
                            ResValue("Other: %d значений", null, Quantity.OTHER),
                            ResValue("One: %d значение", null, Quantity.ONE),
                            ResValue("Zero: нет значений", null, Quantity.ZERO),
                            ResValue("Two: %d значения", null, Quantity.TWO),
                            ResValue("Few: %d значений", null, Quantity.FEW)
                        )
                    ))
                }
            )
        }

        sourceConfig.resources.write(resMap, null)
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }
}