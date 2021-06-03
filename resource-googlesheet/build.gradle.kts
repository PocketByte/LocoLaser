@file:Suppress("UnstableApiUsage")
import java.util.Properties

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

    compile("com.google.gdata:core:${BuildVersion.googleGdata}")
    compile("com.google.api-client:google-api-client-java6:${BuildVersion.googleApiClient}")
    compile("com.google.oauth-client:google-oauth-client-jetty:${BuildVersion.googleAuthClient}")

    testImplementation("junit:junit:4.12")
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

    val copyAppProperties = create("copyAppProperties", Copy::class) {
        from("src/main/resources/properties")
        into("build/resources/main/properties")

        val propFile = project.rootProject.file("local.properties")
        if (propFile.exists()) {
            val propFileInputStreem = propFile.inputStream()
            val props = Properties()
            props.load(propFileInputStreem)
            val values = mutableMapOf(
                Pair("google_oauth_api_key", props["google_oauth_api_key"] ?: "none"),
                Pair("google_oauth_api_secret", props["google_oauth_api_secret"] ?: "none")
            )
            inputs.properties(values)
            expand(values)
            propFileInputStreem.close()
        }
    }

    create("printGoogleApiKeys").doLast {
        val propFile = project.rootProject.file("local.properties")
        if (propFile.exists()) {
            val propFileInputStreem = propFile.inputStream()
            val props = Properties()
            props.load(propFileInputStreem)

            println(props["google_oauth_api_key"] ?: "none")
            println(props["google_oauth_api_secret"] ?: "none")

            propFileInputStreem.close()
        } else {
            println("File 'local.properties' doesn't exists")
        }
    }

    findByName("processResources")?.finalizedBy(copyAppProperties)
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
                description.set("Implementation of source for LocoLaser tool to work with Google Sheets.")
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