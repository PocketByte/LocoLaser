plugins {
    id("java")
    id("kotlin")
    id("java-publishing-conventions")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val libs: VersionCatalog = the<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("kotlin-stdlib").get())
    implementation(libs.findLibrary("kotlin-stdlib-jdk8").get())
    testImplementation(libs.findLibrary("junit").get())
}
