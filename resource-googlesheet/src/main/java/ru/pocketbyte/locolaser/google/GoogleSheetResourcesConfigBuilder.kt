package ru.pocketbyte.locolaser.google

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfigBuilder
import ru.pocketbyte.locolaser.google.sheet.GoogleSheetConfig
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import java.io.File

class GoogleSheetResourcesConfigBuilder(
    private val config: GoogleSheetConfig
): BaseTableResourcesConfigBuilder(config) {

    var id: String?
        get() = config.id
        set(value) {config.id = value }

    var worksheetTitle: String?
        get() = config.worksheetTitle
        set(value) {config.worksheetTitle = value }

    var credentialFile: File?
        get() = config.credentialFile
        set(value) {config.credentialFile = value }

    fun credentialFile(path: String) {
        credentialFile = File(path)
    }

    var formattingType: FormattingType
        get() = config.formattingType
        set(value) {config.formattingType = value }

}