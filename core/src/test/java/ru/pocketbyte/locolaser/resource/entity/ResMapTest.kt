/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import org.junit.Test

import org.junit.Assert.*

/**
 * @author Denis Shurygin
 */
class ResMapTest {

    @Test
    @Throws(Exception::class)
    fun testMerge() {
        val resMap1 = prepareResMap1()
        val resMap2 = prepareResMap2()

        assert(resMap1.merge(resMap2) === resMap1)

        assertEquals(4, resMap1.size)
        assertEquals(prepareResLocale1().merge(prepareResLocale2()), resMap1["locale1"])
        assertEquals(prepareResLocale2().merge(prepareResLocale3()), resMap1["locale2"])
        assertEquals(prepareResLocale3(), resMap1["locale3"])
        assertEquals(prepareResLocale1(), resMap1["locale4"])
    }

    @Test
    @Throws(Exception::class)
    fun testRemove() {
        val resMap1 = prepareResMap1()
        val resMap2 = prepareResMap2()

        resMap1.put("locale5", prepareResLocale1())
        resMap2.put("locale5", prepareResLocale1())

        assert(resMap1.remove(resMap2) === resMap1)

        assertEquals(3, resMap1.size)
        assertEquals(prepareResLocale1().remove(prepareResLocale2()), resMap1["locale1"])
        assertEquals(prepareResLocale2().remove(prepareResLocale3()), resMap1["locale2"])
        assertEquals(prepareResLocale3(), resMap1["locale3"])
        assertNull(resMap1["locale4"])
        assertNull(resMap1["locale5"])
    }

    @Test
    fun testClone() {
        val resMap1 = prepareResMap1()
        val resMap2 = ResMap(resMap1)

        assertEquals(resMap1, resMap2)
    }

    @Test
    fun testCloneNullMap() {
        val resMap = ResMap(null)
        assertTrue(resMap.isEmpty())
    }

    @Test
    fun testFilter() {
        val resMap = prepareResMap1().filter { it.contains("3") ||it.contains("1")  }

        val locale1 = resMap["locale1"]
        assertEquals(2, locale1!!.size)
        assertNotNull(locale1["key1"])
        assertNull(locale1["key2"])
        assertNotNull(locale1["key3"])
        assertNull(locale1["key4"])

        val locale2 = resMap["locale2"]
        assertEquals(2, locale2!!.size)
        assertNotNull(locale2["key1"])
        assertNull(locale2["key2"])
        assertNotNull(locale2["key3"])
        assertNull(locale2["key4"])

        val locale3 = resMap["locale3"]
        assertEquals(1, locale3!!.size)
        assertNotNull(locale3["key1"])
        assertNull(locale3["key2"])
        assertNull(locale3["key3"])
        assertNull(locale3["key4"])
    }

    private fun prepareResMap1(): ResMap {
        val resMap = ResMap()
        resMap.put("locale1", prepareResLocale1())
        resMap.put("locale2", prepareResLocale2())
        resMap.put("locale3", prepareResLocale3())
        return resMap
    }

    private fun prepareResMap2(): ResMap {
        val resMap = ResMap()
        resMap.put("locale1", prepareResLocale2())
        resMap.put("locale2", prepareResLocale3())
        resMap.put("locale4", prepareResLocale1())
        return resMap
    }

    private fun prepareResLocale1(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_1", null, Quantity.OTHER), ResValue("value2_1", null, Quantity.MANY), ResValue("value2_1", null, Quantity.FEW))))
        resLocale.put(prepareResItem("key2", arrayOf(ResValue("value1_1", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value1_1", null, Quantity.OTHER), ResValue("value2_1", null, Quantity.MANY), ResValue("value3_1", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResLocale2(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_2", null, Quantity.OTHER), ResValue("value2_2", null, Quantity.MANY), ResValue("value3_2", "Comment", Quantity.TWO))))
        resLocale.put(prepareResItem("key3", arrayOf(ResValue("value1_2", null, Quantity.OTHER))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value1_2", null, Quantity.OTHER), ResValue("value2_2", null, Quantity.MANY), ResValue("value3_2", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResLocale3(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1", arrayOf(ResValue("value1_3", null, Quantity.OTHER), ResValue("value2_3", null, Quantity.FEW), ResValue("value3_3", "Comment", Quantity.TWO), ResValue("value4_3", "Comment", Quantity.ZERO))))
        resLocale.put(prepareResItem("key4", arrayOf(ResValue("value1_3", null, Quantity.OTHER), ResValue("value3_3", "Comment", Quantity.ZERO))))
        return resLocale
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }
}
