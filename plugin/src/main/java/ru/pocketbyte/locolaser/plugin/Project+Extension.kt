package ru.pocketbyte.locolaser.plugin

import org.gradle.api.Project

/**
 * Configures the [LocalizationConfigContainer] for this project.
 *
 * @param action the block to configure localization.
 */
fun Project.localize(action: LocalizationConfigContainer.() -> Unit) {
    action(extensions.getByName("localize") as LocalizationConfigContainer)
}

/** Returns the [LocalizationConfigContainer] registered by the LocoLaser plugin. */
val Project.localize: LocalizationConfigContainer
    get() = extensions.getByName("localize") as LocalizationConfigContainer
