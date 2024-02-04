package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class AndroidResourcesConfigBuilder
    : BaseResourcesConfigBuilder<AndroidResourcesConfig>() {
    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): AndroidResourcesConfig {
        return AndroidResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}