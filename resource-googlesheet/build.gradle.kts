import java.util.Properties

plugins {
    id("java-library-convention")
    id("com.gradleup.nmcp")
}

dependencies {
    implementation(project(":core"))
    implementation(libs.google.api.client)
    implementation(libs.google.oauth.client.jetty)
    implementation(libs.google.api.services.sheets)
}

tasks {
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

publishing {
    publications.withType<MavenPublication> {
        pom {
            description.set("Implementation of source for LocoLaser tool to work with Google Sheets.")
        }
    }
}
