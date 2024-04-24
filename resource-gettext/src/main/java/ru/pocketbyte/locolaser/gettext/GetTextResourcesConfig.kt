/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.gettext

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.gettext.resource.GetTextResources
import ru.pocketbyte.locolaser.gettext.resource.file.provider.GetTextResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
class GetTextResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    filter: ((key: String) -> Boolean)?
) : BaseResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: GetTextResourceFileProvider,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<GetTextResourcesConfig, GetTextResourcesConfigBuilder> {
        const val TYPE = "gettext"

        override fun getBuilder(): GetTextResourcesConfigBuilder {
            return GetTextResourcesConfigBuilder()
        }
    }

    override val type = TYPE
    override val defaultTempDirPath = "./build/tmp/"
    override val defaultResourcesPath = "./languages/"
    override val defaultResourceName = "messages"

    override val resources: Resources by lazy {
        GetTextResources(
            resourcesDir = this.resourcesDir,
            fileName = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }
}