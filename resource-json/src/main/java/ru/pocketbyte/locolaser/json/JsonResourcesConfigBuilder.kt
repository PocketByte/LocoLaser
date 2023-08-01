package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

open class JsonResourcesConfigBuilder
    : BaseResourcesConfigBuilder<JsonResourcesConfig>(JsonResourcesConfig()) {

    /**
     * JSON indent. Set this property to prettify result JSON.
     */
    var indent: Int
        get() = config.indent
        set(value) { config.indent = value }

}