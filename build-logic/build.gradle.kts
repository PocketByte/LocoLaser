plugins {
    `kotlin-dsl`
    `maven-publish`
}

repositories {
    mavenLocal()
    google()
    mavenCentral()
    gradlePluginPortal()
}

group = "ru.pocketbyte"

dependencies {
    implementation(libs.kotlin.gradle.plugin)
}
