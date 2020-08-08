/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.sheet

import com.google.gdata.data.ILink
import com.google.gdata.data.batch.BatchOperationType
import com.google.gdata.data.batch.BatchUtils
import com.google.gdata.data.spreadsheet.CellEntry
import com.google.gdata.data.spreadsheet.CellFeed
import com.google.gdata.util.ServiceException
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.source.BaseTableSource
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.utils.LogUtils

import java.io.IOException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Denis Shurygin
 */
class GoogleSheet(
        private val mConfig: GoogleSheetConfig,
        private val mWorksheetFacade: WorksheetFacade
) : BaseTableSource(mConfig) {

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

        private fun findCellByValue(cellFeed: CellFeed?, value: String?): CellEntry? {
            if (value != null)
                for (cell in cellFeed!!.entries) {
                    if (value == cell.cell.value)
                        return cell
                }
            return null
        }
    }

    private var mIgnoreRows: List<Int>? = null
    private lateinit var mColumnIndexes: ColumnIndexes

    private var mTitleRow = -1
    private var mRowsCount = 0

    private var mQuery: CellFeed? = null

    override val modifiedDate = mWorksheetFacade.sheetEntry.updated.value

    override val formattingType: FormattingType = JavaFormattingType

    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        if (fetchCellsIfNeeded()) {
            var totalRows = mRowsCount
            val batchRequest = CellFeed()
            val newRows = ArrayList<Pair<String, Quantity>>()

            for ((locale, resLocale) in resMap) {
                val localeColumn = mColumnIndexes.locales[locale]
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
                    mWorksheetFacade.batch(URL(batchLink.href), batchRequest)
                }

                if (totalRows > mWorksheetFacade.rowCount) {
                    mWorksheetFacade.rowCount = totalRows
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id)
            } catch (e: ServiceException) {
                e.printStackTrace()
                throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id)
            }

            if (totalRows > mRowsCount) {
                val cellFeed: CellFeed = try {
                    mWorksheetFacade.queryRange(
                        mColumnIndexes.min,
                        mRowsCount + 1,
                        mColumnIndexes.max,
                        totalRows, true
                    ) ?: throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id)
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id)
                } catch (e: ServiceException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id)
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

                        val resItem = resMap[PlatformResources.BASE_LOCALE]?.get(newRowKey)

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
                            val localeColumn = mColumnIndexes.locales[locale]
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
                    mWorksheetFacade.batch(URL(batchLink.href), batchRequest)
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id!!)
                } catch (e: ServiceException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id!!)
                }

                // Invalidate Query
                mQuery = null
            }
        }
    }

    override fun close() {
        mQuery = null
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
                indexColumnFeed = mWorksheetFacade.queryRange(1, -1, 1, -1, false)
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

            val key: Int
            var quantity = -1
            var comment = -1
            val locales = HashMap<String, Int>()
            var metadata = -1

            val titleRowFeed: CellFeed?
            try {
                titleRowFeed = mWorksheetFacade.queryRange(-1, mTitleRow, -1, mTitleRow, false)
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } catch (e: ServiceException) {
                e.printStackTrace()
                return false
            }

            var cellEntry = findCellByValue(titleRowFeed, sourceConfig.keyColumn)
            if (cellEntry == null) {
                LogUtils.warn("Column " + sourceConfig.keyColumn + " not found.")
                return false
            }
            key = cellEntry.cell.col

            cellEntry = findCellByValue(titleRowFeed, sourceConfig.quantityColumn)
            if (cellEntry != null) {
                quantity = cellEntry.cell.col
            } else if (sourceConfig.quantityColumn != null) {
                LogUtils.warn("Column ${sourceConfig.quantityColumn} not found.")
            }

            cellEntry = findCellByValue(titleRowFeed, sourceConfig.commentColumn)
            if (cellEntry != null) {
                comment = cellEntry.cell.col
            } else if (sourceConfig.commentColumn != null) {
                LogUtils.warn("Column ${sourceConfig.commentColumn} not found.")
            }

            for (locale in sourceConfig.localeColumns!!) {
                cellEntry = findCellByValue(titleRowFeed, locale)
                if (cellEntry == null) {
                    LogUtils.warn("Column $locale not found. Resource files will not be created/changed for this locale.")
                    locales[locale] = -1
                } else
                    locales[locale] = cellEntry.cell.col
            }

            cellEntry = findCellByValue(titleRowFeed, sourceConfig.metadataColumn)
            if (cellEntry != null) {
                metadata = cellEntry.cell.col
            } else if (sourceConfig.metadataColumn != null) {
                LogUtils.warn("Column ${sourceConfig.metadataColumn} not found.")
            }

            mColumnIndexes = ColumnIndexes(key, quantity, comment, locales, metadata)
            LogUtils.info("Column Indexes: $mColumnIndexes")

            mRowsCount = try {
                val list = mWorksheetFacade
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
                mQuery = mWorksheetFacade
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

}
