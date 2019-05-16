/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import ru.pocketbyte.locolaser.utils.LogUtils

import java.util.ArrayList
import java.util.Collections

/**
 * Single resource item that contain key, value and comment.
 *
 * @author Denis Shurygin
 */
class ResItem(
    /** Resource key. */
    val key: String
) {

    private val mutableValues = ArrayList<ResValue>(1)

    /** Resource values. */
    val values: List<ResValue> = Collections.unmodifiableList(mutableValues)!!


    constructor(item: ResItem) : this(item.key) {
        mutableValues.addAll(item.mutableValues)
    }

    //TODO docs
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

    //TODO docs
    fun removeValue(value: ResValue): Boolean {
        return mutableValues.remove(value)
    }

    //TODO docs
    fun removeValueForQuantity(quantity: Quantity): ResValue? {
        for (i in mutableValues.indices) {
            val resValue = mutableValues[i]
            if (resValue.quantity == quantity) {
                return mutableValues.removeAt(i)
            }
        }
        return null
    }

    //TODO docs
    fun valueForQuantity(quantity: Quantity): ResValue? {
        for (resValue in mutableValues) {
            if (resValue.quantity == quantity) {
                return resValue
            }
        }
        return null
    }

    //TODO docs
    val isHasQuantities: Boolean
        get() = mutableValues.size > 1 || mutableValues.size == 1 && mutableValues[0].quantity !== Quantity.OTHER

    //TODO docs
    fun merge(item: ResItem?): ResItem {
        if (item != null) {
            for (value2 in item.mutableValues) {

                val value1 = this.valueForQuantity(value2.quantity)
                if (value1 != null) {
                    this.removeValue(value1)
                }
                this.addValue(value2)
            }
        }
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is ResItem) {
            val item = other as ResItem?
            if (item!!.key == key && item.mutableValues.size == mutableValues.size) {
                return item.values.none { it != valueForQuantity(it.quantity) }
            }
        }
        return super.equals(other)
    }
}
