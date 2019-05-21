/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.sheet

import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.SpreadsheetEntry
import com.google.gdata.data.spreadsheet.WorksheetEntry
import com.google.gdata.data.spreadsheet.WorksheetFeed
import com.google.gdata.util.ResourceNotFoundException
import com.google.gdata.util.ServiceException
import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig
import ru.pocketbyte.locolaser.source.google.utils.OAuth2Helper
import ru.pocketbyte.locolaser.utils.LogUtils

import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

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

    /**
     * Url for the sheet.
     */
    val url: URL
        @Throws(MalformedURLException::class)
        get() = URL("https://spreadsheets.google.com/feeds/spreadsheets/" + id!!)

    override val type = TYPE

    override fun open(): GoogleSheet? {
        val service = prepareService(credentialFile) ?: return null

        val sheetEntry = prepareSheetEntry(service) ?: return null

        LogUtils.info("Open sheet: " + sheetEntry.title.plainText)


        val worksheet = prepareWorksheet(sheetEntry, worksheetTitle) ?: return null

        val holder = WorksheetFacade(service, sheetEntry, worksheet)

        return GoogleSheet(this, holder)
    }

    private fun prepareService(credentialFile: File?): SpreadsheetService? {
        val service = SpreadsheetService("spreadsheetservice")

        if (credentialFile != null)
            try {
                service.setOAuth2Credentials(OAuth2Helper.credentialFromFile(credentialFile))
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        else
            try {
                service.setOAuth2Credentials(OAuth2Helper.getCredential(id!!))
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        return service
    }

    private fun prepareSheetEntry(service: SpreadsheetService): SpreadsheetEntry? {
        val entry: SpreadsheetEntry
        try {
            // !!! Bug workaround. Start !!!
            // Some times when used service account the server returns response with wrong content type.
            // Invoke getFeed request first, before getEntry.
            service.getFeed(URL(
                    "https://spreadsheets.google.com/feeds/spreadsheets/private/full?xoauth_requestor_id=test"),
                    WorksheetFeed::class.java)
            // !!! Bug workaround. End !!!

            entry = service.getEntry(url, SpreadsheetEntry::class.java)
        } catch (e: ResourceNotFoundException) {
            e.printStackTrace()
            try {
                OAuth2Helper.deleteCredential(id!!)
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

            return null
        } catch (e: ServiceException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return entry
    }

    private fun prepareWorksheet(sheetEntry: SpreadsheetEntry, title: String?): WorksheetEntry? {
        val worksheets: List<WorksheetEntry>
        try {
            worksheets = sheetEntry.worksheets
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: ServiceException) {
            e.printStackTrace()
            return null
        }

        LogUtils.info(Integer.toString(worksheets.size) + " worksheets found.")
        if (worksheets.isEmpty()) {
            LogUtils.err("ERROR: Nothing to parse!")
        } else {
            var worksheet: WorksheetEntry? = null
            if (title != null) {
                LogUtils.info("Searching worksheet with title: $title")
                for (entry in worksheets) {
                    if (title == entry.title.plainText) {
                        worksheet = entry
                        LogUtils.info("Worksheet found.")
                        break
                    }
                }
                if (worksheet == null)
                    LogUtils.info("Nothing found!")
            }
            if (worksheet == null) {
                worksheet = worksheets[0]
                LogUtils.info("Use default worksheet with index 0. Worksheet title: ${worksheet.title.plainText}")
            }
            return worksheet
        }
        return null
    }
}
