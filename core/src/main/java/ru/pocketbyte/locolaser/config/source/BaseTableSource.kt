/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.utils.LogUtils
import ru.pocketbyte.locolaser.utils.TextUtils

import java.util.ArrayList

/**
 * @author Denis Shurygin
 */
abstract class BaseTableSource(sourceConfig: BaseTableSourceConfig) : Source(sourceConfig) {

    abstract val firstRow: Int
    abstract val rowsCount: Int
    abstract fun getValue(col: Int, row: Int): String?
    abstract val columnIndexes: ColumnIndexes

    override val sourceConfig: BaseTableSourceConfig
        get() = super.sourceConfig as BaseTableSourceConfig

    override fun read(): Source.ReadResult {

        val items = ResMap()
        val missedValues = ArrayList<Source.MissedValue>()

        var row = firstRow
        while (rowsCount >= row) {
            val key = getValue(columnIndexes.key, row)
            if (key?.isNotEmpty() == true) {
                var comment: String? = null
                if (columnIndexes.comment > 0)
                    comment = getValue(columnIndexes.comment, row)

                for (locale in sourceConfig.locales) {

                    val localeCol = columnIndexes.locales[locale] ?: -1

                    if (localeCol >= 0) {
                        if (!items.containsKey(locale))
                            items.put(locale, ResLocale())

                        val itemMap = items[locale]

                        val value = getValue(localeCol, row)

                        var quantity = Quantity.OTHER
                        if (columnIndexes.quantity > 0)
                            quantity = Quantity.fromString(getValue(columnIndexes.quantity, row))

                        if (value?.isNotEmpty() == true) {

                            var item: ResItem? = itemMap?.get(key)
                            if (item == null) {
                                item = ResItem(key)
                                itemMap?.put(item)
                            }

                            val resValue = ResValue(sourceValueToValue(value), comment, quantity)
                            resValue.location = CellLocation(this, localeCol, row)
                            item.addValue(resValue)
                        } else {
                            LogUtils.warn("\rValue not found! Locale= $locale, key= $key.")
                            missedValues.add(Source.MissedValue(key, locale, quantity,
                                    CellLocation(this, localeCol, row)))
                        }
                    }
                }
            }
            row++
        }

        return Source.ReadResult(items, missedValues)
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

    class ColumnIndexes(val key: Int, val quantity: Int, val comment: Int, val locales: Map<String, Int>) {

        val max: Int
        val min: Int

        init {

            var max = -1
            var min = -1
            for (index in locales.values) {
                max = Math.max(max, index)
                min = min(min, index)
            }
            this.max = Math.max(Math.max(max, Math.max(key, comment)), 1)
            this.min = Math.max(min(min, min(key, comment)), 1)
        }

        private fun min(a: Int, b: Int): Int {
            return if (b >= 0) {
                if (a == -1)
                    b
                else
                    Math.min(a, b)
            } else a
        }
    }
}
