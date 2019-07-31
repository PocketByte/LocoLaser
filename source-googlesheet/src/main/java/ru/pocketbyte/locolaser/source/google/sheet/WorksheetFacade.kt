/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.sheet

import com.google.gdata.client.spreadsheet.CellQuery
import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.IFeed
import com.google.gdata.data.spreadsheet.CellFeed
import com.google.gdata.data.spreadsheet.SpreadsheetEntry
import com.google.gdata.data.spreadsheet.WorksheetEntry
import com.google.gdata.util.ServiceException

import java.io.IOException
import java.net.URL

/**
 * @author Denis Shurygin
 */
abstract class WorksheetFacade {

    abstract val service: SpreadsheetService
    abstract val sheetEntry: SpreadsheetEntry
    abstract val worksheetEntry: WorksheetEntry

    var rowCount: Int
        get() = worksheetEntry.rowCount
        @Throws(IOException::class, ServiceException::class)
        set(count) {
            worksheetEntry.rowCount = count
            worksheetEntry.update()
        }

    @Throws(IOException::class, ServiceException::class)
    fun <F : IFeed> batch(feedUrl: URL, inputFeed: F): F? {
        return service.batch(feedUrl, inputFeed)
    }

    @Throws(IOException::class, ServiceException::class)
    fun queryRange(left: Int, top: Int, right: Int, bottom: Int,
                   returnEmpty: Boolean): CellFeed? {
        val query = CellQuery(worksheetEntry.cellFeedUrl)

        if (top >= 0)
            query.minimumRow = top

        if (bottom >= 0)
            query.maximumRow = bottom

        if (left >= 0)
            query.minimumCol = left

        if (right >= 0)
            query.maximumCol = right

        query.returnEmpty = returnEmpty

        return service.getFeed(query.url, CellFeed::class.java)

    }
}
