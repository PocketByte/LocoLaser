/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.platform.mobile.resource.WpResources

/**
 * Windows Phone platform configuration.
 *
 * @author Denis Shurygin
 */
class WpPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "wp"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getDefaultTempDirPath(): String {
        return "../tmp/"
    }

    override fun getDefaultResourcesPath(): String {
        return "./Strings/"
    }

    override fun getDefaultResourceName(): String {
        return "Resources.resw"
    }

    override fun getResources(): PlatformResources {
        return WpResources(resourcesDir, resourceName)
    }
}