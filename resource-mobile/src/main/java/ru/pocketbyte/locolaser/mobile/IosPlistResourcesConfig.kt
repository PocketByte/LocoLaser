package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.mobile.resource.IosPlistResources
import ru.pocketbyte.locolaser.mobile.resource.file.provider.IosResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

/**
 * Resources configuration for iOS Info.plist localization.
 */
class IosPlistResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    filter: ResourcesFilter?
) : BaseResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: IosResourceFileProvider,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<IosPlistResourcesConfig, IosPlistResourcesConfigBuilder> {
        const val TYPE = "ios_plist"

        override fun getBuilder(): IosPlistResourcesConfigBuilder {
            return IosPlistResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./"
    override val defaultResourceName = "InfoPlist"

    override val resources: Resources by lazy {
        IosPlistResources(
            resourcesDir = this.resourcesDir,
            name = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }
}
