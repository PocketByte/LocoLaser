/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.google.sheet

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest
import com.google.api.services.sheets.v4.model.ValueRange
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.BaseTableResources
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.summary.FileSummary
import ru.pocketbyte.locolaser.utils.LogUtils
import java.io.IOException

/**
 * @author Denis Shurygin
 */
class GoogleSheet(
        private val sourceConfig: GoogleSheetConfig,
        private val service: Sheets,
        override val formattingType: FormattingType
) : BaseTableResources() {

    companion object {

        const val IGNORE_INDEX = "-"

        fun valueToSourceValue(value: String): String {
            // Add "'" if string beginning from "'", "+" or "="
            return value.replace("^(['+=])".toRegex(), "'$1")
        }

        fun sourceValueToValue(sourceValue: String): String {
            // Remove "'" from the beginning
            return sourceValue.replace("^(')".toRegex(), "")
        }
    }

    private var mIgnoreRows: List<Int>? = null
    private lateinit var mColumnIndexes: ColumnIndexes

    private var mTitleRow = -1

    private var mQuery: List<List<Any>>? = null

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(0, System.currentTimeMillis().toString()) // FIXME use sheet update time
    }

    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        if (fetchCellsIfNeeded()) {
            var totalRows = rowsCount
            val batchRequest = mutableListOf<ValueRange>()
            val newRows = ArrayList<Pair<String, Quantity>>()

            for ((locale, resLocale) in resMap) {
                val localeColumn = mColumnIndexes.indexesMap[locale]
                if (localeColumn != null && localeColumn >= 0) {
                    for ((_, resItem) in resLocale) {
                        for (i in resItem.values.indices) {
                            val resValue = formattingType.convert(resItem.values[i])
                            val resRow = getRow(resItem.key, resValue.quantity)
                            // =====================================
                            // Prepare batch for found missed resMap
                            if (resRow != null) {
                                if (getValue(localeColumn, resRow)?.isBlank() != false) {
                                    batchRequest.add(
                                            ValueRange()
                                                    .setRange("${columnName(localeColumn)}$resRow")
                                                    .setValues(listOf(listOf(valueToSourceValue(resValue.value))))
                                    )

                                    val comment = resValue.comment
                                    if (comment != null && mColumnIndexes.comment >= 0) {
                                        batchRequest.add(
                                                ValueRange()
                                                        .setRange("${columnName(mColumnIndexes.comment)}$resRow")
                                                        .setValues(listOf(listOf(valueToSourceValue(comment))))
                                        )
                                    }
                                }
                            } else {
                                // =====================================
                                // Reserve rows for new keys
                                if (getRow(resItem.key, resValue.quantity) == null) {
                                    registerRowForKey(++totalRows, resItem.key, resValue.quantity)
                                    newRows.add(Pair(resItem.key, resValue.quantity))
                                }
                            }
                        }
                    }
                }
            }

            service.spreadsheets()
                .values()
                .batchUpdate(
                    sourceConfig.id,
                    BatchUpdateValuesRequest()
                        .setValueInputOption("RAW")
                        .setData(batchRequest)
                )
                .execute()
                .let {
                    LogUtils.info("${it.totalUpdatedCells} cells updated.")
                }

            if (totalRows > rowsCount) {
                val newKeysList = mutableListOf<String>()
                val newValuesBatch = mutableListOf<ValueRange>()

                for ((newRowKey, newRowQuantity) in newRows) {
                    getRow(newRowKey, newRowQuantity)?.let { row ->
                        // Key Cell

                        newKeysList.add(newRowKey)

                        val resItem = resMap[Resources.BASE_LOCALE]?.get(newRowKey)

                        // Quantity Cell
                        if (mColumnIndexes.quantity >= 0) {
                            if (resItem?.isHasQuantities != false) {
                                newValuesBatch.add(
                                    ValueRange()
                                    .setRange("${columnName(mColumnIndexes.quantity)}$row")
                                    .setValues(listOf(listOf(newRowQuantity.toString())))
                                )
                            }
                        }

                        // Comment Cell
                        val comment = resItem?.valueForQuantity(newRowQuantity)?.comment
                        if (comment != null && mColumnIndexes.comment >= 0) {
                            newValuesBatch.add(
                                ValueRange()
                                    .setRange("${columnName(mColumnIndexes.comment)}$row")
                                    .setValues(listOf(listOf(valueToSourceValue(comment))))
                            )
                        }

                        // Value Cells
                        for ((locale, resLocale) in resMap) {
                            val localeColumn = mColumnIndexes.indexesMap[locale]
                            if (localeColumn != null && localeColumn >= 0) {
                                resLocale[newRowKey]?.valueForQuantity(newRowQuantity)?.let {
                                    val resValue = formattingType.convert(it)
                                    newValuesBatch.add(
                                        ValueRange()
                                            .setRange("${columnName(localeColumn)}$row")
                                            .setValues(listOf(listOf(valueToSourceValue(resValue.value))))
                                    )
                                }
                            }
                        }
                    } ?: { // getRow() equal null
                        //TODO warn
                    }()
                }

                try {
                    val firstNewRow = newRows[0].let {
                        getRow(it.first, it.second)
                    }

                    // Appending keys
                    service.spreadsheets().values()
                        .append(
                            sourceConfig.id,
                            "${columnName(mColumnIndexes.key)}${firstNewRow}",
                            ValueRange().setValues(listOf(newKeysList)).setMajorDimension("COLUMNS")
                        )
                        .setValueInputOption("RAW")
                        .execute()
                        .let {
                            LogUtils.info("${it.updates.updatedCells} new rows inserted.")
                        }

                    // Updating values
                    service.spreadsheets()
                        .values()
                        .batchUpdate(
                            sourceConfig.id,
                            BatchUpdateValuesRequest()
                                .setValueInputOption("RAW")
                                .setData(newValuesBatch)
                        )
                        .execute()
                        .let {
                            LogUtils.info("${it.totalUpdatedCells} cells updated.")
                        }
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + sourceConfig.id!!)
                }

                // Invalidate Query
                mQuery = null
            }
        }
    }

    override val firstRow: Int
        get() {
            fetchCellsIfNeeded()
            return mTitleRow + 1
        }

    override val columnIndexes: ColumnIndexes
        get() {
            fetchCellsIfNeeded()
            return mColumnIndexes
        }

    override fun getValue(col: Int, row: Int): String? {
        fetchCellsIfNeeded()
        if (mIgnoreRows == null || mIgnoreRows!!.contains(row))
            return null

        return mQuery
                ?.getOrNull(row - mTitleRow - 1)
                ?.getOrNull(col - columnIndexes.min)
                ?.toString()
    }

    override val rowsCount: Int
        get() {
            fetchCellsIfNeeded()
            return (mQuery?.size ?: 0) + firstRow - 1
        }

    override fun valueToSourceValue(value: String): String {
        return Companion.valueToSourceValue(value)
    }

    override fun sourceValueToValue(sourceValue: String): String {
        return Companion.sourceValueToValue(sourceValue)
    }

    private fun fetchCellsIfNeeded(): Boolean {
        if (mQuery == null) {
            val workSheet = sourceConfig.worksheetTitle?.let { "$it!" } ?: ""

            // Ignore rows
            val ignoreRows = ArrayList<Int>()
            val indexColumnFeed: ValueRange?
            try {
                indexColumnFeed = service.spreadsheets().values()
                        .get(sourceConfig.id, "${workSheet}A:A")
                        .execute()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

            indexColumnFeed?.getValues()?.forEachIndexed { row, rowValues ->
                if (IGNORE_INDEX == rowValues.getOrElse(0) { "" }) {
                    ignoreRows.add(row + 1)
                }
            }
            mIgnoreRows = ignoreRows

            // Title row
            var titleRow = 1
            while (mIgnoreRows!!.contains(titleRow))
                titleRow++
            mTitleRow = titleRow
            LogUtils.info("Title row: $mTitleRow")

            val titleRowFeed: ValueRange?
            try {
                titleRowFeed = service.spreadsheets().values()
                        .get(sourceConfig.id, "${workSheet}$titleRow:$titleRow")
                        .execute()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

            val indexesMap = titleRowFeed?.toIndexesMap()

            val key = indexesMap?.get(sourceConfig.keyColumn)
            if (key == null) {
                LogUtils.warn("Column " + sourceConfig.keyColumn + " not found.")
                return false
            }

            val quantity = indexesMap[sourceConfig.quantityColumn] ?: -1
            if (quantity == -1 && sourceConfig.quantityColumn != null) {
                LogUtils.warn("Column ${sourceConfig.quantityColumn} not found.")
            }

            val comment = indexesMap[sourceConfig.commentColumn] ?: -1
            if (comment == -1 && sourceConfig.commentColumn != null) {
                LogUtils.warn("Column ${sourceConfig.commentColumn} not found.")
            }

            val metadata = indexesMap[sourceConfig.metadataColumn] ?: -1
            if (comment == -1 && sourceConfig.metadataColumn != null) {
                LogUtils.warn("Column ${sourceConfig.metadataColumn} not found.")
            }

            mColumnIndexes = ColumnIndexes(key, quantity, comment, metadata, indexesMap)
            LogUtils.info("Column Indexes: $mColumnIndexes")

            try {
                val minColumn = columnName(mColumnIndexes.min)
                val maxColumn = columnName(mColumnIndexes.max)
                mQuery = service.spreadsheets().values()
                        .get(sourceConfig.id, "${workSheet}$minColumn${titleRow + 1}:$maxColumn")
                        .execute()
                        .getValues()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return mQuery != null
    }

    private fun ValueRange.toIndexesMap(): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        getValues().getOrNull(0)?.forEachIndexed { index, value ->
            result[value.toString()] = index + 1
        }
        return result
    }

    private fun columnName(column: Int): String {
        return if (column < 26) {
            (64 + column).toChar().toString()
        } else {
            columnName(column / 26) + (64 + column % 26).toChar()
        }
    }

}
