package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinAndroidResources

class KotlinAndroidPlatformConfig : KotlinBaseImplPlatformConfig() {

    companion object {
        const val TYPE = "kotlin-android"
    }

    var appPackage: String? = null

    override val type =  TYPE

    override val defaultResourceName =  "ru.pocketbyte.locolaser.StrAndroid"

    override val resources =  KotlinAndroidResources(resourcesDir!!, resourceName!!, interfaceName!!, appPackage!!)

}