package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.mobile.resource.IosObjectiveCResources

class IosObjectiveCResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    tableName: String?,
    filter: ((key: String) -> Boolean)?
) : IosBaseClassResourcesConfig(
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

    override val resources
        get() = IosObjectiveCResources(
            resourcesDir, resourceName, resourceFileProvider,
            tableName, filter
        )

}
