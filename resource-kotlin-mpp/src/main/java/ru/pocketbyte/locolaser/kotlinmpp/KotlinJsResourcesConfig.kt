package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinJsResources
import java.io.File

class KotlinJsResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    workDir, resourceName, resourcesDirPath, interfaceName, filter
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

    override val resources by lazy {
        KotlinJsResources(
            dir = this.resourcesDir,
            name = this.resourceName,
            interfaceName = this.implements,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }

}