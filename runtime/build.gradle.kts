import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import ru.pocketbyte.kotlin.gradle.plugin.mpp_publish.upperFirstChar
import ru.pocketbyte.kotlin_mpp.plugin.publish.registerPlatformDependentPublishingTasks

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

android {
    compileSdk = AndroidSdk.compile
    buildToolsVersion = BuildVersion.androidTool

    defaultConfig {
        minSdk = AndroidSdk.min
        targetSdk = AndroidSdk.target
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

kotlin {
    jvm()
    ios()
    iosSimulatorArm64()

    android {
        publishLibraryVariants("release")
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib")
            }
        }

        val commonTest by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)
        }

        val jvmTest by getting {
            dependsOn(jvmMain)
            dependsOn(commonTest)
        }

//        val jsMain by getting {
//            dependsOn(commonMain)
//            dependencies {
//                api("ru.pocketbyte.locolaser:i18next-externals:1.0")
//            }
//        }
    }
    jvmToolchain(8)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        sonatype(sonatypeUser, sonatypePassword)
    }
}


//signing {
//    sign(publishing.publications)
//}

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
        val targetName = name.upperFirstChar()
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