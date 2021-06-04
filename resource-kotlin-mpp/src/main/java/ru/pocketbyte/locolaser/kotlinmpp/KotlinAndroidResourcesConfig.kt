package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAndroidResources

class KotlinAndroidResourcesConfig : KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-android"
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/androidMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.AndroidStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinAndroidResources(resourcesDir!!, resourceName!!, implements!!, filter)

}