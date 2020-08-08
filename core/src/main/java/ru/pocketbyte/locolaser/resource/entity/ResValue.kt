/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

/**
 * @author Denis Shurygin
 */
class ResValue(
        val value: String,
        /** Resource comment. */
        val comment: String?,
        val quantity: Quantity = Quantity.OTHER,
        val formattingType: FormattingType = NoFormattingType,
        val formattingArguments: List<FormattingArgument>? = null,
        val meta: Map<String, String>? = null
) {

    override fun equals(other: Any?): Boolean {
        if (other is ResValue) {
            return isStringEquals(value, other.value) &&
                    isStringEquals(comment, other.comment) &&
                    quantity === other.quantity &&
                    (
                        (metaIsEmpty() && other.metaIsEmpty()) ||
                        meta?.equals(other.meta) ?: false
                    ) &&
                    formattingType == other.formattingType &&
                    (
                        (formatArgumentsIsEmpty() && other.formatArgumentsIsEmpty()) ||
                        formattingArguments?.equals(other.formattingArguments) ?: false
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
        return result
    }

    override fun toString(): String {
        return "ResValue{" +
                "v=$value," +
                "c=$comment," +
                "q=$quantity," +
                "f=${formattingType.javaClass.simpleName}," +
                "fa=${formattingArguments?.joinToString { it.toString() }?.let { "[$it]" }}," +
                "meta=$meta}"
    }
}

fun ResValue.metaIsEmpty(): Boolean {
    return this.meta == null || this.meta.isEmpty()
}

fun ResValue.metaIsNotEmpty(): Boolean {
    return !this.metaIsEmpty()
}

fun ResValue.formatArgumentsIsEmpty(): Boolean {
    return this.formattingArguments == null || this.formattingArguments.isEmpty()
}

fun ResValue.formatParamsIsNotEmpty(): Boolean {
    return !this.formatArgumentsIsEmpty()
}

fun ResValue?.merge(item: ResValue?): ResValue? {
    if (this == null) return item
    if (item == null) return this

    val arguments: List<FormattingArgument>? = if (this.formattingArguments != null) {
        if (item.formattingArguments != null && this.formattingArguments.size == item.formattingArguments.size) {
            List(this.formattingArguments.size) {
                this.formattingArguments.getOrNull(it).merge(item.formattingArguments.getOrNull(it))!!
            }
        } else {
            this.formattingArguments
        }
    } else {
        item.formattingArguments
    }

    val meta = if (item.metaIsNotEmpty()) {
        if (this.metaIsNotEmpty()) {
            this.meta?.toMutableMap()?.apply { item.meta?.let { putAll(it) } }
        } else {
            item.meta
        }
    } else {
        this.meta
    }
    return ResValue(item.value, item.comment ?: this.comment, item.quantity, item.formattingType, arguments, meta)
}