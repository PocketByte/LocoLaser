import java.time.temporal.ChronoUnit
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

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

// Force minimum versions for vulnerable transitive NPM dependencies (kotlin-js-store/yarn.lock)
rootProject.plugins.withType<YarnPlugin> {
    rootProject.the<YarnRootExtension>().apply {
        // HIGH severity
        resolution("serialize-javascript", ">=7.0.5")
        resolution("flatted", ">=3.4.2")
        resolution("socket.io-parser", ">=4.2.6")
        resolution("minimatch", ">=3.1.4")
        resolution("body-parser", ">=1.20.3")
        resolution("ws", ">=8.17.1")
        // MEDIUM severity
        resolution("brace-expansion", ">=2.0.3")
        resolution("picomatch", ">=2.3.2")
        resolution("ajv", ">=6.14.0")
        resolution("lodash", "4.17.21") // 4.18.0 is a bad release; 4.17.21 is latest stable
        resolution("qs", ">=6.14.2")
        resolution("js-yaml", ">=4.1.1")
        resolution("@babel/runtime", ">=7.26.10")
        resolution("nanoid", ">=3.3.8")
        resolution("webpack", ">=5.104.1")
        // LOW severity
        resolution("diff", ">=5.2.2")
        resolution("tmp", ">=0.2.4")
        resolution("cookie", ">=0.7.0")
    }
}