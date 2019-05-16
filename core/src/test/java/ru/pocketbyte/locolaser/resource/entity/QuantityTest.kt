/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.util.Arrays

import org.junit.Assert.assertEquals

/**
 * @author Denis Shurygin
 */
@RunWith(JUnit4::class)
class QuantityTest {

    private var quantityPairs: List<Pair<String, Quantity>>? = null

    @Before
    fun init() {
        quantityPairs = Arrays.asList(
                Pair("zero", Quantity.ZERO),
                Pair("one", Quantity.ONE),
                Pair("two", Quantity.TWO),
                Pair("few", Quantity.FEW),
                Pair("many", Quantity.MANY),
                Pair("other", Quantity.OTHER)
        )
    }

    @Test
    @Throws(Exception::class)
    fun testToString() {
        for ((first, second) in quantityPairs!!) {
            assertEquals(first, second.toString())
        }
    }

    @Test
    @Throws(Exception::class)
    fun testFromString() {
        for ((first, second) in quantityPairs!!) {
            assertEquals(second, Quantity.fromString(first))
        }
    }

    @Test
    @Throws(Exception::class)
    fun testFromStringFallback() {
        val invalidQuantity = "invalid_quantity"
        assertEquals(Quantity.OTHER, Quantity.fromString(invalidQuantity))
        assertEquals(Quantity.OTHER, Quantity.fromString(null))
        assertEquals(Quantity.OTHER, Quantity.fromString(invalidQuantity, Quantity.OTHER))
        assertEquals(Quantity.MANY, Quantity.fromString(invalidQuantity, Quantity.MANY))
    }

    @Test
    @Throws(Exception::class)
    fun testQuantityOther() {
        assertEquals(1, Quantity.QUANTITY_OTHER.size.toLong())
        assert(Quantity.QUANTITY_OTHER.contains(Quantity.OTHER))
    }
}
