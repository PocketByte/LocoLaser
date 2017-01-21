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
public class ResMapTest {

    @Test
    public void testMerge() throws Exception {
        ResMap resMap1 = prepareResMap1();
        ResMap resMap2 = prepareResMap2();

        assert resMap1.merge(resMap2) == resMap1;

        assertEquals(4, resMap1.size());
        assertEquals(prepareResLocale1().merge(prepareResLocale2()), resMap1.get("locale1"));
        assertEquals(prepareResLocale2().merge(prepareResLocale3()), resMap1.get("locale2"));
        assertEquals(prepareResLocale3(), resMap1.get("locale3"));
        assertEquals(prepareResLocale1(), resMap1.get("locale4"));
    }

    @Test
    public void testRemove() throws Exception {
        ResMap resMap1 = prepareResMap1();
        ResMap resMap2 = prepareResMap2();

        resMap1.put("locale5", prepareResLocale1());
        resMap2.put("locale5", prepareResLocale1());

        assert resMap1.remove(resMap2) == resMap1;

        assertEquals(3, resMap1.size());
        assertEquals(prepareResLocale1().remove(prepareResLocale2()), resMap1.get("locale1"));
        assertEquals(prepareResLocale2().remove(prepareResLocale3()), resMap1.get("locale2"));
        assertEquals(prepareResLocale3(), resMap1.get("locale3"));
        assertNull(resMap1.get("locale4"));
        assertNull(resMap1.get("locale5"));
    }

    @Test
    public void testClone() {
        ResMap resMap1 = prepareResMap1();
        ResMap resMap2 = new ResMap(resMap1);

        assertEquals(resMap1, resMap2);
    }

    @Test
    public void testCloneNullMap() {
        ResMap resMap = new ResMap(null);
        assertTrue(resMap.isEmpty());
    }

    private ResMap prepareResMap1() {
        ResMap resMap = new ResMap();
        resMap.put("locale1", prepareResLocale1());
        resMap.put("locale2", prepareResLocale2());
        resMap.put("locale3", prepareResLocale3());
        return resMap;
    }

    private ResMap prepareResMap2() {
        ResMap resMap = new ResMap();
        resMap.put("locale1", prepareResLocale2());
        resMap.put("locale2", prepareResLocale3());
        resMap.put("locale4", prepareResLocale1());
        return resMap;
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

    private ResLocale prepareResLocale3() {
        ResLocale resLocale = new ResLocale();
        resLocale.put(prepareResItem("key1", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
                new ResValue("value2_3", null, Quantity.FEW),
                new ResValue("value3_3", "Comment", Quantity.TWO),
                new ResValue("value4_3", "Comment", Quantity.ZERO)
        }));
        resLocale.put(prepareResItem("key4", new ResValue[]{
                new ResValue("value1_3", null, Quantity.OTHER),
                new ResValue("value3_3", "Comment", Quantity.ZERO)
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
