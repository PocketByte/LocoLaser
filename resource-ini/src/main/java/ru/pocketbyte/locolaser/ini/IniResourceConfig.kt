package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.ini.resource.IniResources
import ru.pocketbyte.locolaser.resource.Resources

class IniResourceConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "ini"
    }

    override val type: String
        get() = TYPE

    override val defaultTempDirPath: String
        get() = "./build/tmp/"

    override val defaultResourcesPath: String
        get() = "./"

    override val defaultResourceName: String
        get() = "data"

    override val resources: Resources
        get() = IniResources(resourcesDir, resourceName, filter)

}
