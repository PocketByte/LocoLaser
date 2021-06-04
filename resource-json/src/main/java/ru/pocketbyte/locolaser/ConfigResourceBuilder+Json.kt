package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.json.JsonResourcesConfig
import ru.pocketbyte.locolaser.json.JsonResourcesConfigBuilder

/**
 * Create and configure JSON resources config.
 */
fun ConfigResourceBuilder.json(action: JsonResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = JsonResourcesConfig()
    action(JsonResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}