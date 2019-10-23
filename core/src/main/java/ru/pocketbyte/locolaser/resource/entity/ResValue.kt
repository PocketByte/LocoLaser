/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import ru.pocketbyte.locolaser.config.source.Source

/**
 * @author Denis Shurygin
 */
class ResValue(
        val value: String,
        /** Resource comment. */
        val comment: String?,
        val quantity: Quantity = Quantity.OTHER,
        val meta: Map<String, String>? = null
) {
    /**
     * Source location where placed this resource item.
     */
    var location: Source.ValueLocation? = null

    override fun equals(other: Any?): Boolean {
        if (other is ResValue) {
            return isStringEquals(value, other.value) &&
                    isStringEquals(comment, other.comment) &&
                    quantity === other.quantity && (
                        (metaIsEmpty() && other.metaIsEmpty()) ||
                        meta?.equals(other.meta) ?: false
                    )

        }
        return super.equals(other)
    }

    private fun isStringEquals(string1: String?, string2: String?): Boolean {
        return string1 != null && string1 == string2 || string1 == null && string2 == null
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + quantity.hashCode()
        result = 31 * result + (location?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ResValue{" +
                "v=$value," +
                "c=$comment," +
                "q=$quantity," +
                "meta=$meta}"
    }
}

fun ResValue.metaIsEmpty(): Boolean {
    return this.meta == null || this.meta.isEmpty()
}

fun ResValue.metaIsNotEmpty(): Boolean {
    return !this.metaIsEmpty()
}

fun ResValue?.merge(item: ResValue?): ResValue? {
    if (this == null) return item
    if (item == null) return this

    if (this.metaIsNotEmpty()) {
        return if (item.metaIsNotEmpty()) {
            ResValue(item.value, item.comment, item.quantity,
                    this.meta?.toMutableMap()?.apply { item.meta?.let { putAll(it) } })
        } else {
            ResValue(item.value, item.comment, item.quantity, this.meta)
        }
    }
    return item
}