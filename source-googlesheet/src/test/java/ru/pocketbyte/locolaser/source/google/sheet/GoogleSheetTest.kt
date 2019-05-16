package ru.pocketbyte.locolaser.source.google.sheet

import org.junit.Test

import org.junit.Assert.assertEquals

/**
 * Created by dshurygin on 2017-03-23.
 */
class GoogleSheetTest {

    companion object {

        private val TEST_VALUES = arrayOf(
                "+24 value",
                "+ 24 value",
                "+some text",
                "+ some text",
                "=24 value",
                "= 24 value",
                "=some text",
                "= some text",
                "'quotes'",

                "23 + 24 = ?",
                " + if space in the beginning. Don't add quotes!",
                " = if space in the beginning. Don't add quotes!",
                " ' if space in the beginning. Don't add quotes!"
        )

        private val TEST_SOURCE_VALUES = arrayOf(
                "'+24 value",
                "'+ 24 value",
                "'+some text",
                "'+ some text",
                "'=24 value",
                "'= 24 value",
                "'=some text",
                "'= some text",
                "''quotes'",

                "23 + 24 = ?",
                " + if space in the beginning. Don't add quotes!",
                " = if space in the beginning. Don't add quotes!",
                " ' if space in the beginning. Don't add quotes!")
    }

    @Test
    fun testValueArraysHasSameLength() {
        assertEquals(TEST_VALUES.size.toLong(), TEST_SOURCE_VALUES.size.toLong())
    }

    @Test
    fun testValueToSourceValue() {
        for (i in TEST_VALUES.indices) {
            assertEquals(TEST_SOURCE_VALUES[i], GoogleSheet.valueToSourceValue(TEST_VALUES[i]))
        }
    }

    @Test
    fun testSourceValueToValue() {
        for (i in TEST_SOURCE_VALUES.indices) {
            assertEquals(TEST_VALUES[i], GoogleSheet.sourceValueToValue(TEST_SOURCE_VALUES[i]))
        }
    }
}
