/*
* Copyright © 2017 Denis Shurygin. All rights reserved.
* Licensed under the Apache License, Version 2.0
*/

package ru.pocketbyte.locolaser.google

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.google.resource.GoogleSheetResources
import ru.pocketbyte.locolaser.google.utils.GoogleSheetGlobalPool
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.utils.buildFileFrom

/**
 * @author Denis Shurygin
 */
class GoogleSheetResourcesConfig(

    val id: String,

    /**
     * Title of the Worksheet that should be used for localization.
     */
    val worksheetTitle: String? = null,
    val credentialFile: String? = null,
    val formattingType: FormattingType,

    keyColumn: String,
    quantityColumn: String? = null,
    commentColumn: String? = null,
    metadataColumn: String? = null
) : BaseTableResourcesConfig(
    keyColumn, quantityColumn, commentColumn, metadataColumn
) {

    companion object : ResourcesConfigBuilderFactory<GoogleSheetResourcesConfig, GoogleSheetResourcesConfigBuilder> {
        const val TYPE = "googlesheet"

        override fun getBuilder(): GoogleSheetResourcesConfigBuilder {
            return GoogleSheetResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultTempDirPath: String = "./temp/"

    override val resources: Resources
        get() {
            return GoogleSheetResources(
                this,
                GoogleSheetGlobalPool.getService(
                    id, credentialFile?.let { buildFileFrom(workDir, it) }
                ),
                formattingType
            )
        }
}
