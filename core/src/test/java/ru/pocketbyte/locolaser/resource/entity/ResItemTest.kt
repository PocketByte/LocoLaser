/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

/**
 * @author Denis Shurygin
 */
class ResItemTest {

    private var resKey: String? = null
    private var resItem: ResItem? = null

    @Before
    fun init() {
        resKey = "res_key"
        resItem = ResItem(resKey!!)
    }

    @Test
    @Throws(Exception::class)
    fun testItemConstructor() {
        assertEquals(resKey, resItem!!.key)
        assertEquals(0, resItem!!.values.size.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun testAddValue() {
        val value = ResValue("value", null, Quantity.OTHER)

        assert(resItem!!.addValue(value))

        assertEquals(1, resItem!!.values.size.toLong())
        assertEquals(value, resItem!!.values[0])
    }

    @Test
    @Throws(Exception::class)
    fun testAddSameQuantity() {
        val value1 = ResValue("value1", null, Quantity.FEW)
        val value2 = ResValue("value2", "Comment", Quantity.FEW)

        assert(resItem!!.addValue(value1))
        assert(!resItem!!.addValue(value2))

        assertEquals(1, resItem!!.values.size.toLong())
        assertNotEquals(value1, resItem!!.values[0])
        assertEquals(value2, resItem!!.values[0])
    }

    @Test
    @Throws(Exception::class)
    fun testAddManyQuantity() {
        val value1 = ResValue("value1", null, Quantity.MANY)
        val value2 = ResValue("value2", "Comment", Quantity.FEW)

        assert(resItem!!.addValue(value1))
        assert(resItem!!.addValue(value2))

        assertEquals(2, resItem!!.values.size.toLong())

        assertEquals(value1, resItem!!.values[0])
        assertEquals(value2, resItem!!.values[1])
    }

    @Test
    @Throws(Exception::class)
    fun testValueForQuantity() {
        val value1 = ResValue("value1", null, Quantity.MANY)
        val value2 = ResValue("value2", "Comment", Quantity.FEW)

        resItem!!.addValue(value1)
        resItem!!.addValue(value2)

        assertEquals(value1, resItem!!.valueForQuantity(value1.quantity))
        assertEquals(value2, resItem!!.valueForQuantity(value2.quantity))
        assertNull(resItem!!.valueForQuantity(Quantity.ONE))
    }

    @Test
    @Throws(Exception::class)
    fun testRemove() {
        val value1 = ResValue("value1", null, Quantity.MANY)
        val value2 = ResValue("value2", "Comment", Quantity.FEW)

        resItem!!.addValue(value1)
        resItem!!.addValue(value2)

        assert(resItem!!.removeValue(value1))
        assertEquals(1, resItem!!.values.size.toLong())
        assertNull(resItem!!.valueForQuantity(value1.quantity))
        assertEquals(value2, resItem!!.valueForQuantity(value2.quantity))
        assertEquals(value2, resItem!!.values[0])

        assert(!resItem!!.removeValue(value1))
        assertEquals(1, resItem!!.values.size.toLong())
        assertEquals(value2, resItem!!.valueForQuantity(value2.quantity))
        assertEquals(value2, resItem!!.values[0])
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveForQuantity() {
        val value1 = ResValue("value1", null, Quantity.MANY)
        val value2 = ResValue("value2", "Comment", Quantity.FEW)

        resItem!!.addValue(value1)
        resItem!!.addValue(value2)

        assertEquals(value1, resItem!!.removeValueForQuantity(value1.quantity))
        assertEquals(1, resItem!!.values.size.toLong())
        assertNull(resItem!!.valueForQuantity(value1.quantity))
        assertEquals(value2, resItem!!.valueForQuantity(value2.quantity))
        assertEquals(value2, resItem!!.values[0])

        assertNull(resItem!!.removeValueForQuantity(value1.quantity))
        assertEquals(1, resItem!!.values.size.toLong())
        assertEquals(value2, resItem!!.valueForQuantity(value2.quantity))
        assertEquals(value2, resItem!!.values[0])
    }

    @Test
    @Throws(Exception::class)
    fun testIsHasQuantities() {
        assert(!resItem!!.isHasQuantities)

        resItem!!.addValue(ResValue("Value1", null, Quantity.OTHER))

        assert(!resItem!!.isHasQuantities)

        resItem!!.addValue(ResValue("Value1", null, Quantity.MANY))

        assert(resItem!!.isHasQuantities)

        resItem!!.removeValueForQuantity(Quantity.OTHER)

        assert(resItem!!.isHasQuantities)

        resItem!!.removeValueForQuantity(Quantity.MANY)

        assert(!resItem!!.isHasQuantities)
    }

    @Test
    @Throws(Exception::class)
    fun testMerge() {
        val value1 = ResValue("value1", null, Quantity.OTHER)
        val value2 = ResValue("value2", "Comment", Quantity.FEW)
        val value3 = ResValue("value3", "Comment2", Quantity.ZERO)
        resItem!!.addValue(value1)
        resItem!!.addValue(value2)
        resItem!!.addValue(value3)

        val resItem2 = ResItem(resItem!!.key + "_") // Merge should work independent from res key
        val value4 = ResValue("value4", null, Quantity.OTHER)
        val value5 = ResValue("value5", "Comment", Quantity.FEW)
        val value6 = ResValue("value6", "Comment2", Quantity.TWO)
        resItem2.addValue(value4)
        resItem2.addValue(value5)
        resItem2.addValue(value6)

        assert(resItem!!.merge(resItem2) === resItem)

        assertEquals(4, resItem!!.values.size.toLong())
        assertNotEquals(value1, resItem!!.valueForQuantity(Quantity.OTHER))
        assertNotEquals(value2, resItem!!.valueForQuantity(Quantity.FEW))
        assertEquals(value3, resItem!!.valueForQuantity(Quantity.ZERO))
        assertEquals(value4, resItem!!.valueForQuantity(Quantity.OTHER))
        assertEquals(value5, resItem!!.valueForQuantity(Quantity.FEW))
        assertEquals(value6, resItem!!.valueForQuantity(Quantity.TWO))
    }

    @Test
    @Throws(Exception::class)
    fun testEquals() {
        assertEquals(ResItem("key1"), ResItem("key1"))
        assertEquals(
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null))),
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null))))
        assertEquals(
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null),
                        ResValue("value2", "Com", Quantity.TWO))),
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null),
                        ResValue("value2", "Com", Quantity.TWO))))
        assertEquals(
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null),
                        ResValue("value2", "Com", Quantity.MANY))),
                prepareResItem("some_key", arrayOf(
                        ResValue("value2", "Com", Quantity.MANY),
                        ResValue("value1", null))))

        // Not equals
        assertNotEquals(ResItem("key1"), ResItem("key2"))
        assertNotEquals(
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null))),
                prepareResItem("some_key", arrayOf(
                        ResValue("value2", null))))
        assertNotEquals(
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null),
                        ResValue("value2", "Com", Quantity.TWO))),
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null))))
        assertNotEquals(
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null),
                        ResValue("value2", "Com", Quantity.TWO))),
                prepareResItem("some_key", arrayOf(
                        ResValue("value1", null),
                        ResValue("value2", "Com", Quantity.MANY))))
    }

    @Test
    fun testClone() {
        val item1 = prepareResItem("some_key", arrayOf(
                ResValue("value1", null),
                ResValue("value2", "Com", Quantity.TWO)))

        val item2 = ResItem(item1)
        assertEquals(item1, item2)
    }

    @Test(expected = NullPointerException::class)
    fun testCloneNullItem() {
        ResItem((null as ResItem?)!!)
    }

    private fun prepareResItem(key: String, values: Array<ResValue>): ResItem {
        val resItem = ResItem(key)
        for (value in values)
            resItem.addValue(value)
        return resItem
    }
}
