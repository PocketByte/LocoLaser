package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.properties.PropertiesResourcesConfigBuilder

/**
 * Create and configure Properties resources config.
 */
fun ResourcesSetConfigBuilder.properties(action: PropertiesResourcesConfigBuilder.() -> Unit) {
    add(PropertiesResourcesConfigBuilder(), action)
}