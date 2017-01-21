/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Denis Shurygin
 */
public class ResValueTest {

    @Test
    public void testDefaultQuantity() throws Exception {
        ResValue resValue = new ResValue("value", null);
        assertEquals(Quantity.OTHER, resValue.quantity);

        resValue = new ResValue("value", "Comment", null);
        assertEquals(Quantity.OTHER, resValue.quantity);
    }

    @Test
    public void testEqual() throws Exception {
        assertEquals(new ResValue(null, null), new ResValue(null, null));
        assertEquals(new ResValue("value1", null), new ResValue("value1", null));
        assertEquals(new ResValue(null, "Comment1"), new ResValue(null, "Comment1"));
        assertEquals(new ResValue("value1", "Comment1"), new ResValue("value1", "Comment1"));
        assertEquals(new ResValue("value1", "Com1", Quantity.TWO), new ResValue("value1", "Com1", Quantity.TWO));
        assertEquals(new ResValue("value1", "Com", Quantity.MANY), new ResValue("value1", "Com", Quantity.MANY));

        assertNotEquals(new ResValue(null, null), new ResValue("not null", null));
        assertNotEquals(new ResValue("value1", null), new ResValue("value2", null));
        assertNotEquals(new ResValue(null, "Comment1"), new ResValue(null, "Comment2"));
        assertNotEquals(new ResValue("value1", "Comment1"), new ResValue("value1", "Comment2"));
        assertNotEquals(new ResValue("value1", "Com1", Quantity.TWO), new ResValue("value2", "Com1", Quantity.TWO));
        assertNotEquals(new ResValue("value1", "Com", Quantity.TWO), new ResValue("value1", "Com", Quantity.MANY));
    }
}
