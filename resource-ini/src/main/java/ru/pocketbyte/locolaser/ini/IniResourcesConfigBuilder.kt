package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class IniResourcesConfigBuilder : BaseResourcesConfigBuilder<IniResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): IniResourcesConfig {
        return IniResourcesConfig(
            workDir, resourceName, resourcesDir, resourceFileProvider, filter
        )
    }
}
