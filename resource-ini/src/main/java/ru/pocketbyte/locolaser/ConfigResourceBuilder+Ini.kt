package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.ini.IniResourceConfig

fun ConfigResourceBuilder.ini(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = IniResourceConfig()
    action(BaseResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}