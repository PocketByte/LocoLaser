package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.ini.IniResourceConfig
import ru.pocketbyte.locolaser.ini.IniResourceConfigBuilder

/**
 * Create and configure Ini resources config.
 */
fun ConfigResourceBuilder.ini(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IniResourceConfig()
    action(IniResourceConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}