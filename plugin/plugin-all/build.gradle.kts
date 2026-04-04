@file:Suppress("UnstableApiUsage")

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
    api(project(":plugin"))
    api(project(":resource-gettext"))
    api(project(":resource-googlesheet"))
    api(project(":resource-ini"))
    api(project(":resource-json"))
    api(project(":resource-kotlin-mpp"))
    api(project(":resource-mobile"))
    api(project(":resource-properties"))
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
        create("locolaserAllPlugin") {
            id = "ru.pocketbyte.locolaser.all"
            implementationClass = "ru.pocketbyte.locolaser.plugin.LocoLaserPluginAll"
            displayName = "Plugin for LocoLaser"
            description =
                "Extended Plugin for LocoLaser that also attaches all known dependencies."
            tags.set(listOf("localization", "tool"))
        }
    }
}
