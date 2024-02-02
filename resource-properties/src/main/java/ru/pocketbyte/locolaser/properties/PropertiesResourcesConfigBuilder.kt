package ru.pocketbyte.locolaser.properties

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class PropertiesResourcesConfigBuilder : BaseResourcesConfigBuilder<PropertiesResourcesConfig>() {
    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): PropertiesResourcesConfig {
        return PropertiesResourcesConfig(
            resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}