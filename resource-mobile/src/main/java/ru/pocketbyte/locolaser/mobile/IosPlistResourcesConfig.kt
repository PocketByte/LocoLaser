package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.mobile.resource.IosPlistResources
import ru.pocketbyte.locolaser.mobile.resource.file.provider.IosResourceFileProvider

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

    override var resourceFileProvider: ResourceFileProvider = IosResourceFileProvider()

    override val resources
        get() = IosPlistResources(resourcesDir, resourceName, resourceFileProvider, filter)

}