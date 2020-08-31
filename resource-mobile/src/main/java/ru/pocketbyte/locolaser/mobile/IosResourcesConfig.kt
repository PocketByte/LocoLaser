/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.mobile.resource.IosResources

/**
 * iOS platform configuration.
 *
 * @author Denis Shurygin
 */
class IosResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "ios"
    }

    override val type = TYPE

    override val defaultTempDirPath = "../DerivedData/LocoLaserTemp/"
    override val defaultResourcesPath = "./"
    override val defaultResourceName = "Localizable"

    override val resources
        get() = IosResources(resourcesDir!!, resourceName!!, filter)

}