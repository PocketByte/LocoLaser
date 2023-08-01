package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.json.JsonResourcesConfigBuilder

/**
 * Create and configure JSON resources config.
 */
fun ResourcesSetConfigBuilder.json(action: JsonResourcesConfigBuilder.() -> Unit) {
    add(JsonResourcesConfigBuilder(), action)
}