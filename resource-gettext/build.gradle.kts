@file:Suppress("UnstableApiUsage")

plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
    id("signing")
    id("com.gradleup.nmcp")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(project(":core"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.stdlib.jdk8)
    testImplementation(libs.junit)
}

tasks {
    register("sourceJar", Jar::class) {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }

    register("generateJavadocs", Javadoc::class) {
        source(sourceSets.main.get().allJava)
        isFailOnError = false
    }

    register("sourceDoc", Jar::class) {
        dependsOn("generateJavadocs")
        from(javadoc.get().destinationDir)
        archiveClassifier.set("javadoc")
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

publishing {
    publications {
        create("locolaser", MavenPublication::class) {
            from(components.getByName("java"))
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                name.set("locolaser-${project.name}")
                description.set("Implementation of platform for LocoLaser tool to work with gettext resources.")
                url.set("https://github.com/PocketByte/LocoLaser")

                addCommonRepositoryProperties()
            }

            artifact(tasks.getByPath("sourceJar"))
            artifact(tasks.getByPath("sourceDoc"))
        }
    }
}

signing {
    sign(publishing.publications["locolaser"])
}