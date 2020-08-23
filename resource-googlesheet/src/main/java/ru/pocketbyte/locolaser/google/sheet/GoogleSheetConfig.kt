/*
* Copyright © 2017 Denis Shurygin. All rights reserved.
* Licensed under the Apache License, Version 2.0
*/

package ru.pocketbyte.locolaser.google.sheet

import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.SpreadsheetEntry
import com.google.gdata.data.spreadsheet.WorksheetEntry
import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfig
import ru.pocketbyte.locolaser.google.utils.GoogleSheetGlobalPool
import java.io.File

/**
 * @author Denis Shurygin
 */
class GoogleSheetConfig : BaseTableResourcesConfig() {

    companion object {
        const val TYPE = "googlesheet"
    }

    var id: String? = null

    /**
     * Title of the Worksheet that should be used for localization.
     */
    var worksheetTitle: String? = null
    var credentialFile: File? = null

    override val type = TYPE

    override val defaultTempDir: File = File("./temp/")

    override val resources: GoogleSheet
        get() {
            return id?.let { id ->
                GoogleSheet(
                        this,
                        object : WorksheetFacade() {
                            override val service: SpreadsheetService
                                get() = GoogleSheetGlobalPool.getService(id, credentialFile)
                            override val sheetEntry: SpreadsheetEntry
                                get() = GoogleSheetGlobalPool.getSheetEntry(id, service)
                            override val worksheetEntry: WorksheetEntry
                                get() = GoogleSheetGlobalPool.getWorksheet(sheetEntry, worksheetTitle)
                        }
                )
            } ?: throw IllegalStateException("Sheet ID is not set")
        }
}
