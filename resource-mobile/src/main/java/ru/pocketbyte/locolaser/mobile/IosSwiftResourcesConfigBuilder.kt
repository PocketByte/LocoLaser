package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [IosSwiftResourcesConfig].
 *
 * Provides configuration options for the iOS Swift class generator,
 * which produces a `.swift` file containing a class with string constants
 * for each localization key.
 */
class IosSwiftResourcesConfigBuilder
    : IosClassResourcesConfigBuilder<IosSwiftResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): IosSwiftResourcesConfig {
        return IosSwiftResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, tableName, filter
        )
    }
}
