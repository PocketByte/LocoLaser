package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

class IosSwiftResourcesConfigBuilder
    : IosClassResourcesConfigBuilder<IosSwiftResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): IosSwiftResourcesConfig {
        return IosSwiftResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, tableName, filter
        )
    }
}