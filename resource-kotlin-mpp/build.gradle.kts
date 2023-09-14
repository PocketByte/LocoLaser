@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":core"))
    implementation(project(":resource-mobile"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildVersion.kotlin}")
    implementation("com.squareup:kotlinpoet:${BuildVersion.kotlinPoet}")
    testImplementation("junit:junit:4.13.2")
}

tasks {
    create("sourceJar", Jar::class) {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }

    create("generateJavadocs", Javadoc::class) {
        source(sourceSets.main.get().allJava)
        isFailOnError = false
    }

    create("sourceDoc", Jar::class) {
        dependsOn("generateJavadocs")
        from(javadoc.get().destinationDir)
        archiveClassifier.set("javadoc")
    }
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

publishing {
    repositories {
        sonatype(sonatypeUser, sonatypePassword)
    }
    publications {
        create("locolaser", MavenPublication::class) {
            from(components.getByName("java"))
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                name.set("locolaser-${project.name}")
                description.set("Implementation of platform for LocoLaser tool to work with Kotlin MPP projects.")
                url.set("https://github.com/PocketByte/LocoLaser")

                addCommonRepositoryProperties()
            }

            artifact(tasks.getByPath("sourceJar"))
            artifact(tasks.getByPath("sourceDoc"))
        }
    }
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

signing {
    sign(publishing.publications["locolaser"])
}