package ru.pocketbyte.locolaser.google.sheet

import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.*
import java.io.File

class GoogleSheetIntegrationTest {

    private val sheetId = "1G-hI9MilctvIm5-5m0z9YgUEIRzS9lIl9d1mXXMJHEg"

    @Ignore
    @Test
    fun testRead() {
        val config = GoogleSheetConfig().apply {
            id = sheetId
            keyColumn = "key"
            quantityColumn = "quantity"
            worksheetTitle = "Strings"
            credentialFile = File("../playground", "service_account.json")
        }
        val result = config.resources.read(setOf("base", "ru"), null)
        Assert.assertNotNull(result)
    }

    @Ignore
    @Test
    fun testWrite() {
        val config = GoogleSheetConfig().apply {
            id = sheetId
            keyColumn = "key"
            quantityColumn = "quantity"
            credentialFile = File("../playground", "service_account.json")
        }

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

        config.resources.write(resMap, null)
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }
}