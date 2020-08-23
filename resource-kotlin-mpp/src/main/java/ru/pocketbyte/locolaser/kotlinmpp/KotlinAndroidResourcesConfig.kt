package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAndroidResources

class KotlinAndroidResourcesConfig : ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-android"
    }

    override val type = ru.pocketbyte.locolaser.kotlinmpp.KotlinAndroidResourcesConfig.Companion.TYPE

    override val defaultResourcesPath = "./src/androidMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.AndroidStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinAndroidResources(resourcesDir!!, resourceName!!, interfaceName!!, filter)

}