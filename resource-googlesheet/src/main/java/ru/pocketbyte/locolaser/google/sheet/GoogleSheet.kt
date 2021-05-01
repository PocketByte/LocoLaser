/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.google.sheet

import com.google.gdata.data.ILink
import com.google.gdata.data.batch.BatchOperationType
import com.google.gdata.data.batch.BatchUtils
import com.google.gdata.data.spreadsheet.CellEntry
import com.google.gdata.data.spreadsheet.CellFeed
import com.google.gdata.util.ServiceException
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.BaseTableResources
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary
import ru.pocketbyte.locolaser.utils.LogUtils

import java.io.IOException
import java.net.URL
import kotlin.collections.ArrayList

/**
 * @author Denis Shurygin
 */
class GoogleSheet(
        private val sourceConfig: GoogleSheetConfig,
        private val worksheetFacade: WorksheetFacade,
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
    private var mRowsCount = 0

    private var mQuery: CellFeed? = null

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(0, worksheetFacade.sheetEntry.updated.value.toString())
    }

    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        if (fetchCellsIfNeeded()) {
            var totalRows = mRowsCount
            val batchRequest = CellFeed()
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
                                if (getValue(localeColumn, resRow)?.isBlank() == true) {
                                    CellEntry(getCell(localeColumn, resRow)!!).let { batchEntry ->
                                        batchEntry.changeInputValueLocal(valueToSourceValue(resValue.value))
                                        BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                        batchRequest.entries.add(batchEntry)
                                    }

                                    val comment = resValue.comment
                                    if (comment != null && mColumnIndexes.comment >= 0) {
                                        CellEntry(getCell(mColumnIndexes.comment, resRow)!!).let { batchEntry ->
                                            batchEntry.changeInputValueLocal(valueToSourceValue(comment))
                                            BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                            batchRequest.entries.add(batchEntry)
                                        }
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

            try {
                mQuery?.getLink(ILink.Rel.FEED_BATCH, ILink.Type.ATOM)?.let { batchLink ->
                    worksheetFacade.batch(URL(batchLink.href), batchRequest)
                }

                if (totalRows > worksheetFacade.rowCount) {
                    worksheetFacade.rowCount = totalRows
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + sourceConfig.id)
            } catch (e: ServiceException) {
                e.printStackTrace()
                throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + sourceConfig.id)
            }

            if (totalRows > mRowsCount) {
                val cellFeed: CellFeed = try {
                    worksheetFacade.queryRange(
                        mColumnIndexes.min,
                        mRowsCount + 1,
                        mColumnIndexes.max,
                        totalRows, true
                    ) ?: throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + sourceConfig.id)
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + sourceConfig.id)
                } catch (e: ServiceException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + sourceConfig.id)
                }

                for ((newRowKey, newRowQuantity) in newRows) {
                    getRow(newRowKey, newRowQuantity)?.let { row ->
                        // Key Cell
                        CellEntry(cellFeed.entries[
                            entryIndex(mColumnIndexes.key, row, mRowsCount)
                        ]).let { batchEntry ->
                            batchEntry.changeInputValueLocal(newRowKey)
                            BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                            batchRequest.entries.add(batchEntry)
                        }

                        val resItem = resMap[Resources.BASE_LOCALE]?.get(newRowKey)

                        // Quantity Cell
                        if (mColumnIndexes.quantity >= 0) {
                            CellEntry(cellFeed.entries[
                                entryIndex(mColumnIndexes.quantity, row, mRowsCount)
                            ]).let { batchEntry ->
                                if (resItem?.isHasQuantities != false)
                                    batchEntry.changeInputValueLocal(newRowQuantity.toString())
                                else
                                    batchEntry.changeInputValueLocal("")
                                BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                batchRequest.entries.add(batchEntry)
                            }
                        }

                        // Comment Cell
                        val comment = resItem?.valueForQuantity(newRowQuantity)?.comment
                        if (comment != null && mColumnIndexes.comment >= 0) {
                            CellEntry(cellFeed.entries[
                                entryIndex(mColumnIndexes.comment, row, mRowsCount)
                            ]).let { batchEntry ->
                                batchEntry.changeInputValueLocal(valueToSourceValue(comment))
                                BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                batchRequest.entries.add(batchEntry)
                            }
                        }

                        // Value Cells
                        for ((locale, resLocale) in resMap) {
                            val localeColumn = mColumnIndexes.indexesMap[locale]
                            if (localeColumn != null && localeColumn >= 0) {
                                resLocale[newRowKey]?.valueForQuantity(newRowQuantity)?.let {
                                    val resValue = formattingType.convert(it)
                                    CellEntry(cellFeed.entries[
                                        entryIndex(localeColumn, row, mRowsCount)
                                    ]).let { batchEntry ->
                                        batchEntry.changeInputValueLocal(valueToSourceValue(resValue.value))
                                        BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                        batchRequest.entries.add(batchEntry)
                                    }
                                }
                            }
                        }
                    } ?: { // getRow() equal null
                        //TODO warn
                    }()
                }

                try {
                    val batchLink = cellFeed.getLink(ILink.Rel.FEED_BATCH, ILink.Type.ATOM)
                    worksheetFacade.batch(URL(batchLink.href), batchRequest)
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + sourceConfig.id!!)
                } catch (e: ServiceException) {
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

        val cell = getCell(col, row)
        return cell?.cell?.value
    }

    override val rowsCount: Int
        get() {
            fetchCellsIfNeeded()
            return mRowsCount
        }

    override fun valueToSourceValue(value: String): String {
        return Companion.valueToSourceValue(value)
    }

    override fun sourceValueToValue(sourceValue: String): String {
        return Companion.sourceValueToValue(sourceValue)
    }

    private fun fetchCellsIfNeeded(): Boolean {
        if (mQuery == null) {
            // Ignore rows
            val ignoreRows = ArrayList<Int>()
            val indexColumnFeed: CellFeed?
            try {
                indexColumnFeed = worksheetFacade.queryRange(1, -1, 1, -1, false)
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } catch (e: ServiceException) {
                e.printStackTrace()
                return false
            }

            if (indexColumnFeed != null) {
                for (cell in indexColumnFeed.entries) {
                    if (IGNORE_INDEX == cell.cell.value)
                        ignoreRows.add(cell.cell.row)
                }
            }
            mIgnoreRows = ignoreRows

            // Title row
            var titleRow = 1
            while (mIgnoreRows!!.contains(titleRow))
                titleRow++
            mTitleRow = titleRow
            LogUtils.info("Title row: $mTitleRow")

            val titleRowFeed: CellFeed?
            try {
                titleRowFeed = worksheetFacade.queryRange(-1, mTitleRow, -1, mTitleRow, false)
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } catch (e: ServiceException) {
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

            mRowsCount = try {
                val list = worksheetFacade
                        .queryRange(mColumnIndexes.key, mTitleRow + 1, mColumnIndexes.key, -1, false)!!
                        .entries
                if (list.size > 0)
                    list[list.size - 1].cell.row
                else
                    mTitleRow
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } catch (e: ServiceException) {
                e.printStackTrace()
                return false
            }

            try {
                mQuery = worksheetFacade
                        .queryRange(mColumnIndexes.min, mTitleRow + 1, mColumnIndexes.max, mRowsCount, true)
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ServiceException) {
                e.printStackTrace()
            }

        }
        return mQuery != null
    }

    private fun getCell(col: Int, row: Int): CellEntry? {
        if (fetchCellsIfNeeded()) {
            val index = entryIndex(col, row, mTitleRow)
            if (index >= 0 && index < mQuery!!.entries.size)
                return mQuery!!.entries[index]
        }
        return null
    }

    private fun entryIndex(col: Int, row: Int, top: Int): Int {
        return (row - top - 1) * (mColumnIndexes.max - mColumnIndexes.min + 1) + col - mColumnIndexes.min
    }

    private fun CellFeed.toIndexesMap(): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        this.entries.forEach {
            result[it.cell.value] = it.cell.col
        }
        return result
    }

}
