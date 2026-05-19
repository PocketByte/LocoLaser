package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.ini.resource.IniResources
import ru.pocketbyte.locolaser.ini.resource.file.provider.IniResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import java.io.File

/**
 * Resources configuration for the INI file format.
 */
class IniResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    val formattingType: FormattingType,
    resourceFileProvider: ResourceFileProvider?,
    filter: ResourcesFilter?
) : BaseResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: IniResourceFileProvider,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<IniResourcesConfig, IniResourcesConfigBuilder> {
        const val TYPE = "ini"

        override fun getBuilder(): IniResourcesConfigBuilder {
            return IniResourcesConfigBuilder()
        }
    }

    override val type: String = TYPE

    override val defaultResourcesPath: String = "./"
    override val defaultResourceName: String = "data"

    override val resources: Resources by lazy {
        IniResources(
            resourcesDir = this.resourcesDir,
            fileName = this.resourceName,
            formattingType = this.formattingType,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }
}
