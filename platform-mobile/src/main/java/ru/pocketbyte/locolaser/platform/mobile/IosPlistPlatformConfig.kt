package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.resource.IosPlistResources

/**
 * iOS Info.plist localisation configuration.
 *
 * @author Denis Shurygin
 */
class IosPlistPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "ios_plist"
    }

    override val type = TYPE

    override val defaultTempDirPath = "../DerivedData/LocoLaserTemp/"
    override val defaultResourcesPath = "./"
    override val defaultResourceName = "InfoPlist"

    override val resources
        get() = IosPlistResources(resourcesDir!!, resourceName!!)

}