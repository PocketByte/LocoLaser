package ru.pocketbyte.locolaser.google

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfigBuilder
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

/**
 * Builder for [GoogleSheetResourcesConfig].
 *
 * Provides configuration options for accessing a Google Spreadsheet as a localization source,
 * including the spreadsheet ID, worksheet title, OAuth credential file, and string formatting type.
 */
class GoogleSheetResourcesConfigBuilder
    : BaseTableResourcesConfigBuilder<GoogleSheetResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        keyColumn: String,
        quantityColumn: String?,
        commentColumn: String?,
        metadataColumn: String?
    ): GoogleSheetResourcesConfig {
        return GoogleSheetResourcesConfig(
            workDir,
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
     * ID of the Google Spreadsheet used as the localization source.
     * Can be found in the spreadsheet URL: `https://docs.google.com/spreadsheets/d/{sheet_id}/`.
     */
    var id: String? = null

    /**
     * Title of the worksheet containing localized strings.
     * Optional; defaults to the first worksheet in the spreadsheet.
     */
    var worksheetTitle: String? = null

    /**
     * Path to the OAuth credential file.
     * If `null`, OAuth2 interactive authentication is used.
     */
    var credentialFile: String? = null

    /**
     * Formatting type applied to localized string values.
     * Defaults to [JavaFormattingType].
     */
    var formattingType: FormattingType = JavaFormattingType
}
