/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.entity

/**
 * @author Denis Shurygin
 */
enum class Quantity {

    ZERO, ONE, TWO, FEW, MANY, OTHER;

    override fun toString(): String {
        return when (this) {
            ZERO -> "zero"
            ONE -> "one"
            TWO -> "two"
            FEW -> "few"
            MANY -> "many"
            else -> "other"
        }
    }
}
