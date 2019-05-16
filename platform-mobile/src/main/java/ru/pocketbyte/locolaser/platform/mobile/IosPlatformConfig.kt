/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.resource.IosResources
import ru.pocketbyte.locolaser.resource.PlatformResources

/**
 * iOS platform configuration.
 *
 * @author Denis Shurygin
 */
class IosPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "ios"
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
        return "Localizable"
    }

    override fun getResources(): PlatformResources {
        return IosResources(resourcesDir, resourceName)
    }

}