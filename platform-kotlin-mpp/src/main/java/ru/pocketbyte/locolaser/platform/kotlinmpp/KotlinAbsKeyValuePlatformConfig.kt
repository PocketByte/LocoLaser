package ru.pocketbyte.locolaser.platform.kotlinmpp

import ru.pocketbyte.locolaser.platform.kotlinmpp.resource.KotlinAbsKeyValueResources

class KotlinAbsKeyValuePlatformConfig : KotlinBaseImplPlatformConfig() {

    companion object {
        const val TYPE = "kotlin-abs-key-value"
    }

    override val type =  TYPE

    override val defaultResourcesPath = "./src/androidMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.AbsKeyValueStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinAbsKeyValueResources(resourcesDir!!, resourceName!!, interfaceName!!, filter)

}