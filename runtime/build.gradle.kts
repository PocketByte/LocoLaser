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


// =================================
// Common Source Sets
kotlin {
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

        nativeMain {
            dependsOn(commonMain.get())
        }

        nativeTest {
            dependsOn(commonTest.get())
        }
    }

    // Fix to generate unique name in klib manifest for commonMain artifact
    // https://youtrack.jetbrains.com/issue/KT-57914/Task-compileIosMainKotlinMetadata-during-build-task-fails-without-kotlin.mpp.hierarchicalStructureSupportfalse
    metadata {
        compilations.configureEach {
            if (name == KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME) {
                compileTaskProvider {
                    this as KotlinCompileCommon
                    moduleName.set("${project.group}:${moduleName.get()}")
                }
            }
        }
    }
}

// =================================
// JVM based targets
kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
    }

    sourceSets {
        jvmMain {
            dependsOn(commonMain.get())
            dependencies {
                implementation("com.ibm.icu:icu4j:73.2")
            }
        }

        jvmTest {
            dependsOn(commonTest.get())
        }
    }
    jvmToolchain(8)
}


// =================================
// JS Target
kotlin {
    js(IR) {
        browser()
        nodejs()
        binaries.library()
    }

    sourceSets {
        jsMain {
            dependsOn(commonMain.get())
            dependencies {
                implementation(npm("i18next", "23.7.11"))
            }
        }
    }
}

// =================================
// Android Native Targets
kotlin {
//    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()

    sourceSets {
        // AndroidNative implementation not yet implemented
    }
}

// =================================
// Apple Targets (macOS required)
kotlin {
    val targets = arrayOf(
        macosX64(),
        macosArm64(),

        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),

        watchosArm32(),
        watchosArm64(),
        watchosX64(),
        watchosSimulatorArm64(),

        tvosX64(),
        tvosArm64(),
        tvosSimulatorArm64()
    )
    sourceSets {
        appleMain {
            dependsOn(commonMain.get())
            targets.forEach {
                getByName("${it.name}Main").dependsOn(this)
            }
        }

        appleTest {
            targets.forEach {
                getByName("${it.name}Test").dependsOn(this)
            }
        }
    }
}

// =================================
// Linux targets
kotlin {
    linuxX64()
    linuxArm64()

    sourceSets {
        // Linux implementation not yet implemented
    }
}

// =================================
// Windows targets
kotlin {
    mingwX64()

    sourceSets {
        // Windows implementation not yet implemented
    }
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
            this@kotlin.androidTarget {
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
