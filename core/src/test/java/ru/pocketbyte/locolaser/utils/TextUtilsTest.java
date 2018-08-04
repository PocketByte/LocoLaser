package ru.pocketbyte.locolaser.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextUtilsTest {

    @Test
    public void testKeyToProperty() {
        assertEquals("some_value", TextUtils.keyToProperty("Some Value"));
        assertEquals("some_value", TextUtils.keyToProperty("some__value__"));
        assertEquals("some_value2", TextUtils.keyToProperty("some 'value2'"));
        assertEquals("some_value_2", TextUtils.keyToProperty("some 'value' + 2"));
        assertEquals("formula_x_y_3_2_z", TextUtils.keyToProperty("formula x = (y + 3/2) * z."));
        assertEquals("_1_value", TextUtils.keyToProperty("1 value"));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(TextUtils.isEmpty(null));
        assertTrue(TextUtils.isEmpty(""));
        assertTrue(TextUtils.isEmpty(" "));
        assertTrue(TextUtils.isEmpty("  "));
        assertFalse(TextUtils.isEmpty(" null"));
        assertFalse(TextUtils.isEmpty("Not empty "));
    }

}
