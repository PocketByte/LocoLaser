package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinIosResources

class KotlinIosResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    resourceName, resourcesDirPath, interfaceName, filter
) {

    companion object : ResourcesConfigBuilderFactory<KotlinIosResourcesConfig, KotlinIosResourcesConfigBuilder> {
        const val TYPE = "kotlin-ios"

        override fun getBuilder(): KotlinIosResourcesConfigBuilder {
            return KotlinIosResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/iosMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.Ios$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinIosResources(
            resourcesDir, resourceName, implements,
            resourceFileProvider, filter
        )

}