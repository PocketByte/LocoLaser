@file:Suppress("UnstableApiUsage")
import kotlin.collections.listOf

plugins {
    id("kotlin")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish")
    id("maven-publish")
}
dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(project(":core"))
}

pluginBundle {
    website = "https://github.com/PocketByte/LocoLaser/"
    vcsUrl = "https://github.com/PocketByte/LocoLaser/"
    description = "Plugin for LocoLaser"
    tags = listOf("localization", "tool")
}

gradlePlugin {
    plugins {
        create("locolaserPlugin") {
            id = "ru.pocketbyte.locolaser"
            implementationClass = "ru.pocketbyte.locolaser.plugin.LocoLaserPlugin"
            displayName = "Plugin for LocoLaser"
        }
    }
}