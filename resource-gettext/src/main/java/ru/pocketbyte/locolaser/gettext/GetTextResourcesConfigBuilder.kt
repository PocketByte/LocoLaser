package ru.pocketbyte.locolaser.gettext

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class GetTextResourcesConfigBuilder : BaseResourcesConfigBuilder<GetTextResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): GetTextResourcesConfig {
        return GetTextResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}
