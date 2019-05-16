package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinAndroidResources
import ru.pocketbyte.locolaser.resource.PlatformResources

class KotlinAndroidPlatformConfig : KotlinBaseImplPlatformConfig() {

    companion object {
        const val TYPE = "kotlin-android"
    }

    var appPackage: String? = null

    override fun getType(): String {
        return TYPE
    }

    override fun getDefaultResourceName(): String {
        return "ru.pocketbyte.locolaser.StrAndroid"
    }

    override fun getResources(): PlatformResources {
        return KotlinAndroidResources(resourcesDir, resourceName, interfaceName!!, appPackage!!)
    }
}