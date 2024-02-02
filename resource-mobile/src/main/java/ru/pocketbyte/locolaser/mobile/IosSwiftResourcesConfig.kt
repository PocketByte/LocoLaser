package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.mobile.resource.IosSwiftResources

class IosSwiftResourcesConfig(
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

    companion object : ResourcesConfigBuilderFactory<IosSwiftResourcesConfig, IosSwiftResourcesConfigBuilder> {
        const val TYPE = "ios_swift"

        override fun getBuilder(): IosSwiftResourcesConfigBuilder {
            return IosSwiftResourcesConfigBuilder()
        }
    }

    override val type = TYPE
    override val resources
        get() = IosSwiftResources(
            resourcesDir, resourceName, resourceFileProvider,
            tableName, filter
        )

}
