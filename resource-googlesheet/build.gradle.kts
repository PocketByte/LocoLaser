@file:Suppress("UnstableApiUsage")
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

plugins {
    id("kotlin")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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

    val copyAppProperties = register("copyAppProperties", Copy::class) {
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

    register("printGoogleApiKeys") {
        doLast {
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
    }

    findByName("processResources")?.finalizedBy(copyAppProperties)
    findByName("jar")?.dependsOn(copyAppProperties)
    findByName("test")?.dependsOn(copyAppProperties)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
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