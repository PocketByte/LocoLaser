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
        assertEquals(ResValue("value1", null), ResValue("value1", null))
        assertEquals(ResValue("value1", "Comment1"), ResValue("value1", "Comment1"))
        assertEquals(ResValue("value1", "Com1", Quantity.TWO), ResValue("value1", "Com1", Quantity.TWO))
        assertEquals(ResValue("value1", "Com", Quantity.MANY), ResValue("value1", "Com", Quantity.MANY))

        assertNotEquals(ResValue("value1", null), ResValue("value2", null))
        assertNotEquals(ResValue("value1", "Comment1"), ResValue("value1", "Comment2"))
        assertNotEquals(ResValue("value1", "Com1", Quantity.TWO), ResValue("value2", "Com1", Quantity.TWO))
        assertNotEquals(ResValue("value1", "Com", Quantity.TWO), ResValue("value1", "Com", Quantity.MANY))
    }
}
