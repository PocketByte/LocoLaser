plugins {
    id("java-library-convention")
    id("com.gradleup.nmcp")
}

dependencies {
    api(project(":runtime"))
    api(libs.json.simple)
    api(gradleApi())
    implementation(libs.jcommander)
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            description.set("Core library of LocoLaser tool")
        }
    }
}
