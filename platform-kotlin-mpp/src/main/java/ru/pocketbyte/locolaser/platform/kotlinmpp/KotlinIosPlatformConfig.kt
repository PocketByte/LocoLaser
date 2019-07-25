package ru.pocketbyte.locolaser.platform.kotlinmpp

import ru.pocketbyte.locolaser.platform.kotlinmpp.resource.KotlinIosResources

class KotlinIosPlatformConfig : KotlinBaseImplPlatformConfig() {

    companion object {
        const val TYPE = "kotlin-ios"
    }

    override val type = TYPE

    override val defaultResourcesPath = "./src/iosMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.IosStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinIosResources(resourcesDir!!, resourceName!!, interfaceName!!)

}