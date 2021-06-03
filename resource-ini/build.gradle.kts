@file:Suppress("UnstableApiUsage")

plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
    id("signing")
}

configurations { compile.get().extendsFrom(named("noJarCompile").get()) }

dependencies {
    add("noJarCompile", project(":core"))
    compile("org.jetbrains.kotlin:kotlin-stdlib")
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildVersion.kotlin}")
    testCompile("junit:junit:4.12")
}

tasks {
    jar {
        // Include all libraries into result JAR file.
        from(
            (configurations.compile.get() - configurations.named("noJarCompile").get())
                .map { if(it.isDirectory()) { it } else { zipTree(it) }})
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
            from(components.getByName("java"))
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
            pom {
                withXml {
                    val root = asNode()
                    root.remove((root.get("dependencies") as groovy.util.NodeList).first() as groovy.util.Node?)

                    val dependencies = root.appendNode("dependencies")

                    // Add core dependency
                    val coreDependency = dependencies.appendNode("dependency")
                    coreDependency.appendNode("groupId", "ru.pocketbyte.locolaser")
                    coreDependency.appendNode("artifactId", "core")
                    coreDependency.appendNode("version", project.version)
                    coreDependency.appendNode("scope", "runtime")
                }
                name.set("locolaser-${project.name}")
                description.set("Implementation of platform for LocoLaser tool to work with ini resources.")
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