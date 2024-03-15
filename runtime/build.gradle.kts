import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompileCommon
import ru.pocketbyte.kotlin.gradle.plugin.mpp_publish.upperFirstChar
import ru.pocketbyte.kotlin_mpp.plugin.publish.registerPlatformDependentPublishingTasks

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

android {
    compileSdk = AndroidSdk.compile

    defaultConfig {
        minSdk = AndroidSdk.min
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            //
        }
        getByName("debug") {
            //
        }
    }
}

kotlin {

    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    androidTarget {
        publishLibraryVariants("release")
    }

    js(IR) {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        jvmMain {
            dependsOn(commonMain.get())
            dependencies {
                implementation("com.ibm.icu:icu4j:73.2")
            }
        }

        jvmTest {
            dependsOn(commonTest.get())
        }

        iosMain {
            dependsOn(commonMain.get())
        }


        val iosX64Main by getting {
            dependsOn(iosMain.get())
        }

        val iosArm64Main by getting {
            dependsOn(iosMain.get())
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain.get())
        }

        jsMain {
            dependsOn(commonMain.get())
            dependencies {
                implementation(npm("i18next", "23.7.11"))
            }
        }
    }
    jvmToolchain(8)
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        sonatype(sonatypeUser, sonatypePassword)
    }
}

signing {
    sign(publishing.publications)
}

fun configurePomDefault(pom: MavenPom, targetName: String?) {
    pom.apply {
        name.set("locolaser-${project.name}-$targetName")
        description.set("Runtime dependency for LocoLaser.")
        url.set("https://github.com/PocketByte/LocoLaser")

        addCommonRepositoryProperties()
    }
}

// Configure Root publication
publishing {
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(javadocJar.get())
    }
    publications {
        val kotlinMultiplatform by getting {
            (this as? MavenPublication)?.apply {
                configurePomDefault(pom, null)
            }
        }
    }
}

// Configure Target publications
kotlin {
    targets.forEach {
        val targetName = it.name.upperFirstChar()
        if (it is KotlinAndroidTarget) {
            this@kotlin.android {
                afterEvaluate {
                    mavenPublication {
                        val variant = if (this.artifactId.endsWith("debug")) // FIXME
                        { "Debug"} else { "Release" }

                        configurePomDefault(pom, "$targetName $variant")
                    }
                }
            }
        } else {
            it.mavenPublication {
                configurePomDefault(pom, targetName)
            }
        }
    }
}

registerPlatformDependentPublishingTasks()

// Workaround for
// https://youtrack.jetbrains.com/issue/KT-46466/Kotlin-MPP-publishing-Gradle-7-disables-optimizations-because-of-task-dependencies
tasks.withType(Sign::class, configureAction = {
    val signingTask = this
    tasks.withType(AbstractPublishToMaven::class, configureAction = {
        this.dependsOn(signingTask)
    })
})