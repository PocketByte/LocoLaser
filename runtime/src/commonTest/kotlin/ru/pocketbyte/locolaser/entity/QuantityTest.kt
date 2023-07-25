/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Denis Shurygin
 */
class QuantityTest {

    private val quantityPairs = listOf(
        Pair("zero", Quantity.ZERO),
        Pair("one", Quantity.ONE),
        Pair("two", Quantity.TWO),
        Pair("few", Quantity.FEW),
        Pair("many", Quantity.MANY),
        Pair("other", Quantity.OTHER)
    )

    @Test
    fun testToString() {
        for ((first, second) in quantityPairs) {
            assertEquals(first, second.toString())
        }
    }
}
