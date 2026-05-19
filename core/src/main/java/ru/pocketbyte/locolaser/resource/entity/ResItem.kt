/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.utils.LogUtils

import java.util.ArrayList
import java.util.Collections

/**
 * Single resource item that contains a key and a list of values (one per plural quantity).
 *
 * @author Denis Shurygin
 */
class ResItem(
    /** Resource key. */
    val key: String
) {

    private val mutableValues = ArrayList<ResValue>(1)

    /** The list of [ResValue] instances for this item, one per plural quantity. */
    val values: List<ResValue> = Collections.unmodifiableList(mutableValues)


    constructor(item: ResItem) : this(item.key) {
        mutableValues.addAll(item.mutableValues)
    }

    /**
     * Adds [value] to this item. If a value with the same quantity already exists, it is replaced.
     * @param value The value to add.
     * @return true if no duplicate was found, false if an existing value was replaced.
     */
    fun addValue(value: ResValue): Boolean {
        var isHasNoError = true

        val oldValue = valueForQuantity(value.quantity)
        if (oldValue != null) {
            isHasNoError = false
            LogUtils.err("Duplicate quantity! Key= ${this.key} Quantity=${value.quantity}")
            mutableValues.remove(oldValue)
        }
        mutableValues.add(value)
        return isHasNoError
    }

    /**
     * Removes [value] from this item.
     * @return true if the value was present and removed, false otherwise.
     */
    fun removeValue(value: ResValue): Boolean {
        return mutableValues.remove(value)
    }

    /**
     * Removes and returns the [ResValue] with the given [quantity], or null if not found.
     */
    fun removeValueForQuantity(quantity: Quantity): ResValue? {
        for (i in mutableValues.indices) {
            val resValue = mutableValues[i]
            if (resValue.quantity == quantity) {
                return mutableValues.removeAt(i)
            }
        }
        return null
    }

    /**
     * Returns the [ResValue] with the given [quantity], or null if not found.
     */
    fun valueForQuantity(quantity: Quantity): ResValue? {
        for (resValue in mutableValues) {
            if (resValue.quantity == quantity) {
                return resValue
            }
        }
        return null
    }

    /**
     * True if this item has multiple values or its single value has a quantity other than [Quantity.OTHER].
     */
    val isHasQuantities: Boolean
        get() = mutableValues.size > 1 || mutableValues.size == 1 && mutableValues[0].quantity !== Quantity.OTHER

    /**
     * Merges the values from [item] into this item, replacing existing values for the same quantity.
     * @return This item after merging.
     */
    fun merge(item: ResItem?): ResItem {
        if (item != null) {
            for (value2 in item.mutableValues) {
                val value1 = this.valueForQuantity(value2.quantity)
                if (value1 != null) {
                    this.removeValue(value1)
                }
                value1.merge(value2)?.let { this.addValue(it) }
            }
        }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is ResItem) {
            if (other.key == key && other.mutableValues.size == mutableValues.size) {
                return other.values.none { it != valueForQuantity(it.quantity) }
            }
        }
        return super.equals(other)
    }

    override fun toString(): String {
        if (isHasQuantities) {
            val valuesString = values.joinToString(",") {
                it.toString()
            }
            return "[$valuesString]"
        }
        return values[0].toString()
    }
}
