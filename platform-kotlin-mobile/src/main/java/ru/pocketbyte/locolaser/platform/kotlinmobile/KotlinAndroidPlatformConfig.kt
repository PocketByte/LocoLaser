package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinAndroidResources

class KotlinAndroidPlatformConfig : KotlinBaseImplPlatformConfig() {

    companion object {
        const val TYPE = "kotlin-android"
    }

    override val type =  TYPE

    override val defaultResourcesPath = "./src/androidMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.AndroidStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinAndroidResources(resourcesDir!!, resourceName!!, interfaceName!!)

}