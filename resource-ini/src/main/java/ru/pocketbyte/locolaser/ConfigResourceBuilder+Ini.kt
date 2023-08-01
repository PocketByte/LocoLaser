package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.ini.IniResourcesConfigBuilder

/**
 * Create and configure Ini resources config.
 */
fun ResourcesSetConfigBuilder.ini(action: IniResourcesConfigBuilder.() -> Unit) {
    add(IniResourcesConfigBuilder(), action)
}