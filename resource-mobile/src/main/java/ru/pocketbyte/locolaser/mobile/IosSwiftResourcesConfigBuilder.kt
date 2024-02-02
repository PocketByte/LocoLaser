package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class IosSwiftResourcesConfigBuilder
    : IosClassResourcesConfigBuilder<IosSwiftResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IosSwiftResourcesConfig {
        return IosSwiftResourcesConfig(
            resourceName, resourcesDir, resourceFileProvider, tableName, filter
        )
    }
}