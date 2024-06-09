package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.mobile.resource.IosPlistResources
import ru.pocketbyte.locolaser.mobile.resource.file.provider.IosResourceFileProvider
import java.io.File

/**
 * iOS Info.plist localisation configuration.
 *
 * @author Denis Shurygin
 */
class IosPlistResourcesConfig(
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

    companion object : ResourcesConfigBuilderFactory<IosPlistResourcesConfig, IosPlistResourcesConfigBuilder> {
        const val TYPE = "ios_plist"

        override fun getBuilder(): IosPlistResourcesConfigBuilder {
            return IosPlistResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./"
    override val defaultResourceName = "InfoPlist"

    override val resources by lazy {
        IosPlistResources(
            resourcesDir = this.resourcesDir,
            name = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }
}
