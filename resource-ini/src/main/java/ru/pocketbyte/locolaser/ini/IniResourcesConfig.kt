package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.ini.resource.IniResources
import ru.pocketbyte.locolaser.ini.resource.file.provider.IniResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources

class IniResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    filter: ((key: String) -> Boolean)?
) : BaseResourcesConfig(
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: IniResourceFileProvider(),
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

    override val resources: Resources
        get() = IniResources(resourcesDir, resourceName, resourceFileProvider, filter)

}
