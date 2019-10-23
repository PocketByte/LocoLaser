/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.source.BaseTableSource

import java.util.ArrayList
import java.util.HashMap

/**
 * @author Denis Shurygin
 */
class MockDataSet(locales: Array<String>) {

    private val mDataList = ArrayList<DataRow>()
    private val mLocalesCount: Int = locales.size
    val columnIndexes: BaseTableSource.ColumnIndexes

    init {
        val localeIndexes = HashMap<String, Int>(locales.size)
        for (i in 0 until mLocalesCount)
            localeIndexes[locales[i]] = 5 + i

        columnIndexes = BaseTableSource.ColumnIndexes(1, 2, 3, localeIndexes, 4)
    }

    fun add(key: String, quantity: String?, comment: String?, locales: Array<String?>, metadata: String?) {
        mDataList.add(DataRow(key, quantity, comment, locales, metadata))
    }

    operator fun get(col: Int, row: Int): String? {
        if (row < mDataList.size && row >= 0) {
            val dataRow = mDataList[row]
            when (col) {
                1 -> return dataRow.key
                2 -> return dataRow.quantity
                3 -> return dataRow.comment
                4 -> return dataRow.metadata
                else -> if (col >= 5 && col - 5 < dataRow.locales.size) {
                    return dataRow.locales[col - 5]
                }
            }
        }
        return null
    }

    fun size(): Int {
        return mDataList.size
    }

    private class DataRow(
            val key: String,
            val quantity: String?,
            val comment: String?,
            val locales: Array<String?>,
            val metadata: String?
    )
}