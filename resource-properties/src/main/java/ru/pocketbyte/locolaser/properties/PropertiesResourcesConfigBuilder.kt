package ru.pocketbyte.locolaser.properties

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class PropertiesResourcesConfigBuilder : BaseResourcesConfigBuilder<PropertiesResourcesConfig>() {
    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): PropertiesResourcesConfig {
        return PropertiesResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}