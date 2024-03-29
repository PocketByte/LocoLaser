/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.gettext

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.gettext.resource.GetTextResources
import ru.pocketbyte.locolaser.resource.Resources

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
class GetTextResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "gettext"
    }

    override val defaultTempDirPath = "./build/tmp/"

    override val defaultResourcesPath = "./languages/"

    override val defaultResourceName = "messages"

    override val type = TYPE

    override val resources: Resources
        get() = GetTextResources(resourcesDir, resourceName, filter)

}