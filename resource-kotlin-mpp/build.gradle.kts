plugins {
    id("java-library-convention")
    id("com.gradleup.nmcp")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":resource-mobile"))
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.kotlinPoet)
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            description.set("Implementation of platform for LocoLaser tool to work with Kotlin MPP projects.")
        }
    }
}
