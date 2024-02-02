package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class IosResourcesConfigBuilder : BaseResourcesConfigBuilder<IosResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IosResourcesConfig {
        return IosResourcesConfig(
            resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}