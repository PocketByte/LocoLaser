package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.ini.resource.IniResources
import ru.pocketbyte.locolaser.ini.resource.file.provider.IniResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

class IniResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    filter: ((key: String) -> Boolean)?
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

    override val defaultTempDirPath: String = "./build/tmp/"
    override val defaultResourcesPath: String = "./"
    override val defaultResourceName: String = "data"

    override val resources: Resources by lazy {
        IniResources(
            resourcesDir = this.resourcesDir,
            fileName = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }
}
