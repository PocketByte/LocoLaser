package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinIosResources

class KotlinIosResourcesConfig : ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-ios"
    }

    override val type = ru.pocketbyte.locolaser.kotlinmpp.KotlinIosResourcesConfig.Companion.TYPE

    override val defaultResourcesPath = "./src/iosMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.IosStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinIosResources(resourcesDir!!, resourceName!!, interfaceName!!, filter)

}