package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class IniResourcesConfigBuilder : BaseResourcesConfigBuilder<IniResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IniResourcesConfig {
        return IniResourcesConfig(
            resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}
