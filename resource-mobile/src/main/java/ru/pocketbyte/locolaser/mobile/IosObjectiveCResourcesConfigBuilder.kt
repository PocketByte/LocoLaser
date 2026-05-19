package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [IosObjectiveCResourcesConfig].
 *
 * Provides configuration options for the iOS Objective-C class generator,
 * which produces `.h` and `.m` files containing a class with string constants
 * for each localization key.
 */
class IosObjectiveCResourcesConfigBuilder
    : IosClassResourcesConfigBuilder<IosObjectiveCResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): IosObjectiveCResourcesConfig {
        return IosObjectiveCResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, tableName, filter
        )
    }
}
