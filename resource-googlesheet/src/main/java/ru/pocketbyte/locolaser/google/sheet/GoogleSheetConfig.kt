/*
* Copyright © 2017 Denis Shurygin. All rights reserved.
* Licensed under the Apache License, Version 2.0
*/

package ru.pocketbyte.locolaser.google.sheet

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfig
import ru.pocketbyte.locolaser.google.utils.GoogleSheetGlobalPool
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.utils.buildFileFrom

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
    var credentialFile: String? = null

    var formattingType: FormattingType = JavaFormattingType

    override val type = TYPE

    override val defaultTempDirPath: String = "./temp/"

    override val resources: Resources
        get() {
            return id?.let { id ->
                GoogleSheet(
                    this,
                    GoogleSheetGlobalPool.getService(
                        id, credentialFile?.let { buildFileFrom(workDir, it) }
                    ),
                    formattingType
                )
            } ?: throw IllegalStateException("Sheet ID is not set")
        }
}
