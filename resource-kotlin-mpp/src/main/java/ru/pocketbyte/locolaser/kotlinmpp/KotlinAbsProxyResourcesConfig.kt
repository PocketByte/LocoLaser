package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsProxyResources

class KotlinAbsProxyResourcesConfig : KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-abs-proxy"
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/commonMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.AbsProxy$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinAbsProxyResources(resourcesDir, resourceName, implements, filter)
}