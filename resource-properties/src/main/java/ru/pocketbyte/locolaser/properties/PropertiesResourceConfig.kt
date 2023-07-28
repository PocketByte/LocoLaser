package ru.pocketbyte.locolaser.properties

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.properties.resource.PropertiesResources
import ru.pocketbyte.locolaser.properties.resource.file.provider.PropertiesResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources

class PropertiesResourceConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "properties"
    }

    override val type: String = TYPE
    override val defaultTempDirPath: String = "./build/tmp/"
    override val defaultResourcesPath: String = "./locales/"
    override val defaultResourceName: String = "strings"

    override var resourceFileProvider: ResourceFileProvider = PropertiesResourceFileProvider()

    override val resources: Resources
        get() = PropertiesResources(resourcesDir, resourceName, resourceFileProvider, filter)

}
