package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsProxyResources
import java.io.File

class KotlinAbsProxyResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    workDir, resourceName, resourcesDirPath, interfaceName, filter
) {

    companion object : ResourcesConfigBuilderFactory<KotlinAbsProxyResourcesConfig, KotlinAbsProxyResourcesConfigBuilder> {
        const val TYPE = "kotlin-abs-proxy"
        override fun getBuilder(): KotlinAbsProxyResourcesConfigBuilder {
            return KotlinAbsProxyResourcesConfigBuilder()
        }
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/commonMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.AbsProxy$DEFAULT_INTERFACE_NAME"

    override val resources by lazy {
        KotlinAbsProxyResources(
            dir = this.resourcesDir,
            name = this.resourceName,
            interfaceName = this.implements,
            resourceFileProvider = this.resourceFileProvider,
            filter = this.filter
        )
    }
}