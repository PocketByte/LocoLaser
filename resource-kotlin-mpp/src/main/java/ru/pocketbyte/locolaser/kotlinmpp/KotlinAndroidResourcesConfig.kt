package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAndroidResources

class KotlinAndroidResourcesConfig : KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-android"
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/androidMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.Android$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinAndroidResources(
            resourcesDir, resourceName, implements,
            resourceFileProvider, filter
        )

}