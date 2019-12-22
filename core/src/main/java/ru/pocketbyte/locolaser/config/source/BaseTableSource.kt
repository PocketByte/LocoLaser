/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.utils.LogUtils
import ru.pocketbyte.locolaser.utils.PluralUtils
import java.lang.StringBuilder

import java.util.ArrayList
import kotlin.math.max

/**
 * @author Denis Shurygin
 */
abstract class BaseTableSource(sourceConfig: BaseTableSourceConfig) : Source(sourceConfig) {

    companion object {

        fun parseMeta(metaString: String?): Map<String, String>? {
            if (metaString == null || metaString.isBlank()) {
                return null
            } else {
                val metadata = mutableMapOf<String, String>()
                metaString.split(";").forEach {
                    val metaParts = it.split("=")
                    if (metaParts.count() == 2) {
                        metadata[metaParts[0].trim()] = metaParts[1].trim()
                    }
                }
                return if (metadata.isNotEmpty()) metadata else null
            }
        }
    }

    abstract val firstRow: Int
    abstract val rowsCount: Int
    abstract fun getValue(col: Int, row: Int): String?
    abstract val columnIndexes: ColumnIndexes

    override val sourceConfig: BaseTableSourceConfig
        get() = super.sourceConfig as BaseTableSourceConfig

    override fun read(): ReadResult {

        val items = ResMap()
        val missedValues = ArrayList<MissedValue>()

        var row = firstRow
        while (rowsCount >= row) {
            val key = getValue(columnIndexes.key, row)
            if (key?.isNotEmpty() == true) {
                val comment: String? = if (columnIndexes.comment > 0) {
                    getValue(columnIndexes.comment, row)
                } else { null }

                val metadata = if (columnIndexes.metadata > 0) {
                    parseMeta(getValue(columnIndexes.metadata, row))
                } else { null }

                for (locale in sourceConfig.locales) {

                    val localeCol = columnIndexes.locales[locale] ?: -1

                    if (localeCol >= 0) {
                        if (!items.containsKey(locale))
                            items[locale] = ResLocale()

                        val itemMap = items[locale]

                        val value = getValue(localeCol, row)

                        var quantity = Quantity.OTHER
                        if (columnIndexes.quantity > 0)
                            quantity = PluralUtils.quantityFromString(
                                    getValue(columnIndexes.quantity, row), locale) ?: quantity

                        if (value?.isNotEmpty() == true) {

                            var item: ResItem? = itemMap?.get(key)
                            if (item == null) {
                                item = ResItem(key)
                                itemMap?.put(item)
                            }

                            val resValue = ResValue(sourceValueToValue(value), comment, quantity, metadata)
                            resValue.location = CellLocation(this, localeCol, row)
                            item.addValue(resValue)
                        } else {
                            LogUtils.warn("\rValue not found! Locale= $locale, key= $key.")
                            missedValues.add(MissedValue(key, locale, quantity,
                                    CellLocation(this, localeCol, row)))
                        }
                    }
                }
            }
            row++
        }

        if (items[PlatformResources.BASE_LOCALE] == null) {
            val firstLocale = columnIndexes.locales.entries.find { it.value >= 0 && items[it.key] != null }?.key
            if (firstLocale != null)
                items[PlatformResources.BASE_LOCALE] = ResLocale(items[firstLocale])
        }

        return ReadResult(items, missedValues)
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

    class CellLocation(source: Source, val col: Int, val row: Int) : Source.ValueLocation(source)

    class ColumnIndexes(
            val key: Int,
            val quantity: Int,
            val comment: Int,
            val locales: Map<String, Int>,
            val metadata: Int) {

        val max: Int
        val min: Int

        init {

            var max = -1
            var min = -1
            for (index in locales.values) {
                max = max(max, index)
                min = min(min, index)
            }
            this.max = max(max(max, max(max(key, comment), metadata)), 1)
            this.min = max(min(min, min(min(key, comment), metadata)), 1)
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
                append("locales=[").append(locales.entries.joinToString { "${it.key}:${it.value}" }).append("], ")
                append("metadata=").append(metadata).append("")
            }.toString()
        }
    }
}
