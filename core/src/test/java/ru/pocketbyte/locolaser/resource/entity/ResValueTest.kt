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
class ResValueTest {

    @Test
    @Throws(Exception::class)
    fun testDefaultQuantity() {
        assertEquals(Quantity.OTHER, ResValue("value", null).quantity)
    }

    @Test
    @Throws(Exception::class)
    fun testEqual() {
        assertEquals(
                ResValue("value1", null),
                ResValue("value1", null))
        assertEquals(
                ResValue("value1", "Comment1"),
                ResValue("value1", "Comment1"))
        assertEquals(
                ResValue("value1", "Com1", Quantity.TWO),
                ResValue("value1", "Com1", Quantity.TWO))
        assertEquals(
                ResValue("value1", "Com", Quantity.MANY),
                ResValue("value1", "Com", Quantity.MANY))
        assertEquals(
                ResValue("value1", null, meta = mutableMapOf()),
                ResValue("value1", null, meta = emptyMap()))
        assertEquals(
                ResValue("value1", null, meta = mutableMapOf()),
                ResValue("value1", null))
        assertEquals(
                ResValue("value1", null, meta = mapOf(Pair("Meta1", "v1"), Pair("Meta2", "value"))),
                ResValue("value1", null, meta = mapOf(Pair("Meta1", "v1"), Pair("Meta2", "value"))))

        assertNotEquals(
                ResValue("value1", null),
                ResValue("value2", null))
        assertNotEquals(
                ResValue("value1", "Comment1"),
                ResValue("value1", "Comment2"))
        assertNotEquals(
                ResValue("value1", "Com1", Quantity.TWO),
                ResValue("value2", "Com1", Quantity.TWO))
        assertNotEquals(
                ResValue("value1", "Com", Quantity.TWO),
                ResValue("value1", "Com", Quantity.MANY))
        assertNotEquals(
                ResValue("value1", null, meta = mapOf(Pair("Meta1", "v1"), Pair("Meta2", "value"))),
                ResValue("value1", null, meta = mapOf(Pair("Meta1", "v1"), Pair("Meta2", "another"))))
        assertNotEquals(
                ResValue("value1", null, meta = mapOf(Pair("Meta", "data"))),
                ResValue("value1", null))

    }

    @Test
    @Throws(Exception::class)
    fun testMerge() {
        var item1 = ResValue("value1", null)
        var item2 = ResValue("value2", null)
        assertEquals(ResValue("value2", null), item1.merge(item2))

        item1 = ResValue("value1", "Comment2")
        item2 = ResValue("value2", "Comment1")
        assertEquals(ResValue("value2", "Comment1"), item1.merge(item2))

        item1 = ResValue("value3", "Com2", Quantity.TWO)
        item2 = ResValue("value4", "Com1", Quantity.MANY)
        assertEquals(ResValue("value4", "Com1", Quantity.MANY), item1.merge(item2))

        item1 = ResValue("value1", null)
        item2 = ResValue("value2", null, meta = mapOf(Pair("M", "2")))
        assertEquals(ResValue("value2", null, meta = mapOf(Pair("M", "2"))), item1.merge(item2))

        item1 = ResValue("value2", null, Quantity.TWO, meta = mapOf(Pair("M", "3")))
        item2 = ResValue("value1", null, Quantity.MANY)
        assertEquals(ResValue("value1", null, Quantity.MANY, meta = mapOf(Pair("M", "3"))), item1.merge(item2))

        item1 = ResValue("value2", null, Quantity.ONE, meta = mapOf(Pair("M1", "3")))
        item2 = ResValue("value1", null, Quantity.OTHER, meta = mapOf(Pair("M2", "hello")))
        assertEquals(ResValue("value1", null, Quantity.OTHER,
                meta = mapOf(Pair("M1", "3"), Pair("M2", "hello"))), item1.merge(item2))

        // Merge nulls
        assertSame(null.merge(item1), item1)
        assertSame(item1.merge(null), item1)
        assertNull((null as? ResValue).merge(null))
    }
}
