@file:Suppress("UnstableApiUsage")
import kotlin.collections.listOf

plugins {
    id("kotlin")
    id("java-gradle-plugin")
    alias(libs.plugins.gradle.pluginPublish)
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation(libs.kotlin.stdlib)
    api(project(":core"))
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

gradlePlugin {
    website.set("https://github.com/PocketByte/LocoLaser/")
    vcsUrl.set("https://github.com/PocketByte/LocoLaser/")
    plugins {
        create("locolaserPlugin") {
            id = "ru.pocketbyte.locolaser"
            implementationClass = "ru.pocketbyte.locolaser.plugin.LocoLaserPlugin"
            displayName = "Minimalistic Plugin for LocoLaser"
            description =
                "Plugin for LocoLaser without any dependency. " +
                "You should provide all necessary dependencies manually."
            tags.set(listOf("localization", "tool"))
        }
    }
}
