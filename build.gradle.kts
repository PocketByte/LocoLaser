import java.time.temporal.ChronoUnit

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
    id("com.android.library") version "8.13.0" apply false
    id("com.gradleup.nmcp.aggregation").version("1.4.4")
}

nmcpAggregation {
    centralPortal {
        username = providers.gradleProperty("sonatype.publish.user").get() ?: ""
        password = providers.gradleProperty("sonatype.publish.password").get() ?: ""

        // optional: publish manually from the portal
        publishingType = "USER_MANAGED"

        // optional: configure the name of your publication in the portal UI
        publicationName = "${LibraryInfo.group}:${LibraryInfo.version}"

        // optional: increase the validation timeout to 30 minutes
        validationTimeout = java.time.Duration.of(30, ChronoUnit.MINUTES)

        // optional: send publications serially instead of in parallel (might be slower)
        uploadSnapshotsParallelism.set(1)
    }
}

dependencies {
    allprojects {
        nmcpAggregation(project(path))
    }
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