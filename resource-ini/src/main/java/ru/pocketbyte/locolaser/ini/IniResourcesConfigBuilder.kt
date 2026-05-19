package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

/**
 * Builder for [IniResourcesConfig].
 *
 * Provides configuration options for INI file-based localization resources,
 * including the formatting type applied to localized string values.
 */
class IniResourcesConfigBuilder : BaseResourcesConfigBuilder<IniResourcesConfig>() {

    /**
     * Formatting type applied to localized string values.
     * Defaults to [JavaFormattingType].
     */
    var formattingType: FormattingType = JavaFormattingType

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): IniResourcesConfig {
        return IniResourcesConfig(
            workDir, resourceName, resourcesDir, formattingType, resourceFileProvider, filter
        )
    }
}
