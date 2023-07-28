/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.json.resource.JsonResources
import ru.pocketbyte.locolaser.json.resource.file.provider.JsonResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

/**
 * JSON resources configuration.
 *
 * @author Denis Shurygin
 */
class JsonResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val TYPE = "json"
    }

    var indent: Int = -1

    override val type = TYPE

    override val defaultTempDirPath = "./build/tmp/"
    override val defaultResourcesPath = "./locales/"
    override val defaultResourceName = "strings"

    override var resourceFileProvider: ResourceFileProvider = JsonResourceFileProvider()

    override val resources: Resources
        get() = JsonResources(resourcesDir, resourceName, resourceFileProvider, indent, filter)

}