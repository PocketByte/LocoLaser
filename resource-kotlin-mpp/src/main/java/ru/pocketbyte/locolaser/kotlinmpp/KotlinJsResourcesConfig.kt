package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinJsResources

class KotlinJsResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    resourceName, resourcesDirPath, interfaceName, filter
) {

    companion object : ResourcesConfigBuilderFactory<KotlinJsResourcesConfig, KotlinJsResourcesConfigBuilder> {
        const val TYPE = "kotlin-js"

        override fun getBuilder(): KotlinJsResourcesConfigBuilder {
            return KotlinJsResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/jsMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.Js$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinJsResources(
            resourcesDir, resourceName, implements,
            resourceFileProvider, filter
        )

}