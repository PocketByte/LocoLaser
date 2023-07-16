package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinIosResources

class KotlinIosResourcesConfig : KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-ios"
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/iosMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.Ios$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinIosResources(resourcesDir, resourceName, implements!!, filter)

}