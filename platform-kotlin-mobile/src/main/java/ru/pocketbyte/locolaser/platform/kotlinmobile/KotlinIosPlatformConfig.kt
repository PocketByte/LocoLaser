package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinIosResources

class KotlinIosPlatformConfig : KotlinBaseImplPlatformConfig() {

    companion object {
        const val TYPE = "kotlin-ios"
    }

    override val type = TYPE

    override val defaultResourceName = "ru.pocketbyte.locolaser.StrIos"

    override val resources = KotlinIosResources(resourcesDir!!, resourceName!!, interfaceName!!)

}