package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinJsResources

class KotlinJsResourcesConfig : ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-js"
    }

    override val type = ru.pocketbyte.locolaser.kotlinmpp.KotlinJsResourcesConfig.Companion.TYPE

    override val defaultResourcesPath = "./src/jsMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.JsStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinJsResources(resourcesDir!!, resourceName!!, interfaceName!!, filter)

}