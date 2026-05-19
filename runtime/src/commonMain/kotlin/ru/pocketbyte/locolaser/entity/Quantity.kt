/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.entity

/**
 * Represents the CLDR plural quantity categories used for plural string selection.
 *
 * Each value corresponds to a plural form defined by the
 * [Unicode CLDR Plural Rules](https://cldr.unicode.org/index/cldr-spec/plural-rules).
 * The appropriate category for a given number is locale-dependent.
 */
enum class Quantity {

    /** Matches the number zero (e.g., Arabic). */
    ZERO,

    /** Matches the singular form (e.g., English: 1). */
    ONE,

    /** Matches the dual form (e.g., Arabic, Welsh: 2). */
    TWO,

    /** Matches a small plural form (e.g., Slavic languages: 2–4). */
    FEW,

    /** Matches a large or special plural form (e.g., Arabic: 11–99). */
    MANY,

    /** The general plural form used as a fallback in all locales. */
    OTHER;

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
