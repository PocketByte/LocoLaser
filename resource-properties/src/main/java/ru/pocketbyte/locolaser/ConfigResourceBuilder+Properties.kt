package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.properties.PropertiesResourceConfig
import ru.pocketbyte.locolaser.properties.PropertiesResourceConfigBuilder

/**
 * Create and configure Properties resources config.
 */
fun ConfigResourceBuilder.properties(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = PropertiesResourceConfig()
    action(PropertiesResourceConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}