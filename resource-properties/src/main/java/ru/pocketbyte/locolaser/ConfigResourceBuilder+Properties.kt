package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.properties.PropertiesResourceConfig

fun ConfigResourceBuilder.properties(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = PropertiesResourceConfig()
    action(BaseResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}