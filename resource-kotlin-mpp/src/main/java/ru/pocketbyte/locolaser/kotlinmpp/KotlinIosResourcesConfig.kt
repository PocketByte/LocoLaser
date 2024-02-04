package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinIosResources
import java.io.File

class KotlinIosResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    workDir, resourceName, resourcesDirPath, interfaceName, filter
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

    override val resources by lazy {
        KotlinIosResources(
            dir = this.resourcesDir,
            name = this.resourceName,
            interfaceName = this.implements,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }

}