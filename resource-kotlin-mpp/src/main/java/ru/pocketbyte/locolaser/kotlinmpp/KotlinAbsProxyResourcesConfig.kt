package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsProxyResources

class KotlinAbsProxyResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    interfaceName: String?,
    filter: ((key: String) -> Boolean)?
) : KotlinBaseResourcesConfig(
    resourceName, resourcesDirPath, interfaceName, filter
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

    override val resources
        get() = KotlinAbsProxyResources(
            resourcesDir, resourceName, implements,
            resourceFileProvider, filter
        )
}