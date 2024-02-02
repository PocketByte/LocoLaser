package ru.pocketbyte.locolaser.properties

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.properties.resource.PropertiesResources
import ru.pocketbyte.locolaser.properties.resource.file.provider.PropertiesResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources

class PropertiesResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    filter: ((key: String) -> Boolean)?
) : BaseResourcesConfig(
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: PropertiesResourceFileProvider(),
    filter
) {

    companion object : ResourcesConfigBuilderFactory<PropertiesResourcesConfig, PropertiesResourcesConfigBuilder> {
        const val TYPE = "properties"

        override fun getBuilder(): PropertiesResourcesConfigBuilder {
            return PropertiesResourcesConfigBuilder()
        }
    }

    override val type: String = TYPE
    override val defaultTempDirPath: String = "./build/tmp/"
    override val defaultResourcesPath: String = "./locales/"
    override val defaultResourceName: String = "strings"

    override val resources: Resources
        get() = PropertiesResources(resourcesDir, resourceName, resourceFileProvider, filter)

}
