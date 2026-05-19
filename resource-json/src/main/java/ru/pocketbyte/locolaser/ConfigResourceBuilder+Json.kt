package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.json.JsonResourcesConfigBuilder

/**
 * Creates and adds a JSON resources configuration to this resources set.
 *
 * @param action Configuration block applied to the [JsonResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.json(action: JsonResourcesConfigBuilder.() -> Unit) {
    add(JsonResourcesConfigBuilder(), action)
}
