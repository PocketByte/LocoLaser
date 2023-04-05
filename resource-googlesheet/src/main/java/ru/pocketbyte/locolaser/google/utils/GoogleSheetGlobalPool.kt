package ru.pocketbyte.locolaser.google.utils

import com.google.api.services.sheets.v4.Sheets
import java.io.File
import java.io.IOException


object GoogleSheetGlobalPool {

    private val sheetsMap = mutableMapOf<String, Sheets>()

    private const val APPLICATION_NAME = "LocoLaser"

    fun getService(sheetId: String, credentialFile: File?): Sheets {
        val sheetKey = "$sheetId/${credentialFile?.canonicalPath ?: "null"}"
        return sheetsMap[sheetKey] ?: {

            val credentials = if (credentialFile != null)
                try {
                    OAuth2Helper.credentialFromFile(credentialFile)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            else
                try {
                    OAuth2Helper.getCredential(sheetId)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

            val service = Sheets.Builder(
                    OAuth2Helper.HTTP_TRANSPORT,
                    OAuth2Helper.JSON_FACTORY,
                    credentials
            ).setApplicationName(APPLICATION_NAME).build()

            sheetsMap[sheetKey] = service
            service
        }()
    }
}