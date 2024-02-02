package ru.pocketbyte.locolaser.google

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfigBuilder
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType

class GoogleSheetResourcesConfigBuilder
    : BaseTableResourcesConfigBuilder<GoogleSheetResourcesConfig>() {

    override fun buildConfig(
        keyColumn: String,
        quantityColumn: String?,
        commentColumn: String?,
        metadataColumn: String?
    ): GoogleSheetResourcesConfig {
        return GoogleSheetResourcesConfig(
            id ?: throw IllegalStateException("Sheet ID is not set"),
            worksheetTitle,
            credentialFile,
            formattingType,
            keyColumn,
            quantityColumn,
            commentColumn,
            metadataColumn
        )
    }

    /**
     * ID of the Google Sheet.
     * You can get it from sheet url (https://docs.google.com/spreadsheets/d/{{sheet_id}}).
     */
    var id: String? = null

    /**
     * Title of the worksheet with localized strings.
     * Not necessary property, by default will be used first worksheet of the sheet.
     */
    var worksheetTitle: String? = null

    /**
     * Path to OAUth credential file.
     */
    var credentialFile: String? = null

    /**
     * Values formatting type.
     * Default value [ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType].
     */
    var formattingType: FormattingType = JavaFormattingType

}