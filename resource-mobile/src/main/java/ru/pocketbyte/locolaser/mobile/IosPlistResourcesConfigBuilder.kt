package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [IosPlistResourcesConfig].
 *
 * Provides configuration options for the iOS Plist localization generator,
 * which produces `.strings` files used for localizing `Info.plist` entries.
 */
class IosPlistResourcesConfigBuilder : BaseResourcesConfigBuilder<IosPlistResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): IosPlistResourcesConfig {
        return IosPlistResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}
