plugins {
    id("java-library-convention")
    id("com.gradleup.nmcp")
}

dependencies {
    implementation(project(":core"))
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            description.set("Implementation of platform for LocoLaser tool to work with properties resources.")
        }
    }
}
