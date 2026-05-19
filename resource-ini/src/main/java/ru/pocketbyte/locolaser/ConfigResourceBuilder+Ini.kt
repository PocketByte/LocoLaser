package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.ini.IniResourcesConfigBuilder

/**
 * Creates and adds an INI resources configuration to this resources set.
 *
 * @param action Configuration block applied to the [IniResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.ini(action: IniResourcesConfigBuilder.() -> Unit) {
    add(IniResourcesConfigBuilder(), action)
}
