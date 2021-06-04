package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

open class JsonResourcesConfigBuilder(
    private val config: JsonResourcesConfig
): BaseResourcesConfigBuilder(config) {

    /**
     * JSON indent. Set this property to prettify result JSON.
     */
    var indent: Int
        get() = config.indent
        set(value) { config.indent = value }

}