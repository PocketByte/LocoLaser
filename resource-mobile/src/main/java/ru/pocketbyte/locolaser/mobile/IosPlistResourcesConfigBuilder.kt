package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class IosPlistResourcesConfigBuilder : BaseResourcesConfigBuilder<IosPlistResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IosPlistResourcesConfig {
        return IosPlistResourcesConfig(
            resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}