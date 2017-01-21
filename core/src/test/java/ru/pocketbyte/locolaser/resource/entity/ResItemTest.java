/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Denis Shurygin
 */
public class ResItemTest {

    private String resKey;
    private ResItem resItem;

    @Before
    public void init() {
        resKey = "res_key";
        resItem = new ResItem(resKey);
    }

    @Test
    public void testItemConstructor() throws Exception {
        assertEquals(resKey, resItem.key);
        assertEquals(0, resItem.values.size());
    }

    @Test
    public void testAddValue() throws Exception {
        ResValue value = new ResValue("value", null, Quantity.OTHER);

        assert resItem.addValue(value);

        assertEquals(1, resItem.values.size());
        assertEquals(value, resItem.values.get(0));
    }

    @Test
    public void testAddSameQuantity() throws Exception {
        ResValue value1 = new ResValue("value1", null, Quantity.FEW);
        ResValue value2 = new ResValue("value2", "Comment", Quantity.FEW);

        assert resItem.addValue(value1);
        assert !resItem.addValue(value2);

        assertEquals(1, resItem.values.size());
        assertNotEquals(value1, resItem.values.get(0));
        assertEquals(value2, resItem.values.get(0));
    }

    @Test
    public void testAddManyQuantity() throws Exception {
        ResValue value1 = new ResValue("value1", null, Quantity.MANY);
        ResValue value2 = new ResValue("value2", "Comment", Quantity.FEW);

        assert resItem.addValue(value1);
        assert resItem.addValue(value2);

        assertEquals(2, resItem.values.size());

        assertEquals(value1, resItem.values.get(0));
        assertEquals(value2, resItem.values.get(1));
    }

    @Test
    public void testValueForQuantity() throws Exception {
        ResValue value1 = new ResValue("value1", null, Quantity.MANY);
        ResValue value2 = new ResValue("value2", "Comment", Quantity.FEW);

        resItem.addValue(value1);
        resItem.addValue(value2);

        assertEquals(value1, resItem.valueForQuantity(value1.quantity));
        assertEquals(value2, resItem.valueForQuantity(value2.quantity));
        assertNull(resItem.valueForQuantity(Quantity.ONE));
    }

    @Test
    public void testRemove() throws Exception {
        ResValue value1 = new ResValue("value1", null, Quantity.MANY);
        ResValue value2 = new ResValue("value2", "Comment", Quantity.FEW);

        resItem.addValue(value1);
        resItem.addValue(value2);

        assert resItem.removeValue(value1);
        assertEquals(1, resItem.values.size());
        assertNull(resItem.valueForQuantity(value1.quantity));
        assertEquals(value2, resItem.valueForQuantity(value2.quantity));
        assertEquals(value2, resItem.values.get(0));

        assert !resItem.removeValue(value1);
        assertEquals(1, resItem.values.size());
        assertEquals(value2, resItem.valueForQuantity(value2.quantity));
        assertEquals(value2, resItem.values.get(0));
    }

    @Test
    public void testRemoveForQuantity() throws Exception {
        ResValue value1 = new ResValue("value1", null, Quantity.MANY);
        ResValue value2 = new ResValue("value2", "Comment", Quantity.FEW);

        resItem.addValue(value1);
        resItem.addValue(value2);

        assertEquals(value1, resItem.removeValueForQuantity(value1.quantity));
        assertEquals(1, resItem.values.size());
        assertNull(resItem.valueForQuantity(value1.quantity));
        assertEquals(value2, resItem.valueForQuantity(value2.quantity));
        assertEquals(value2, resItem.values.get(0));

        assertNull(resItem.removeValueForQuantity(value1.quantity));
        assertEquals(1, resItem.values.size());
        assertEquals(value2, resItem.valueForQuantity(value2.quantity));
        assertEquals(value2, resItem.values.get(0));
    }

    @Test
    public void testIsHasQuantities() throws Exception {
        assert !resItem.isHasQuantities();

        resItem.addValue(new ResValue("Value1", null, Quantity.OTHER));

        assert !resItem.isHasQuantities();

        resItem.addValue(new ResValue("Value1", null, Quantity.MANY));

        assert resItem.isHasQuantities();

        resItem.removeValueForQuantity(Quantity.OTHER);

        assert resItem.isHasQuantities();

        resItem.removeValueForQuantity(Quantity.MANY);

        assert !resItem.isHasQuantities();
    }

    @Test
    public void testMerge() throws Exception {
        ResValue value1 = new ResValue("value1", null, Quantity.OTHER);
        ResValue value2 = new ResValue("value2", "Comment", Quantity.FEW);
        ResValue value3 = new ResValue("value3", "Comment2", Quantity.ZERO);
        resItem.addValue(value1);
        resItem.addValue(value2);
        resItem.addValue(value3);

        ResItem resItem2 = new ResItem(resItem.key + "_"); // Merge should work independent from res key
        ResValue value4 = new ResValue("value4", null, Quantity.OTHER);
        ResValue value5 = new ResValue("value5", "Comment", Quantity.FEW);
        ResValue value6 = new ResValue("value6", "Comment2", Quantity.TWO);
        resItem2.addValue(value4);
        resItem2.addValue(value5);
        resItem2.addValue(value6);

        assert resItem.merge(resItem2) == resItem;

        assertEquals(4, resItem.values.size());
        assertNotEquals(value1, resItem.valueForQuantity(Quantity.OTHER));
        assertNotEquals(value2, resItem.valueForQuantity(Quantity.FEW));
        assertEquals(value3, resItem.valueForQuantity(Quantity.ZERO));
        assertEquals(value4, resItem.valueForQuantity(Quantity.OTHER));
        assertEquals(value5, resItem.valueForQuantity(Quantity.FEW));
        assertEquals(value6, resItem.valueForQuantity(Quantity.TWO));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testValuesImmutable() throws UnsupportedOperationException {
        resItem.values.add(new ResValue("value1", null, Quantity.OTHER));
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(new ResItem("key1"), new ResItem("key1"));
        assertEquals(
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null)
                }),
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null)
                }));
        assertEquals(
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null),
                        new ResValue("value2", "Com", Quantity.TWO)
                }),
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null),
                        new ResValue("value2", "Com", Quantity.TWO)
                }));
        assertEquals(
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null),
                        new ResValue("value2", "Com", Quantity.MANY)
                }),
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value2", "Com", Quantity.MANY),
                        new ResValue("value1", null)
                }));

        // Not equals
        assertNotEquals(new ResItem("key1"), new ResItem("key2"));
        assertNotEquals(
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null)
                }),
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value2", null)
                }));
        assertNotEquals(
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null),
                        new ResValue("value2", "Com", Quantity.TWO)
                }),
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null)
                }));
        assertNotEquals(
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null),
                        new ResValue("value2", "Com", Quantity.TWO)
                }),
                prepareResItem("some_key", new ResValue[] {
                        new ResValue("value1", null),
                        new ResValue("value2", "Com", Quantity.MANY)
                }));
    }

    @Test
    public void testClone() {
        ResItem item1 = prepareResItem("some_key", new ResValue[] {
                new ResValue("value1", null),
                new ResValue("value2", "Com", Quantity.TWO)
        });

        ResItem item2 = new ResItem(item1);
        assertEquals(item1, item2);
    }

    @Test(expected = NullPointerException.class)
    public void testCloneNullItem() {
        new ResItem((ResItem) null);
    }

    private ResItem prepareResItem(String key, ResValue[] values) {
        ResItem resItem = new ResItem(key);
        for (ResValue value: values)
            resItem.addValue(value);
        return resItem;
    }
}
