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
class ResLocaleTest {

    @Test
    @Throws(Exception::class)
    fun testMerge() {
        val resLocale1Original = prepareResLocale1()
        val resLocale1 = prepareResLocale1()
        val resLocale2 = prepareResLocale2()

        assert(resLocale1.merge(resLocale2) === resLocale1)

        // ==============
        // Key 1
        val resItem1_1 = resLocale1["key1"]!!
        val resItem1_2 = resLocale2["key1"]!!
        val resItem1_1_Original = resLocale1Original["key1"]!!

        assertNotNull(resItem1_1)
        assertEquals(4, resItem1_1.values.size.toLong())

        // Check values from map 1
        assertNotEquals(resItem1_1_Original.valueForQuantity(Quantity.OTHER),
                resItem1_1.valueForQuantity(Quantity.OTHER))
        assertNotEquals(resItem1_1_Original.valueForQuantity(Quantity.MANY),
                resItem1_1.valueForQuantity(Quantity.MANY))
        assertEquals(resItem1_1_Original.valueForQuantity(Quantity.FEW), resItem1_1.valueForQuantity(Quantity.FEW))

        // Check values from map 2
        assertEquals(resItem1_2.valueForQuantity(Quantity.OTHER), resItem1_1.valueForQuantity(Quantity.OTHER))
        assertEquals(resItem1_2.valueForQuantity(Quantity.MANY), resItem1_1.valueForQuantity(Quantity.MANY))
        assertEquals(resItem1_2.valueForQuantity(Quantity.TWO), resItem1_1.valueForQuantity(Quantity.TWO))

        // ==============
        // Key 2
        val resItem2_1 = resLocale1["key2"]!!

        assertNotNull(resItem2_1)
        assertEquals(1, resItem2_1.values.size.toLong())
        assertEquals(resLocale1Original["key2"], resItem2_1)

        // ==============
        // Key 3
        val resItem3_1 = resLocale1["key3"]!!
        val resItem3_2 = resLocale2["key3"]!!
        val resItem3_1_Original = resLocale1Original["key3"]!!

        assertNotNull(resItem3_1)
        assertEquals(3, resItem3_1.values.size.toLong())

        // Check values from map 1
        assertNotEquals(resItem3_1_Original.valueForQuantity(Quantity.OTHER),
                resItem3_1.valueForQuantity(Quantity.OTHER))
        assertEquals(resItem3_1_Original.valueForQuantity(Quantity.MANY), resItem3_1.valueForQuantity(Quantity.MANY))
        assertEquals(resItem3_1_Original.valueForQuantity(Quantity.ZERO), resItem3_1.valueForQuantity(Quantity.ZERO))

        // Check values from map 2
        assertEquals(resItem3_2.valueForQuantity(Quantity.OTHER), resItem3_1.valueForQuantity(Quantity.OTHER))

        // ==============
        // Key 4
        val resItem4_1 = resLocale1["key4"]!!

        assertNotNull(resItem4_1)
        assertEquals(3, resItem4_1.values.size.toLong())
        assertEquals(resLocale2["key4"], resItem4_1)
    }


    @Test
    @Throws(Exception::class)
    fun testRemove() {
        val resLocale1Original = prepareResLocale1()
        val resLocale1 = prepareResLocale1()
        val resLocale2 = prepareResLocale2()

        resLocale1.remove(resLocale2)

        // ==============
        // Key 1
        val resItem1_1 = resLocale1["key1"]!!
        val resItem1_1_Original = resLocale1Original["key1"]!!

        assertNotNull(resItem1_1)
        assertEquals(1, resItem1_1.values.size.toLong())

        assertEquals(resItem1_1_Original.valueForQuantity(Quantity.FEW), resItem1_1.valueForQuantity(Quantity.FEW))
        assertNull(resItem1_1.valueForQuantity(Quantity.OTHER))
        assertNull(resItem1_1.valueForQuantity(Quantity.MANY))
        assertNull(resItem1_1.valueForQuantity(Quantity.TWO))

        // ==============
        // Key 2
        val resItem2_1 = resLocale1["key2"]!!

        assertNotNull(resItem2_1)
        assertEquals(1, resItem2_1.values.size.toLong())
        assertEquals(resLocale1Original["key2"], resItem2_1)

        // ==============
        // Key 3
        val resItem3_1 = resLocale1["key3"]!!
        val resItem3_1_Original = resLocale1Original["key3"]!!

        assertNotNull(resItem3_1)
        assertEquals(2, resItem3_1.values.size.toLong())

        assertEquals(resItem3_1_Original.valueForQuantity(Quantity.MANY), resItem3_1.valueForQuantity(Quantity.MANY))
        assertEquals(resItem3_1_Original.valueForQuantity(Quantity.ZERO), resItem3_1.valueForQuantity(Quantity.ZERO))
        assertNull(resItem3_1.valueForQuantity(Quantity.OTHER))

        // ==============
        // Key 4
        assertNull(resLocale1["key4"])
    }

    @Test
    @Throws(Exception::class)
    fun testEqual() {
        val locale1 = prepareResLocale1()
        val locale2 = prepareResLocale1()

        assertEquals(ResLocale(), ResLocale())
        assertEquals(locale1, locale2)
        assertNotEquals(ResLocale(), locale1)
        assertEquals(prepareResLocale2(), prepareResLocale2())

        locale1.remove("key2")
        locale1.put(prepareResItem("key2", ResValue("value1_1", null, Quantity.OTHER)))
        assertEquals(locale1, locale2)
    }

    @Test
    fun testClone() {
        val locale1 = prepareResLocale1()
        val locale2 = ResLocale(locale1)

        assertEquals(locale1, locale2)
    }

    @Test
    fun testCloneNullMap() {
        val locale = ResLocale(null)
        assertTrue(locale.isEmpty())
    }


    @Test
    fun testFilter() {
        val resLocale = ResLocale()

        resLocale.put(prepareResItem("key1",
                ResValue("value1_1", null)))
        resLocale.put(prepareResItem("welcome_key2",
                ResValue("value1_1", null)))
        resLocale.put(prepareResItem("welcome_3",
                ResValue("value1_1", null)))

        assertSame(resLocale, resLocale.filter(null))

        val filtered1 = resLocale.filter { it.startsWith("welcome") }
        assertEquals(2, filtered1.size)
        assertNull(filtered1["key1"])
        assertNotNull(filtered1["welcome_key2"])
        assertNotNull(filtered1["welcome_3"])

        val filtered2 = resLocale.filter { it.contains("key") }
        assertEquals(2, filtered2.size)
        assertNotNull(filtered2["key1"])
        assertNotNull(filtered2["welcome_key2"])
        assertNull(filtered2["welcome_3"])
    }

    private fun prepareResLocale1(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1",
                ResValue("value1_1", null, Quantity.OTHER),
                ResValue("value2_1", null, Quantity.MANY),
                ResValue("value2_1", null, Quantity.FEW)))
        resLocale.put(prepareResItem("key2",
                ResValue("value1_1", null, Quantity.OTHER)))
        resLocale.put(prepareResItem("key3",
                ResValue("value1_1", null, Quantity.OTHER),
                ResValue("value2_1", null, Quantity.MANY),
                ResValue("value3_1", "Comment", Quantity.ZERO)))
        return resLocale
    }

    private fun prepareResLocale2(): ResLocale {
        val resLocale = ResLocale()
        resLocale.put(prepareResItem("key1",
                ResValue("value1_2", null, Quantity.OTHER),
                ResValue("value2_2", null, Quantity.MANY),
                ResValue("value3_2", "Comment", Quantity.TWO)))
        resLocale.put(prepareResItem("key3",
                ResValue("value1_2", null, Quantity.OTHER)))
        resLocale.put(prepareResItem("key4",
                ResValue("value1_2", null, Quantity.OTHER),
                ResValue("value2_2", null, Quantity.MANY),
                ResValue("value3_2", "Comment", Quantity.ZERO)))
        return resLocale
    }

    private fun prepareResItem(key: String, vararg values: ResValue): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }
}
