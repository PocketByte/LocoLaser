package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfigBuilder

/**
 * Creates and adds a Google Sheet resources configuration to this resources set.
 *
 * @param action Configuration block applied to the [GoogleSheetResourcesConfigBuilder].
 */
fun ResourcesSetConfigBuilder.googleSheet(action: GoogleSheetResourcesConfigBuilder.() -> Unit) {
    add(GoogleSheetResourcesConfigBuilder(), action)
}

/**
 * Creates and adds a Google Sheet resources configuration to this resources set.
 *
 * @param action Configuration block applied to the [GoogleSheetResourcesConfigBuilder].
 */
@Deprecated(
    message = "This extension is deprecated. Use `googleSheet` instead.",
    replaceWith = ReplaceWith("googleSheet(action)"),
    level = DeprecationLevel.WARNING
)
fun ResourcesSetConfigBuilder.googlesheet(action: GoogleSheetResourcesConfigBuilder.() -> Unit) {
    add(GoogleSheetResourcesConfigBuilder(), action)
}
