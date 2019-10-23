/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.gettext

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.platform.gettext.resource.GetTextResources
import ru.pocketbyte.locolaser.resource.PlatformResources

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
class GetTextPlatformConfig : BasePlatformConfig() {

    companion object {
        const val TYPE = "gettext"
    }

    override val defaultTempDirPath = "./build/tmp/"

    override val defaultResourcesPath = "./languages/"

    override val defaultResourceName = "messages"

    override val type = TYPE

    override val resources: PlatformResources
        get() = GetTextResources(resourcesDir!!, resourceName!!, filter)

}