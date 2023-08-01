package ru.pocketbyte.locolaser.google

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfigBuilder
import ru.pocketbyte.locolaser.google.sheet.GoogleSheetConfig
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import java.io.File

class GoogleSheetResourcesConfigBuilder
    : BaseTableResourcesConfigBuilder<GoogleSheetConfig>(GoogleSheetConfig()) {

    /**
     * ID of the Google Sheet.
     * You can get it from sheet url (https://docs.google.com/spreadsheets/d/{{sheet_id}}).
     */
    var id: String?
        get() = config.id
        set(value) {config.id = value }

    /**
     * Title of the worksheet with localized strings.
     * Not necessary property, by default will be used first worksheet of the sheet.
     */
    var worksheetTitle: String?
        get() = config.worksheetTitle
        set(value) {config.worksheetTitle = value }

    /**
     * Path to OAUth credential file.
     */
    var credentialFile: String?
        get() = config.credentialFile
        set(value) {config.credentialFile = value }

    /**
     * Values formatting type.
     * Default value [ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType].
     */
    var formattingType: FormattingType
        get() = config.formattingType
        set(value) {config.formattingType = value }

}