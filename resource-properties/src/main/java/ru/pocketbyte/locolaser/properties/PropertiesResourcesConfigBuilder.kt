package ru.pocketbyte.locolaser.properties

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

/**
 * Builder for [PropertiesResourcesConfig].
 *
 * Provides configuration options for Java `.properties` file-based localization resources,
 * including the formatting type applied to localized string values.
 */
class PropertiesResourcesConfigBuilder : BaseResourcesConfigBuilder<PropertiesResourcesConfig>() {

    /** Formatting type applied to localized string values. Defaults to [JavaFormattingType]. */
    var formattingType: FormattingType = JavaFormattingType

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): PropertiesResourcesConfig {
        return PropertiesResourcesConfig(
            workDir = workDir,
            resourceName = resourceName,
            resourcesDirPath = resourcesDir,
            formattingType = formattingType,
            resourceFileProvider = resourceFileProvider,
            filter = filter
        )
    }
}