package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder
import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfigBuilder
import ru.pocketbyte.locolaser.google.sheet.GoogleSheetConfig

/**
 * Create and configure Google Sheet resources config.
 */
fun ConfigResourceBuilder.googlesheet(action: GoogleSheetResourcesConfigBuilder.() -> Unit) {
    val resourcesConfig = GoogleSheetConfig()
    action(GoogleSheetResourcesConfigBuilder(resourcesConfig))
    add(resourcesConfig)
}