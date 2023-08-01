package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ResourcesSetConfigBuilder
import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfigBuilder

/**
 * Create and configure Google Sheet resources config.
 */
fun ResourcesSetConfigBuilder.googleSheet(action: GoogleSheetResourcesConfigBuilder.() -> Unit) {
    add(GoogleSheetResourcesConfigBuilder(), action)
}

/**
 * Create and configure Google Sheet resources config.
 */
@Deprecated(
    message = "This extension is deprecated. Use `googleSheet` instead.",
    replaceWith = ReplaceWith("googleSheet(action)")
)
fun ResourcesSetConfigBuilder.googlesheet(action: GoogleSheetResourcesConfigBuilder.() -> Unit) {
    add(GoogleSheetResourcesConfigBuilder(), action)
}