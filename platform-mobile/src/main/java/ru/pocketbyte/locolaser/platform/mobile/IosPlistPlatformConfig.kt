package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.resource.IosPlistResources
import ru.pocketbyte.locolaser.resource.PlatformResources

/**
 * iOS Info.plist localisation configuration.
 *
 * @author Denis Shurygin
 */
class IosPlistPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "ios_plist"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getDefaultTempDirPath(): String {
        return "../DerivedData/LocoLaserTemp/"
    }

    override fun getDefaultResourcesPath(): String {
        return "./"
    }

    override fun getDefaultResourceName(): String {
        return "InfoPlist"
    }

    override fun getResources(): PlatformResources {
        return IosPlistResources(resourcesDir, resourceName)
    }

}