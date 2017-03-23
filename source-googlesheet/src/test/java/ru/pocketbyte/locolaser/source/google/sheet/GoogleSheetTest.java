package ru.pocketbyte.locolaser.source.google.sheet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dshurygin on 2017-03-23.
 */
public class GoogleSheetTest {

    private static final String[] TEST_VALUES = new String[] {
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
    };

    private static final String[] TEST_SOURCE_VALUES = new String[] {
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
            " ' if space in the beginning. Don't add quotes!"
    };

    @Test
    public void  testValueArraysHasSameLength() {
        assertEquals(TEST_VALUES.length, TEST_SOURCE_VALUES.length);
    }

    @Test
    public void testValueToSourceValue() {
        GoogleSheet sheet = dummySheet();
        for (int i = 0; i < TEST_VALUES.length; i++) {
            assertEquals(TEST_SOURCE_VALUES[i], sheet.valueToSourceValue(TEST_VALUES[i]));
        }
    }

    @Test
    public void testSourceValueToValue() {
        GoogleSheet sheet = dummySheet();
        for (int i = 0; i < TEST_SOURCE_VALUES.length; i++) {
            assertEquals(TEST_VALUES[i], sheet.sourceValueToValue(TEST_SOURCE_VALUES[i]));
        }
    }

    private GoogleSheet dummySheet() {
        return new GoogleSheet(new GoogleSheetConfig(), new WorksheetFacade(null, null, null));
    }
}
