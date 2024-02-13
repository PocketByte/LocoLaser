@file:Suppress("UnstableApiUsage")
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.collections.listOf

plugins {
    id("kotlin")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.2.0"
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
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
