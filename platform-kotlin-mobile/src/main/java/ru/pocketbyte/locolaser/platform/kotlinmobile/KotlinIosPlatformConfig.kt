package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinIosResources
import ru.pocketbyte.locolaser.resource.PlatformResources

class KotlinIosPlatformConfig : KotlinBaseImplPlatformConfig() {

    companion object {
        const val TYPE = "kotlin-ios"
    }

    override fun getType(): String {
        return TYPE
    }

    override fun getDefaultResourceName(): String {
        return "ru.pocketbyte.locolaser.StrIos"
    }

    override fun getResources(): PlatformResources {
        return KotlinIosResources(resourcesDir, resourceName, interfaceName!!)
    }
}