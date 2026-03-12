@file:Suppress("UnstableApiUsage")

plugins {
    id("kotlin")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "1.2.0"
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    api(project(":plugin"))
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
        create("locolaserKmpPlugin") {
            id = "ru.pocketbyte.locolaser.kmp"
            implementationClass = "ru.pocketbyte.locolaser.plugin.LocoLaserPluginKmp"
            displayName = "KMP Plugin for LocoLaser"
            description =
                "Extended Plugin for LocoLaser that also attaches all required dependencies " +
                "for Kotlin Multiplatform projects."
            tags.set(listOf("localization", "tool", "multiplatform"))
        }
    }
}
