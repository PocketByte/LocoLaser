/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.LogUtils
import ru.pocketbyte.locolaser.utils.PluralUtils
import kotlin.math.max

/**
 * @author Denis Shurygin
 */
abstract class BaseTableResources : Resources {

    companion object {

        fun parseMeta(metaString: String?): Map<String, String>? {
            return if (metaString != null && metaString.isNotBlank()) {
                val metadata = mutableMapOf<String, String>()
                metaString.split(";").forEach {
                    val metaParts = it.split("=")
                    if (metaParts.count() == 2) {
                        metadata[metaParts[0].trim()] = metaParts[1].trim()
                    }
                }
                if (metadata.isNotEmpty()) metadata else null
            } else null
        }
    }

    abstract val firstRow: Int
    abstract val rowsCount: Int
    abstract fun getValue(col: Int, row: Int): String?
    abstract val columnIndexes: ColumnIndexes

    private var keysRows: MutableMap<String, MutableMap<Quantity, Int>>? = null

    fun getRow(key: String, quantity: Quantity): Int? {
        if (this.keysRows == null) {
            val keysRows = mutableMapOf<String, MutableMap<Quantity, Int>>()

            for (row in firstRow..rowsCount) {
                val rowKey = getValue(columnIndexes.key, row)
                if (rowKey?.isNotEmpty() == true) {
                    val rowQuantity = getQuantity(row)
                    (keysRows[rowKey]
                        ?: mutableMapOf<Quantity, Int>().apply { keysRows[rowKey] = this }
                    )[rowQuantity] = row
                }
            }
            this.keysRows = keysRows
        }
        return keysRows?.get(key)?.get(quantity)
    }

    protected fun registerRowForKey(row: Int, key: String, quantity: Quantity) {
        if (this.keysRows == null) {
            this.keysRows = mutableMapOf()
        }

        val quantityMap = keysRows?.get(key)
                ?: mutableMapOf<Quantity, Int>().apply { keysRows?.put(key, this) }

        quantityMap[quantity] = row
    }

    override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap? {
        val items = ResMap()

        val keysRows = mutableMapOf<String, MutableMap<Quantity, Int>>()
        var row = firstRow
        while (rowsCount >= row) {
            val key = getValue(columnIndexes.key, row)
            val quantity = getQuantity(row)
            if (key?.isNotEmpty() == true) {
                (keysRows[key]
                    ?: mutableMapOf<Quantity, Int>().apply { keysRows[key] = this }
                )[quantity] = row
                val comment: String? = if (columnIndexes.comment > 0) {
                    getValue(columnIndexes.comment, row)
                } else { null }

                val metadata = if (columnIndexes.metadata > 0) {
                    parseMeta(getValue(columnIndexes.metadata, row))
                } else { null }

                locales?.forEach { locale ->
                    val localeCol = columnIndexes.indexesMap[locale] ?: -1

                    if (localeCol >= 0) {
                        if (!items.containsKey(locale))
                            items[locale] = ResLocale()

                        val itemMap = items[locale]

                        val value = getValue(localeCol, row)

                        if (value?.isNotEmpty() == true) {

                            var item: ResItem? = itemMap?.get(key)
                            if (item == null) {
                                item = ResItem(key)
                                itemMap?.put(item)
                            }

                            val fixedValue = sourceValueToValue(value)
                            val formattingArguments = formattingType.argumentsFromValue(fixedValue)
                            val resValue = ResValue(
                                fixedValue, comment, quantity,
                                    if (formattingArguments?.isEmpty() != false) NoFormattingType
                                        else formattingType,
                                    formattingArguments, metadata
                            )
                            item.addValue(resValue)
                        } else {
                            LogUtils.warn("\rValue not found! Locale= $locale, key= $key.")
                        }
                    }
                }
            }
            row++
        }

        this.keysRows = keysRows

        if (items[Resources.BASE_LOCALE] == null) {
            val firstLocale = columnIndexes.indexesMap.entries.find {
                locales?.contains(it.key) ?: when(it.value) {
                    columnIndexes.key,
                    columnIndexes.quantity,
                    columnIndexes.comment,
                    columnIndexes.metadata -> false
                    else -> true
                }
            }?.key
            if (firstLocale != null)
                items[Resources.BASE_LOCALE] = ResLocale(items[firstLocale])
        }

        return items
    }


    /**
     * Converts common value to source value format
     * @param value Value that should be converted
     * @return Source value format
     */
    open fun valueToSourceValue(value: String): String {
        return value
    }

    /**
     * Converts source value to common value format
     * @param sourceValue Source value that should be converted
     * @return Value in common format
     */
    open fun sourceValueToValue(sourceValue: String): String {
        return sourceValue
    }

    private fun getQuantity(row: Int): Quantity {
        val quantity = if (columnIndexes.quantity > 0) {
            PluralUtils.quantityFromString(getValue(columnIndexes.quantity, row))
        } else null

        return quantity ?: Quantity.OTHER
    }

    class ColumnIndexes(
            val key: Int,
            val quantity: Int,
            val comment: Int,
            val metadata: Int,
            val indexesMap: Map<String, Int>

    ) {

        val max: Int
        val min: Int

        init {
            var max = -1
            var min = -1
            indexesMap.values.forEach {
                max = max(max, it)
                min = min(min, it)
            }
            this.max = max
            this.min = min
        }

        private fun min(a: Int, b: Int): Int {
            return if (b >= 0) {
                if (a == -1)
                    b
                else
                    Math.min(a, b)
            } else a
        }

        override fun toString(): String {
            return StringBuilder().apply {
                append("key=").append(key).append(", ")
                append("quantity=").append(quantity).append(", ")
                append("comment=").append(comment).append(", ")
                append("metadata=").append(metadata).append("")
                append("other=[").append(indexesMap.entries.filter {
                    when(it.value) {
                        key, quantity, comment, metadata -> false
                        else -> true
                    }
                }.joinToString { "${it.key}:${it.value}" }).append("], ")
            }.toString()
        }
    }
}
