package ru.pocketbyte.locolaser.gettext

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class GetTextResourcesConfigBuilder : BaseResourcesConfigBuilder<GetTextResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): GetTextResourcesConfig {
        return GetTextResourcesConfig(
            resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}
