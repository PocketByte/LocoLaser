package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class IosObjectiveCResourcesConfigBuilder
    : IosClassResourcesConfigBuilder<IosObjectiveCResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IosObjectiveCResourcesConfig {
        return IosObjectiveCResourcesConfig(
            resourceName, resourcesDir, resourceFileProvider, tableName, filter
        )
    }
}