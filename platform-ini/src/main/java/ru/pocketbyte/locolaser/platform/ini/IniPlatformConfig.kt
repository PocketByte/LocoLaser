package ru.pocketbyte.locolaser.platform.ini

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.ini.resource.IniResources
import ru.pocketbyte.locolaser.resource.PlatformResources

class IniPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "ini"
    }

    override val type: String
        get() = TYPE

    override val defaultTempDirPath: String
        get() = "./build/tmp/"

    override val defaultResourcesPath: String
        get() = ".//"

    override val defaultResourceName: String
        get() = "data"

    override val resources: PlatformResources
        get() = IniResources(resourcesDir!!, resourceName!!)

}
