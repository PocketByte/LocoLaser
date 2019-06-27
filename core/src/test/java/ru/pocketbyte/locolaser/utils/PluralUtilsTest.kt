package ru.pocketbyte.locolaser.utils

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.pocketbyte.locolaser.resource.entity.Quantity
import java.util.*

class PluralUtilsTest {

    private var quantityPairs: List<Pair<String, Quantity>>? = null

    @Before
    fun init() {
        quantityPairs = Arrays.asList(
                Pair("zero", Quantity.ZERO),
                Pair("one", Quantity.ONE),
                Pair("two", Quantity.TWO),
                Pair("few", Quantity.FEW),
                Pair("many", Quantity.MANY),
                Pair("other", Quantity.OTHER)
        )
    }

    @Test
    @Throws(Exception::class)
    fun testFromString() {
        for ((first, second) in quantityPairs!!) {
            assertEquals(second, PluralUtils.quantityFromString(first))
        }
    }


    @Test
    @Throws(Exception::class)
    fun testFromIndex() {
        assertEquals(Quantity.ONE, PluralUtils.quantityFromIndex(0, "en"))
        assertEquals(Quantity.OTHER, PluralUtils.quantityFromIndex(1, "en"))
        assertEquals(Quantity.ONE, PluralUtils.quantityFromIndex(0, "ru"))
        assertEquals(Quantity.MANY, PluralUtils.quantityFromIndex(2, "ru"))
    }

    @Test
    fun testListForLocale() {
        assertEquals(
                listOf(Quantity.ONE, Quantity.FEW, Quantity.MANY, Quantity.OTHER),
                PluralUtils.quantitiesForLocale("ru_RU"))

        assertEquals(
                listOf(Quantity.ONE, Quantity.OTHER),
                PluralUtils.quantitiesForLocale("en_RU"))

        assertEquals(
                listOf(Quantity.ONE, Quantity.TWO, Quantity.OTHER),
                PluralUtils.quantitiesForLocale("iu"))
    }
}