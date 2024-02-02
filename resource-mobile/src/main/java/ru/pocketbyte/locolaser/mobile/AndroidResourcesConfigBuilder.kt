package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class AndroidResourcesConfigBuilder
    : BaseResourcesConfigBuilder<AndroidResourcesConfig>() {
    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): AndroidResourcesConfig {
        return AndroidResourcesConfig(
            resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}