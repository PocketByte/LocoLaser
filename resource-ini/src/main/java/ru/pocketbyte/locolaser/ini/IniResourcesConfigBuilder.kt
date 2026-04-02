package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class IniResourcesConfigBuilder : BaseResourcesConfigBuilder<IniResourcesConfig>() {

    var formattingType: FormattingType = JavaFormattingType

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): IniResourcesConfig {
        return IniResourcesConfig(
            workDir, resourceName, resourcesDir, formattingType, resourceFileProvider, filter
        )
    }
}
