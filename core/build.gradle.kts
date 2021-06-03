@file:Suppress("UnstableApiUsage")

import groovy.util.Node
import groovy.util.NodeList

plugins {
    id("java")
    id("kotlin")
    id("org.jetbrains.kotlin.jvm")
    id("application")
    id("maven-publish")
    id("signing")
}

application {
    mainClass.set("ru.pocketbyte.locolaser.Main")
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildVersion.kotlin}")
    compile("com.beust:jcommander:1.48")
    compile("com.googlecode.json-simple:json-simple:1.1.1")
    testImplementation("junit:junit:4.12")
}

tasks {
    jar {
        // Include all libraries into result JAR file.
        from(configurations.compile.get().map { if(it.isDirectory()) { it } else { zipTree(it) }})
    }

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

publishing {
    repositories {
        sonatype(sonatypeUser, sonatypePassword)
    }
    publications {
        create("locolaser", MavenPublication::class) {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                withXml {
                    val root = asNode()
                    root.remove((root.get("dependencies") as NodeList).first() as Node?)
                }

                name.set("locolaser-${project.name}")
                description.set("Core library of LocoLaser tool")
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