package ru.pocketbyte.locolaser.plugin

import org.gradle.api.Project

fun Project.localize(action: LocalizationConfigContainer.() -> Unit) {
    action(extensions.getByName("localize") as LocalizationConfigContainer)
}
