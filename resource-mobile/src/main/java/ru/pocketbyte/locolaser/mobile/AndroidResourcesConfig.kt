/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.mobile.resource.AndroidResources

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
class AndroidResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "android"
    }

    override val type = TYPE

    override val defaultTempDirPath = "./build/tmp/"
    override val defaultResourcesPath = "./src/main/res/"
    override val defaultResourceName = "strings"

    override val resources
        get() = AndroidResources(resourcesDir, resourceName, filter)

}