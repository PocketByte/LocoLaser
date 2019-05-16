/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.resource.AndroidResources
import ru.pocketbyte.locolaser.resource.PlatformResources

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
class AndroidPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "android"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getDefaultTempDirPath(): String {
        return "./build/tmp/"
    }

    override fun getDefaultResourcesPath(): String {
        return "./src/main/res/"
    }

    override fun getDefaultResourceName(): String {
        return "strings"
    }

    override fun getResources(): PlatformResources {
        return AndroidResources(resourcesDir, resourceName)
    }

}