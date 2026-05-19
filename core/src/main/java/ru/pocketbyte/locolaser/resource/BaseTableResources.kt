/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.LogUtils
import ru.pocketbyte.locolaser.utils.PluralUtils
import kotlin.math.max

/**
 * Abstract base implementation of [Resources] for table-based resource sources.
 *
 * @author Denis Shurygin
 */
abstract class BaseTableResources : Resources {

    companion object {

        /**
         * Parses a metadata string in `"key=value;key=value"` format into a map.
         * @param metaString The raw metadata string, or null.
         * @return A map of key-value pairs, or null if the string is null or blank.
         */
        fun parseMeta(metaString: String?): Map<String, String>? {
            return if (!metaString.isNullOrBlank()) {
                val metadata = mutableMapOf<String, String>()
                metaString.split(";").forEach {
                    val metaParts = it.split("=")
                    if (metaParts.count() == 2) {
                        metadata[metaParts[0].trim()] = metaParts[1].trim()
                    }
                }
                metadata.ifEmpty { null }
            } else null
        }
    }

    /** The index of the first data row in the table. */
    abstract val firstRow: Int

    /** The total number of rows in the table. */
    abstract val rowsCount: Int

    /**
     * Returns the cell value at the given column and row, or null if the cell is empty.
     * @param col The column index.
     * @param row The row index.
     */
    abstract fun getValue(col: Int, row: Int): String?

    /** Column index mappings for key, quantity, comment, metadata, and locale columns. */
    abstract val columnIndexes: ColumnIndexes

    private var keysRows: MutableMap<String, MutableMap<Quantity, Int>>? = null

    /**
     * Returns the row index for the given resource key and quantity, or null if not found.
     * @param key The resource key.
     * @param quantity The plural quantity.
     */
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

    /**
     * Reads resources from the table for the given locales.
     * @param locales The set of locale identifiers to read, or null to read all available locales.
     * @param extraParams Additional parameters passed to the read operation, or null for defaults.
     * @return A [ResMap] containing the read resources, or null if no resources were found.
     */
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
     * Converts a common value to the source-specific format before writing to the table.
     * @param value The value in common format.
     * @return The value converted to source format.
     */
    open fun valueToSourceValue(value: String): String {
        return value
    }

    /**
     * Converts a source-specific value to the common format after reading from the table.
     * @param sourceValue The value in source format.
     * @return The value converted to common format.
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

    /**
     * Holds column index mappings for key, quantity, comment, metadata, and locale columns.
     */
    class ColumnIndexes(
            /** Column index for the resource key column. */
            val key: Int,
            /** Column index for the plural quantity column. */
            val quantity: Int,
            /** Column index for the comment column. */
            val comment: Int,
            /** Column index for the metadata column. */
            val metadata: Int,
            /** Map from locale identifier to its column index. */
            val indexesMap: Map<String, Int>

    ) {

        /** Maximum column index among all locale columns, or -1 if none. */
        val max: Int

        /** Minimum column index among all locale columns, or -1 if none. */
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
