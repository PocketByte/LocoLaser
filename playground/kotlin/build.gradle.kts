import ru.pocketbyte.locolaser.*
import ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsStaticResourcesConfig
import ru.pocketbyte.locolaser.plugin.localize
import ru.pocketbyte.locolaser.plugin.resourceConfigAction
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        val localaserVersion = "2.3.0"
        classpath("ru.pocketbyte.locolaser:plugin:$localaserVersion")
        classpath("ru.pocketbyte.locolaser:core:$localaserVersion")
        classpath("ru.pocketbyte.locolaser:resource-gettext:$localaserVersion")
        classpath("ru.pocketbyte.locolaser:resource-googlesheet:$localaserVersion")
        classpath("ru.pocketbyte.locolaser:resource-ini:$localaserVersion")
        classpath("ru.pocketbyte.locolaser:resource-json:$localaserVersion")
        classpath("ru.pocketbyte.locolaser:resource-kotlin-mpp:$localaserVersion")
        classpath("ru.pocketbyte.locolaser:resource-mobile:$localaserVersion")
        classpath("ru.pocketbyte.locolaser:resource-properties:$localaserVersion")
    }
}

plugins.apply("ru.pocketbyte.locolaser")

version = "unspecified"

localize {
    configFromFile("JsonConfig", "../config.json")

    val googleSheetConfiguration = resourceConfigAction {
        googlesheet {
            id = "1JZxUcu30BjxLwHg12bdHTxjDgsGFX9HA9zC4Jd8cuUM"
            keyColumn = "key"
            commentColumn = "base"
            quantityColumn = "quantity"
            credentialFile("../service_account.json")
        }
    }
    config {
        platform {
            this.
//            kotlinMultiplatform {
//                srcDir = File("./build/res/kmpp/")
//                repositoryInterface = "com.example.SuperString"
//                repositoryClass = "com.example.SuperStringImpl"
//                android(); ios(); js()
//                platform("common", "test", KotlinAbsStaticResourcesConfig().apply {
//                    formattingType = JavaFormattingType
//                })
//            }
            json {
                resourcesDir("./build/res/json")
                indent = 2
            }
            gettext {
                resourcesDir("./build/res/gettext")
            }
            ini {
                resourcesDir("./build/res/ini")
            }
            properties {
                resourcesDir("./build/res/properties")
            }
            android {
                resourcesDir("./build/res/android")
            }
            ios {
                resourcesDir("./build/res/ios")
            }
        }
        source.googleSheetConfiguration()
        isDuplicateComments = false
        locales = setOf("base", "ru")
    }
}