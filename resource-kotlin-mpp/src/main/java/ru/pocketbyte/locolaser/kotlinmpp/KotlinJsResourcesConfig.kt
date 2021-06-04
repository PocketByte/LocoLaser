package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinJsResources

class KotlinJsResourcesConfig : KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-js"
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/jsMain/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.JsStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinJsResources(resourcesDir!!, resourceName!!, implements!!, filter)

}