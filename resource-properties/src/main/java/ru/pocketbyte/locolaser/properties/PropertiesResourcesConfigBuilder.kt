package ru.pocketbyte.locolaser.properties

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class PropertiesResourcesConfigBuilder : BaseResourcesConfigBuilder<PropertiesResourcesConfig>() {

    var formattingType: FormattingType = JavaFormattingType

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): PropertiesResourcesConfig {
        return PropertiesResourcesConfig(
            workDir = workDir,
            resourceName = resourceName,
            resourcesDirPath = resourcesDir,
            formattingType = formattingType,
            resourceFileProvider = resourceFileProvider,
            filter = filter
        )
    }
}