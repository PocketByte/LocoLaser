package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

class IosObjectiveCResourcesConfigBuilder
    : IosClassResourcesConfigBuilder<IosObjectiveCResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): IosObjectiveCResourcesConfig {
        return IosObjectiveCResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, tableName, filter
        )
    }
}