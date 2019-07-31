/*
* Copyright © 2017 Denis Shurygin. All rights reserved.
* Licensed under the Apache License, Version 2.0
*/

package ru.pocketbyte.locolaser.source.google.sheet

import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.SpreadsheetEntry
import com.google.gdata.data.spreadsheet.WorksheetEntry
import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig
import ru.pocketbyte.locolaser.source.google.utils.GoogleSheetGlobalPool
import ru.pocketbyte.locolaser.utils.LogUtils
import java.io.File

/**
 * @author Denis Shurygin
 */
class GoogleSheetConfig : BaseTableSourceConfig() {

    companion object {
        const val TYPE = "googlesheet"
    }

    // FIXME resolve nullability
    var id: String? = null

    /**
     * Title of the Worksheet that should be used for localization.
     */
    var worksheetTitle: String? = null
    var credentialFile: File? = null

    override val type = TYPE

    override fun open(): GoogleSheet? {

        val holder = object : WorksheetFacade() {
            override val service: SpreadsheetService
                get() = GoogleSheetGlobalPool.getService(id!!, credentialFile)
            override val sheetEntry: SpreadsheetEntry
                get() = GoogleSheetGlobalPool.getSheetEntry(id!!, service)
            override val worksheetEntry: WorksheetEntry
                get() = GoogleSheetGlobalPool.getWorksheet(sheetEntry, worksheetTitle)
        }

        LogUtils.info("Open sheet: " + holder.sheetEntry.title.plainText)

        return GoogleSheet(this, holder)
    }
}
