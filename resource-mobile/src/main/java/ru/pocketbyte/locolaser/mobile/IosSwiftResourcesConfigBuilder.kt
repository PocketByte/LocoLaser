package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class IosSwiftResourcesConfigBuilder
    : IosClassResourcesConfigBuilder<IosSwiftResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IosSwiftResourcesConfig {
        return IosSwiftResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, tableName, filter
        )
    }
}