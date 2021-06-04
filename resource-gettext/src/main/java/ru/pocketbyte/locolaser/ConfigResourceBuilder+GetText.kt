package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfig
import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfigBuilder

/**
 * Create and configure GetText resources config.
 */
fun ConfigResourceBuilder.gettext(action: BaseResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = GetTextResourcesConfig()
    action(GetTextResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}