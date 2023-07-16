package ru.pocketbyte.locolaser.properties

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.properties.resource.PropertiesResources
import ru.pocketbyte.locolaser.resource.Resources

class PropertiesResourceConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "properties"
    }

    override val type: String
        get() = TYPE

    override val defaultTempDirPath: String
        get() = "./build/tmp/"

    override val defaultResourcesPath: String
        get() = "./locales/"

    override val defaultResourceName: String
        get() = "strings"

    override val resources: Resources
        get() = PropertiesResources(resourcesDir, resourceName, filter)

}
