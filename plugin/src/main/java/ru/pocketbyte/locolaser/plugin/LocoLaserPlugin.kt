package ru.pocketbyte.locolaser.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/** Base LocoLaser Gradle plugin that registers the [LocalizationConfigContainer] as the `localize` extension. */
class LocoLaserPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val localizationConfig = project.extensions
            .create("localize", LocalizationConfigContainer::class.java, project)
    }
}
