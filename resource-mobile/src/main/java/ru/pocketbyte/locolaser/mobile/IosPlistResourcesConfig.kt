package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.mobile.resource.IosPlistResources

/**
 * iOS Info.plist localisation configuration.
 *
 * @author Denis Shurygin
 */
class IosPlistResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "ios_plist"
    }

    override val type = TYPE

    override val defaultTempDirPath = "../DerivedData/LocoLaserTemp/"
    override val defaultResourcesPath = "./"
    override val defaultResourceName = "InfoPlist"

    override val resources
        get() = IosPlistResources(resourcesDir, resourceName, filter)

}