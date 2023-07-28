package ru.pocketbyte.locolaser.ini

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.ini.resource.IniResources
import ru.pocketbyte.locolaser.ini.resource.file.provider.IniResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources

class IniResourceConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "ini"
    }

    override val type: String = TYPE

    override val defaultTempDirPath: String = "./build/tmp/"
    override val defaultResourcesPath: String = "./"
    override val defaultResourceName: String = "data"

    override var resourceFileProvider: ResourceFileProvider = IniResourceFileProvider()

    override val resources: Resources
        get() = IniResources(resourcesDir, resourceName, resourceFileProvider, filter)

}
