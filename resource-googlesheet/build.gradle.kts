@file:Suppress("UnstableApiUsage")
import java.util.Properties

plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
    id("signing")
}

dependencies {
    implementation(project(":core"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildVersion.kotlin}")

    implementation("com.google.api-client:google-api-client:${BuildVersion.googleApiClient}")
    implementation("com.google.oauth-client:google-oauth-client-jetty:${BuildVersion.googleAuthClient}")
    implementation("com.google.apis:google-api-services-sheets:${BuildVersion.googleSheets}")

    val testImplementation = testImplementation("junit:junit:4.13.2")
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
    findByName("jar")?.dependsOn(copyAppProperties)
    findByName("test")?.dependsOn(copyAppProperties)
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