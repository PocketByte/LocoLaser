package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.mobile.resource.IosSwiftResources
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

/**
 * Resources configuration for the iOS Swift class generator.
 */
class IosSwiftResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    tableName: String?,
    filter: ResourcesFilter?
) : IosBaseClassResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider,
    tableName,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<IosSwiftResourcesConfig, IosSwiftResourcesConfigBuilder> {
        const val TYPE = "ios_swift"

        override fun getBuilder(): IosSwiftResourcesConfigBuilder {
            return IosSwiftResourcesConfigBuilder()
        }
    }

    override val type = TYPE
    override val resources: Resources by lazy {
        IosSwiftResources(
            resourcesDir = this.resourcesDir,
            name = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            tableName = this.tableName,
            filter = this.filter
        )
    }
}
