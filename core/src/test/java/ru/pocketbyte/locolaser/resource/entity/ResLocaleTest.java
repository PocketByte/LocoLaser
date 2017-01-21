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
public class ResLocaleTest {

    @Test
    public void testMerge() throws Exception {
        ResLocale resLocale1Original = prepareResLocale1();
        ResLocale resLocale1 = prepareResLocale1();
        ResLocale resLocale2 = prepareResLocale2();

        assert resLocale1.merge(resLocale2) == resLocale1;

        // ==============
        // Key 1
        ResItem resItem1_1 = resLocale1.get("key1");
        ResItem resItem1_2 = resLocale2.get("key1");
        ResItem resItem1_1_Original = resLocale1Original.get("key1");

        assertNotNull(resItem1_1);
        assertEquals(4, resItem1_1.values.size());

        // Check values from map 1
        assertNotEquals(resItem1_1_Original.valueForQuantity(Quantity.OTHER),
                resItem1_1.valueForQuantity(Quantity.OTHER));
        assertNotEquals(resItem1_1_Original.valueForQuantity(Quantity.MANY),
                resItem1_1.valueForQuantity(Quantity.MANY));
        assertEquals(resItem1_1_Original.valueForQuantity(Quantity.FEW), resItem1_1.valueForQuantity(Quantity.FEW));

        // Check values from map 2
        assertEquals(resItem1_2.valueForQuantity(Quantity.OTHER), resItem1_1.valueForQuantity(Quantity.OTHER));
        assertEquals(resItem1_2.valueForQuantity(Quantity.MANY), resItem1_1.valueForQuantity(Quantity.MANY));
        assertEquals(resItem1_2.valueForQuantity(Quantity.TWO), resItem1_1.valueForQuantity(Quantity.TWO));

        // ==============
        // Key 2
        ResItem resItem2_1 = resLocale1.get("key2");

        assertNotNull(resItem2_1);
        assertEquals(1, resItem2_1.values.size());
        assertEquals(resLocale1Original.get("key2"), resItem2_1);

        // ==============
        // Key 3
        ResItem resItem3_1 = resLocale1.get("key3");
        ResItem resItem3_2 = resLocale2.get("key3");
        ResItem resItem3_1_Original = resLocale1Original.get("key3");

        assertNotNull(resItem3_1);
        assertEquals(3, resItem3_1.values.size());

        // Check values from map 1
        assertNotEquals(resItem3_1_Original.valueForQuantity(Quantity.OTHER),
                resItem3_1.valueForQuantity(Quantity.OTHER));
        assertEquals(resItem3_1_Original.valueForQuantity(Quantity.MANY), resItem3_1.valueForQuantity(Quantity.MANY));
        assertEquals(resItem3_1_Original.valueForQuantity(Quantity.ZERO), resItem3_1.valueForQuantity(Quantity.ZERO));

        // Check values from map 2
        assertEquals(resItem3_2.valueForQuantity(Quantity.OTHER), resItem3_1.valueForQuantity(Quantity.OTHER));

        // ==============
        // Key 4
        ResItem resItem4_1 = resLocale1.get("key4");

        assertNotNull(resItem4_1);
        assertEquals(3, resItem4_1.values.size());
        assertEquals(resLocale2.get("key4"), resItem4_1);
    }


    @Test
    public void testRemove() throws Exception {
        ResLocale resLocale1Original = prepareResLocale1();
        ResLocale resLocale1 = prepareResLocale1();
        ResLocale resLocale2 = prepareResLocale2();

        resLocale1.remove(resLocale2);

        // ==============
        // Key 1
        ResItem resItem1_1 = resLocale1.get("key1");
        ResItem resItem1_1_Original = resLocale1Original.get("key1");

        assertNotNull(resItem1_1);
        assertEquals(1, resItem1_1.values.size());

        assertEquals(resItem1_1_Original.valueForQuantity(Quantity.FEW), resItem1_1.valueForQuantity(Quantity.FEW));
        assertNull(resItem1_1.valueForQuantity(Quantity.OTHER));
        assertNull(resItem1_1.valueForQuantity(Quantity.MANY));
        assertNull(resItem1_1.valueForQuantity(Quantity.TWO));

        // ==============
        // Key 2
        ResItem resItem2_1 = resLocale1.get("key2");

        assertNotNull(resItem2_1);
        assertEquals(1, resItem2_1.values.size());
        assertEquals(resLocale1Original.get("key2"), resItem2_1);

        // ==============
        // Key 3
        ResItem resItem3_1 = resLocale1.get("key3");
        ResItem resItem3_1_Original = resLocale1Original.get("key3");

        assertNotNull(resItem3_1);
        assertEquals(2, resItem3_1.values.size());

        assertEquals(resItem3_1_Original.valueForQuantity(Quantity.MANY), resItem3_1.valueForQuantity(Quantity.MANY));
        assertEquals(resItem3_1_Original.valueForQuantity(Quantity.ZERO), resItem3_1.valueForQuantity(Quantity.ZERO));
        assertNull(resItem3_1.valueForQuantity(Quantity.OTHER));

        // ==============
        // Key 4
        assertNull(resLocale1.get("key4"));
    }

    @Test
    public void testEqual() throws Exception {
        ResLocale locale1 = prepareResLocale1();
        ResLocale locale2 = prepareResLocale1();

        assertEquals(new ResLocale(), new ResLocale());
        assertEquals(locale1, locale2);
        assertNotEquals(new ResLocale(), locale1);
        assertEquals(prepareResLocale2(), prepareResLocale2());

        locale1.remove("key2");
        locale1.put(prepareResItem("key2", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER)
        }));
        assertEquals(locale1, locale2);
    }

    @Test
    public void testClone() {
        ResLocale locale1 = prepareResLocale1();
        ResLocale locale2 = new ResLocale(locale1);

        assertEquals(locale1, locale2);
    }

    @Test
    public void testCloneNullMap() {
        ResLocale locale = new ResLocale(null);
        assertTrue(locale.isEmpty());
    }

    private ResLocale prepareResLocale1() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
                new ResValue("value2_1", null, Quantity.MANY),
                new ResValue("value2_1", null, Quantity.FEW)
        }));
        resLocale.put(prepareResItem("key2", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_1", null, Quantity.OTHER),
                new ResValue("value2_1", null, Quantity.MANY),
                new ResValue("value3_1", "Comment", Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResLocale prepareResLocale2() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
                new ResValue("value2_2", null, Quantity.MANY),
                new ResValue("value3_2", "Comment", Quantity.TWO)
        }));
        resLocale.put(prepareResItem("key3", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value1_2", null, Quantity.OTHER),
                new ResValue("value2_2", null, Quantity.MANY),
                new ResValue("value3_2", "Comment", Quantity.ZERO)
        }));
        return resLocale;
    }

    private ResItem prepareResItem(String key, ResValue[] values) {
        ResItem resItem = new ResItem(key);
        for (ResValue value: values)
            resItem.addValue(value);
        return resItem;
    }
}
