package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.properties.PropertiesResourcesConfigBuilder

/**
 * Creates and adds a Properties resources configuration to this resources set.
 *
 * @param action Configuration block applied to the [PropertiesResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.properties(action: PropertiesResourcesConfigBuilder.() -> Unit) {
    add(PropertiesResourcesConfigBuilder(), action)
}
