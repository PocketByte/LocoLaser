/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.mobile.resource.IosResources
import ru.pocketbyte.locolaser.mobile.resource.file.provider.IosResourceFileProvider
import java.io.File

/**
 * iOS platform configuration.
 *
 * @author Denis Shurygin
 */
class IosResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    filter: ((key: String) -> Boolean)?
) : BaseResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: IosResourceFileProvider,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<IosResourcesConfig, IosResourcesConfigBuilder> {
        const val TYPE = "ios"

        override fun getBuilder(): IosResourcesConfigBuilder {
            return IosResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./"
    override val defaultResourceName = "Localizable"

    override val resources by lazy {
        IosResources(
            resourcesDir = this.resourcesDir,
            name = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }
}
