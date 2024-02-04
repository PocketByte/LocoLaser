package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class IosResourcesConfigBuilder : BaseResourcesConfigBuilder<IosResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IosResourcesConfig {
        return IosResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}