// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        jcenter()
        maven("https://maven.google.com")
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildVersion.kotlin}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:1.4.1")
        classpath("com.gradle.publish:plugin-publish-plugin:0.9.7")
    }
}

allprojects {

    version = LibraryInfo.version
    group = LibraryInfo.group

    configurations {
        // dependencies from this configuration will not be included in final jar file
        val noJarCompile by creating
    }

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}