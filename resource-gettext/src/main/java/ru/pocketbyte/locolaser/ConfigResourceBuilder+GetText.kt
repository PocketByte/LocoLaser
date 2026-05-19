package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfigBuilder

/**
 * Creates and adds a GetText resources configuration to this resources set.
 *
 * GetText is a widely used localization format based on `.po` / `.pot` files.
 *
 * @param action Configuration block applied to the [GetTextResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.gettext(action: GetTextResourcesConfigBuilder.() -> Unit) {
    add(GetTextResourcesConfigBuilder(), action)
}
