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
import ru.pocketbyte.locolaser.config.source.BaseTableSource
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.utils.LogUtils

import java.io.IOException
import java.net.URL
import java.util.*

/**
 * @author Denis Shurygin
 */
class GoogleSheet(private val mConfig: GoogleSheetConfig, private val mWorksheetFacade: WorksheetFacade) : BaseTableSource(mConfig) {

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
    private var mColumnIndexes: ColumnIndexes? = null

    private var mTitleRow = -1
    private var mRowsCount = 0

    private var mQuery: CellFeed? = null

    override val modifiedDate = mWorksheetFacade.sheetEntry.updated.value

    override fun write(resMap: ResMap) {
        if (fetchCellsIfNeeded()) {
            var totalRows = mRowsCount

            val newRowIds = HashMap<String, NewRowItem>()

            val batchRequest = CellFeed()

            for ((key, value) in resMap) {
                val localeColumn = mColumnIndexes!!.locales[key]!! //FIXME nullability

                if (localeColumn >= 0) {

                    val iterator = value.entries.iterator()
                    while (iterator.hasNext()) {
                        val entry = iterator.next()
                        val resItem = entry.value


                        for (i in resItem.values.indices) {
                            val resValue = resItem.values[i]
                            // =====================================
                            // Prepare batch for found missed resMap
                            if (resValue.location is CellLocation) {
                                val resRow = (resValue.location as CellLocation).row
                                var batchEntry = CellEntry(getCell(localeColumn, resRow)!!)
                                batchEntry.changeInputValueLocal(valueToSourceValue(resValue.value))
                                BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                batchRequest.entries.add(batchEntry)

                                val comment = resValue.comment
                                if (comment != null && mColumnIndexes!!.comment >= 0) {
                                    batchEntry = CellEntry(getCell(mColumnIndexes!!.comment, resRow)!!)
                                    batchEntry.changeInputValueLocal(valueToSourceValue(comment))
                                    BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                    batchRequest.entries.add(batchEntry)
                                }

                                resItem.removeValue(resValue)
                                if (resItem.values.size == 0)
                                    iterator.remove()
                            } else {
                                // =====================================
                                // Reserve rows for new keys
                                val row: Int
                                val mapKey = resItem.key + ":" + resValue.quantity.toString()
                                val newRowItem = newRowIds[mapKey]
                                if (newRowItem != null)
                                    row = newRowItem.row
                                else {
                                    row = ++totalRows
                                    newRowIds[mapKey] = NewRowItem(resItem.key, row)
                                }
                                resValue.location = CellLocation(this, localeColumn, row)
                            }
                        }
                    }
                }
            }

            try {
                val batchLink = mQuery!!.getLink(ILink.Rel.FEED_BATCH, ILink.Type.ATOM)
                mWorksheetFacade.batch(URL(batchLink.href), batchRequest)

                if (totalRows > mWorksheetFacade.rowCount) {
                    mWorksheetFacade.rowCount = totalRows
                }

            } catch (e: IOException) {
                e.printStackTrace()
                throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id!!)
            } catch (e: ServiceException) {
                e.printStackTrace()
                throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id!!)
            }

            if (totalRows > mRowsCount) {
                var cellFeed: CellFeed? = null
                try {
                    cellFeed = mWorksheetFacade
                            .queryRange(mColumnIndexes!!.min, mRowsCount + 1, mColumnIndexes!!.max, totalRows, true)
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id!!)
                } catch (e: ServiceException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id!!)
                }

                for (newRowItem in newRowIds.values) {
                    val index = entryIndex(mColumnIndexes!!.key, newRowItem.row, mRowsCount)
                    val batchEntry = CellEntry(cellFeed!!.entries[index])
                    batchEntry.changeInputValueLocal(newRowItem.key)
                    BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                    batchRequest.entries.add(batchEntry)
                }

                // Map to remember which quantity already written.
                val editedQuantities = HashMap<String, ArrayList<Quantity>>(newRowIds.size)

                for ((key, value) in resMap) {
                    val localeColumn = mColumnIndexes!!.locales[key]!! //FIXME nullability!

                    if (localeColumn >= 0) {
                        for ((_, resItem) in value) {

                            var editedQuantitiesForKey: ArrayList<Quantity>? = editedQuantities[resItem.key]
                            if (editedQuantitiesForKey == null) {
                                editedQuantitiesForKey = ArrayList(resItem.values.size)
                                editedQuantities[resItem.key] = editedQuantitiesForKey
                            }

                            for (resValue in resItem.values) {
                                if (resValue.location is CellLocation) {
                                    val resRow = (resValue.location as CellLocation).row
                                    var index = entryIndex(localeColumn, resRow, mRowsCount)
                                    var batchEntry = CellEntry(cellFeed!!.entries[index])
                                    batchEntry.changeInputValueLocal(valueToSourceValue(resValue.value))
                                    BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                    batchRequest.entries.add(batchEntry)

                                    val comment = resValue.comment
                                    if (comment != null && mColumnIndexes!!.comment >= 0) {
                                        index = entryIndex(mColumnIndexes!!.comment, resRow, mRowsCount)
                                        batchEntry = CellEntry(cellFeed.entries[index])
                                        batchEntry.changeInputValueLocal(valueToSourceValue(comment))
                                        BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                        batchRequest.entries.add(batchEntry)
                                    }

                                    // Build request only if quantity doesn't edited before
                                    if (mColumnIndexes!!.quantity >= 0 && !editedQuantitiesForKey
                                                    .contains(resValue.quantity)) {
                                        index = entryIndex(mColumnIndexes!!.quantity, resRow, mRowsCount)
                                        batchEntry = CellEntry(cellFeed.entries[index])
                                        if (resItem.isHasQuantities)
                                            batchEntry.changeInputValueLocal(resValue.quantity.toString())
                                        else
                                            batchEntry.changeInputValueLocal("")
                                        BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.UPDATE)
                                        batchRequest.entries.add(batchEntry)

                                        editedQuantitiesForKey.add(resValue.quantity)
                                    }
                                } else {
                                    //TODO warn
                                }
                            }
                        }
                    }
                }

                try {
                    val batchLink = cellFeed!!.getLink(ILink.Rel.FEED_BATCH, ILink.Type.ATOM)
                    mWorksheetFacade.batch(URL(batchLink.href), batchRequest)
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id!!)
                } catch (e: ServiceException) {
                    e.printStackTrace()
                    throw RuntimeException("ERROR: Failed to write sheet. Sheet Id: " + mConfig.id!!)
                }

            }
        }
    }

    override fun close() {
        mQuery = null
        mColumnIndexes = null
    }

    override val firstRow: Int
        get() {
            fetchCellsIfNeeded()
            return mTitleRow + 1
        }

    override val columnIndexes: ColumnIndexes
        get() {
            fetchCellsIfNeeded()
            return mColumnIndexes!!
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

            var key = 0
            var quantity = -1
            var comment = -1
            val locales = HashMap<String, Int>()

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
            if (cellEntry != null)
                quantity = cellEntry.cell.col

            cellEntry = findCellByValue(titleRowFeed, sourceConfig.commentColumn)
            if (cellEntry != null)
                comment = cellEntry.cell.col

            for (locale in sourceConfig.localeColumns!!) {
                cellEntry = findCellByValue(titleRowFeed, locale)
                if (cellEntry == null) {
                    LogUtils.warn("Column " + locale +
                            " not found. Resource files will not be created/changed for this locale.")
                    locales[locale] = -1
                } else
                    locales[locale] = cellEntry.cell.col
            }
            mColumnIndexes = BaseTableSource.ColumnIndexes(key, quantity, comment, locales)

            try {
                val list = mWorksheetFacade
                        .queryRange(mColumnIndexes!!.key, mTitleRow + 1, mColumnIndexes!!.key, -1, false)!!.entries
                if (list.size > 0)
                    mRowsCount = list[list.size - 1].cell.row
                else
                    mRowsCount = mTitleRow
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            } catch (e: ServiceException) {
                e.printStackTrace()
                return false
            }

            try {
                mQuery = mWorksheetFacade
                        .queryRange(mColumnIndexes!!.min, mTitleRow + 1, mColumnIndexes!!.max, mRowsCount, true)
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
            if (index < mQuery!!.entries.size)
                return mQuery!!.entries[index]
        }
        return null
    }

    private fun entryIndex(col: Int, row: Int, top: Int): Int {
        return (row - top - 1) * (mColumnIndexes!!.max - mColumnIndexes!!.min + 1) + col - mColumnIndexes!!.min
    }

    private class NewRowItem constructor(internal val key: String, internal val row: Int)

}
