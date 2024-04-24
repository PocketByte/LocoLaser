/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.mobile.resource.AndroidResources
import ru.pocketbyte.locolaser.mobile.resource.file.provider.AndroidResourceFileProvider
import java.io.File

/**
 * Android platform configuration.
 *
 * @author Denis Shurygin
 */
class AndroidResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    filter: ((key: String) -> Boolean)?
) : BaseResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: AndroidResourceFileProvider,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<AndroidResourcesConfig, AndroidResourcesConfigBuilder> {
        const val TYPE = "android"

        override fun getBuilder(): AndroidResourcesConfigBuilder {
            return AndroidResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultTempDirPath = "./build/tmp/"
    override val defaultResourcesPath = "./src/main/res/"
    override val defaultResourceName = "strings"

    override val resources by lazy {
        AndroidResources(
            resourcesDir = this.resourcesDir,
            fileName = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }

}