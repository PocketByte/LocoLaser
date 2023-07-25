@file:Suppress("UnstableApiUsage")
import kotlin.collections.listOf

plugins {
    id("kotlin")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.2.0"
    id("maven-publish")
}
dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation(project(":core"))
}

gradlePlugin {
    website.set("https://github.com/PocketByte/LocoLaser/")
    vcsUrl.set("https://github.com/PocketByte/LocoLaser/")
    plugins {
        create("locolaserPlugin") {
            id = "ru.pocketbyte.locolaser"
            implementationClass = "ru.pocketbyte.locolaser.plugin.LocoLaserPlugin"
            displayName = "Plugin for LocoLaser"
            description = "Plugin for LocoLaser"
            tags.set(listOf("localization", "tool"))
        }
    }
}
