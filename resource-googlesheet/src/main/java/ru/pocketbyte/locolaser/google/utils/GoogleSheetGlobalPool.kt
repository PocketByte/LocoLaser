package ru.pocketbyte.locolaser.google.utils

import com.google.gdata.client.spreadsheet.SpreadsheetService
import com.google.gdata.data.spreadsheet.SpreadsheetEntry
import com.google.gdata.data.spreadsheet.WorksheetEntry
import com.google.gdata.data.spreadsheet.WorksheetFeed
import com.google.gdata.util.ResourceNotFoundException
import com.google.gdata.util.ServiceException
import ru.pocketbyte.locolaser.utils.LogUtils
import java.io.File
import java.io.IOException
import java.net.URL

object GoogleSheetGlobalPool {

    private val servicesMap = mutableMapOf<String, SpreadsheetService>()
    private val entriesMap = mutableMapOf<String, SpreadsheetEntry>()
    private val worksheetsMap = mutableMapOf<String, WorksheetEntry>()

    fun getService(sheetId: String, credentialFile: File?): SpreadsheetService {
        val sheetKey = "$sheetId/${credentialFile?.canonicalPath ?: "null"}"
        return servicesMap[sheetKey] ?: {
            val service = SpreadsheetService("spreadsheetservice")

            if (credentialFile != null)
                try {
                    service.setOAuth2Credentials(OAuth2Helper.credentialFromFile(credentialFile))
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            else
                try {
                    service.setOAuth2Credentials(OAuth2Helper.getCredential(sheetId))
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

            servicesMap[sheetKey] = service
            service
        }()
    }

    fun getSheetEntry(sheetId: String, service: SpreadsheetService): SpreadsheetEntry {
        val entryKey = "$sheetId/${service.hashCode()}"
        return entriesMap[entryKey] ?: {
            val entry: SpreadsheetEntry
            val url = URL("https://spreadsheets.google.com/feeds/spreadsheets/$sheetId")
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
                try {
                    OAuth2Helper.deleteCredential(sheetId)
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

                throw RuntimeException(e)
            } catch (e: ServiceException) {
                throw RuntimeException(e)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            entriesMap[entryKey] = entry

            LogUtils.info("Sheet opened: " + entry.title.plainText)

            entry
        }()
    }



    fun getWorksheet(sheetEntry: SpreadsheetEntry, title: String?): WorksheetEntry {
        val worksheetKey = "${sheetEntry.hashCode()}/$title"
        return worksheetsMap[worksheetKey] ?: {
            var worksheet: WorksheetEntry? = null
            val worksheets: List<WorksheetEntry>
            try {
                worksheets = sheetEntry.worksheets
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

            if (worksheets.isEmpty()) {
                LogUtils.info("Sheet is empty. 0 worksheets found.")
                throw RuntimeException("ERROR: Nothing to parse!")
            } else {
                if (title != null) {
                    LogUtils.info("Getting worksheet with title: $title")
                    for (entry in worksheets) {
                        if (title == entry.title.plainText) {
                            worksheet = entry
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
            }

            worksheetsMap[worksheetKey] = worksheet
            worksheet ?: throw RuntimeException("Worksheet is null")
        }()
    }
}