import ru.pocketbyte.locolaser.*
import ru.pocketbyte.locolaser.plugin.localize
import ru.pocketbyte.locolaser.plugin.resourceConfigAction

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("ru.pocketbyte.locolaser:plugin:2.2.0")
        classpath("ru.pocketbyte.locolaser:core:2.2.0")
        classpath("ru.pocketbyte.locolaser:resource-gettext:2.2.0")
        classpath("ru.pocketbyte.locolaser:resource-googlesheet:2.2.0")
        classpath("ru.pocketbyte.locolaser:resource-ini:2.2.0")
        classpath("ru.pocketbyte.locolaser:resource-json:2.2.0")
        classpath("ru.pocketbyte.locolaser:resource-kotlin-mpp:2.2.0")
        classpath("ru.pocketbyte.locolaser:resource-mobile:2.2.0")
        classpath("ru.pocketbyte.locolaser:resource-properties:2.2.0")
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
            kotlinMultiplatform {
                srcDir = File("./build/res/kmpp/")
                repositoryInterface = "com.example.SuperString"
                repositoryClass = "com.example.SuperStringImpl"
                android(); ios(); js()
            }
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