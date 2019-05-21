/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.resource.IosResources

/**
 * iOS platform configuration.
 *
 * @author Denis Shurygin
 */
class IosPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "ios"
    }

    override val type = TYPE

    override val defaultTempDirPath = "../DerivedData/LocoLaserTemp/"
    override val defaultResourcesPath = "./"
    override val defaultResourceName = "Localizable"

    override val resources = IosResources(resourcesDir!!, resourceName!!)

}