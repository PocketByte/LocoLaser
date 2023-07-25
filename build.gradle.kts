// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.google.com")
        maven("https://plugins.gradle.org/m2/")
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildVersion.kotlin}")
    }
}

plugins {
    id("com.android.library") version "7.4.1" apply false
}

allprojects {

    version = LibraryInfo.version
    group = LibraryInfo.group

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}