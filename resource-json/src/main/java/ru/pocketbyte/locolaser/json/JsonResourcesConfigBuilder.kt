package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

open class JsonResourcesConfigBuilder(
    private val config: JsonResourcesConfig
): BaseResourcesConfigBuilder(config) {

    var indent: Int
        get() = config.indent
        set(value) { config.indent = value }

}