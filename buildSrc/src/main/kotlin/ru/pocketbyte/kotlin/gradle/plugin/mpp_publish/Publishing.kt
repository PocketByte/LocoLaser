package ru.pocketbyte.kotlin_mpp.plugin.publish

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import ru.pocketbyte.kotlin.gradle.plugin.mpp_publish.upperFirstChar

object Publishing {
    const val ROOT_TARGET = "KotlinMultiplatform"
    const val GROUP = "publishing"

    internal val AppleOnlyTargets = arrayOf(
            "macosX64", "macosArm64",
            "iosX64", "iosArm64", "iosArm32", "iosSimulatorArm64",
            "watchosArm32", "watchosArm64", "watchosX86",
            "watchosX64", "watchosSimulatorArm64",
            "tvosArm64", "tvosX64", "tvosSimulatorArm64"
    )
    internal val MipsOnlyTargets = arrayOf(
            "linuxMips32", "linuxMipsel32"
    )

    internal val WindowsOnlyTargets = arrayOf(
            "mingwX64", "mingwX86"
    )

    internal const val ApplePlatformName = "Apple"
    internal const val MipsPlatformName = "Mips"
    internal const val WindowsPlatformName = "Windows"
}

fun Project.publishingTask(targetName: String, repoName: String?): Task? {
    return if (repoName != null) {
        project.tasks.findByName(
                "publish${targetName.upperFirstChar()}PublicationTo${repoName.upperFirstChar()}Repository")
    } else {
        project.tasks.findByName(
                "publish${targetName.upperFirstChar()}PublicationToMavenLocal")
    }
}

fun Project.visitPublishingTasks(targetName: String, action: (task: Task, repoName: String?) -> Unit) {
    publishing.repositories.forEach { repo ->
        val repoName = repo.name.upperFirstChar()

        publishingTask(targetName, repoName)?.let {
            action(it, repoName)
        }
    }

    publishingTask(targetName, null)?.let {
        action(it, null)
    }
}

fun Project.registerPlatformDependentPublishingTasks() {
    publishing.repositories.forEach { repo ->
        registerPlatformDependentPublishingTasks(repo.name)
    }

    registerPlatformDependentPublishingTasks(null)
}

fun Project.registerPlatformDependentPublishingTasks(repoName: String?) {
    registerPlatformDependentTasks(Publishing.ApplePlatformName, repoName,
            Publishing.AppleOnlyTargets)
    registerPlatformDependentTasks(Publishing.MipsPlatformName, repoName,
            Publishing.MipsOnlyTargets)
    registerPlatformDependentTasks(Publishing.WindowsPlatformName, repoName,
            Publishing.WindowsOnlyTargets)
}

private fun Project.registerPlatformDependentTasks(
        platform: String, repoName: String?, targets: Array<String>) {

    val publishingTasks = targets.mapNotNull {
        publishingTask(it, repoName)
    }
    if (publishingTasks.isNotEmpty()) {
        val taskName = if (repoName != null) {
            "publishAll${platform.upperFirstChar()}To${repoName.upperFirstChar()}Repository"
        } else {
            "publishAll${platform.upperFirstChar()}ToMavenLocal"
        }
        tasks.register(taskName) {
            group = Publishing.GROUP
            dependsOn(*publishingTasks.toTypedArray())
        }
    }
}

private val Project.publishing: PublishingExtension get() =
    (this as ExtensionAware).extensions.getByName("publishing") as PublishingExtension
