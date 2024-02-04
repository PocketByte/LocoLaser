package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.mobile.resource.IosObjectiveCResources
import java.io.File

class IosObjectiveCResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    tableName: String?,
    filter: ((key: String) -> Boolean)?
) : IosBaseClassResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider,
    tableName,
    filter
) {

    companion object : ResourcesConfigBuilderFactory<IosObjectiveCResourcesConfig, IosObjectiveCResourcesConfigBuilder> {
        const val TYPE = "ios_objc"

        override fun getBuilder(): IosObjectiveCResourcesConfigBuilder {
            return IosObjectiveCResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val resources by lazy {
        IosObjectiveCResources(
            resourcesDir = this.resourcesDir,
            name = this.resourceName,
            resourceFileProvider = this.resourceFileProvider,
            tableName = this.tableName,
            filter = this.filter
        )
    }

}
