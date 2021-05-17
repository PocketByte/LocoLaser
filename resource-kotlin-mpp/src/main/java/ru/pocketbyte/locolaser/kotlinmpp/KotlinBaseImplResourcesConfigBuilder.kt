package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

open class KotlinBaseImplResourcesConfigBuilder(
    private val config: KotlinBaseImplResourcesConfig
): BaseResourcesConfigBuilder(config) {

    var implements: String?
        get() = config.implements
        set(value) { config.implements = value }

}