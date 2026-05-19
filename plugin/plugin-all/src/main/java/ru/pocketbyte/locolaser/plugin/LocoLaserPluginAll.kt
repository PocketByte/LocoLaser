package ru.pocketbyte.locolaser.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/** LocoLaser Gradle plugin that bundles support for all resource formats. */
class LocoLaserPluginAll: Plugin<Project> {
    override fun apply(project: Project) {
        project.plugins.apply("ru.pocketbyte.locolaser")
    }
}
