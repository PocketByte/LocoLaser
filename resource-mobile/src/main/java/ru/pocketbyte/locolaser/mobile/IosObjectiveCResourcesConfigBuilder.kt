package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class IosObjectiveCResourcesConfigBuilder
    : IosClassResourcesConfigBuilder<IosObjectiveCResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IosObjectiveCResourcesConfig {
        return IosObjectiveCResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, tableName, filter
        )
    }
}