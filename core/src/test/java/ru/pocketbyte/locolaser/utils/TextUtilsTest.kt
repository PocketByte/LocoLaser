package ru.pocketbyte.locolaser.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class TextUtilsTest {

    @Test
    fun testKeyToProperty() {
        assertEquals("some_value", TextUtils.keyToProperty("Some Value"))
        assertEquals("some_value", TextUtils.keyToProperty("some__value__"))
        assertEquals("some_value2", TextUtils.keyToProperty("some 'value2'"))
        assertEquals("some_value_2", TextUtils.keyToProperty("some 'value' + 2"))
        assertEquals("formula_x_y_3_2_z", TextUtils.keyToProperty("formula x = (y + 3/2) * z."))
        assertEquals("_1_value", TextUtils.keyToProperty("1 value"))
    }

}
