/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.json

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.json.resource.JsonResources
import ru.pocketbyte.locolaser.resource.PlatformResources

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
class JsonPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "json"
    }

    override val defaultTempDirPath = "./build/tmp/"

    override val defaultResourcesPath = "./locales/"

    override val defaultResourceName = "strings"

    override val type = TYPE

    override val resources: PlatformResources
        get() = JsonResources(resourcesDir!!, resourceName!!)

}