package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfigBuilder

/**
 * Create and configure GetText resources config.
 */
fun ResourcesSetConfigBuilder.gettext(action: GetTextResourcesConfigBuilder.() -> Unit) {
    add(GetTextResourcesConfigBuilder(), action)
}