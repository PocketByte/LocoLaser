package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAndroidResources
import java.io.File

class KotlinAndroidResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    workDir, resourceName, resourcesDirPath, interfaceName, filter
) {

    companion object : ResourcesConfigBuilderFactory<KotlinAndroidResourcesConfig, KotlinAndroidResourcesConfigBuilder> {
        const val TYPE = "kotlin-android"

        override fun getBuilder(): KotlinAndroidResourcesConfigBuilder {
            return KotlinAndroidResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/androidMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.Android$DEFAULT_INTERFACE_NAME"

    override val resources by lazy {
        KotlinAndroidResources(
            dir = this.resourcesDir,
            name = this.resourceName,
            interfaceName = this.implements,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }

}