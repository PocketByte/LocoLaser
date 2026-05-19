/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

/**
 * Represents a single localization value with its comment, plural quantity, formatting type, and metadata.
 *
 * @author Denis Shurygin
 */
class ResValue(
    /**
     * The localized string value.
     */
    val value: String,
    /**
     * An optional comment for this value.
     */
    val comment: String?,
    /**
     * The plural quantity this value corresponds to.
     */
    val quantity: Quantity = Quantity.OTHER,
    /**
     * The formatting type used to interpret formatting arguments in [value].
     */
    val formattingType: FormattingType = NoFormattingType,
    /**
     * The list of formatting arguments extracted from [value], or null if none.
     */
    val formattingArguments: List<FormattingArgument>? = null,
    /**
     * Additional metadata associated with this value, or null if none.
     */
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

fun ResValue.formatArgumentsIsNotEmpty(): Boolean {
    return !this.formatArgumentsIsEmpty()
}

/**
 * Merges this [ResValue] with [item], with [item]'s properties taking precedence.
 * @param item The value to merge with.
 * @return The merged [ResValue], or null if both are null.
 */
fun ResValue?.merge(item: ResValue?): ResValue? {
    if (this == null) return item
    if (item == null) return this

    val arguments: List<FormattingArgument>? = if (item.formattingArguments != null) {
        item.formattingArguments.mapIndexed { index, formattingArgument ->
            this.formattingArguments?.getOrNull(index).merge(formattingArgument) ?: formattingArgument
        }
    } else {
        null
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