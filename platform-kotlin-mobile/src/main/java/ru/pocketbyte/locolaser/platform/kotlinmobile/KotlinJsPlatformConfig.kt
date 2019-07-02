package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.platform.kotlinmobile.resource.KotlinJsResources

class KotlinJsPlatformConfig : KotlinBaseImplPlatformConfig() {

    companion object {
        const val TYPE = "kotlin-js"
    }

    override val type =  TYPE

    override val defaultResourcesPath = "./src/jsMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.JsStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinJsResources(resourcesDir!!, resourceName!!, interfaceName!!)

}