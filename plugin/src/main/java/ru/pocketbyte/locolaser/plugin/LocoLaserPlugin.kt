package ru.pocketbyte.locolaser.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class LocoLaserPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        val localizationConfig = project.extensions.create("localize", LocalizationConfigContainer::class.java)
        localizationConfig.project = project
    }
}