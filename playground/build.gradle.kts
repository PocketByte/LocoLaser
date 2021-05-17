import ru.pocketbyte.locolaser.gettext
import ru.pocketbyte.locolaser.googlesheet
import ru.pocketbyte.locolaser.ini
import ru.pocketbyte.locolaser.json
import ru.pocketbyte.locolaser.kotlinCommon
import ru.pocketbyte.locolaser.kotlinMultiplatform
import ru.pocketbyte.locolaser.android
import ru.pocketbyte.locolaser.ios
import ru.pocketbyte.locolaser.plugin.localize
import ru.pocketbyte.locolaser.plugin.resourceConfigAction
import ru.pocketbyte.locolaser.properties

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("ru.pocketbyte.locolaser:plugin:2.1.0")
        classpath("ru.pocketbyte.locolaser:core:2.1.0")
        classpath("ru.pocketbyte.locolaser:resource-gettext:2.1.0")
        classpath("ru.pocketbyte.locolaser:resource-googlesheet:2.1.0")
        classpath("ru.pocketbyte.locolaser:resource-ini:2.1.0")
        classpath("ru.pocketbyte.locolaser:resource-json:2.1.0")
        classpath("ru.pocketbyte.locolaser:resource-kotlin-mpp:2.1.0")
        classpath("ru.pocketbyte.locolaser:resource-mobile:2.1.0")
        classpath("ru.pocketbyte.locolaser:resource-properties:2.1.0")
    }
}

plugins.apply("ru.pocketbyte.locolaser")

version = "unspecified"

localize {
    val googleSheetConfiguration = resourceConfigAction {
        googlesheet {
            id = "1JZxUcu30BjxLwHg12bdHTxjDgsGFX9HA9zC4Jd8cuUM"
            keyColumn = "key"
            commentColumn = "base"
            quantityColumn = "quantity"
            credentialFile("service_account.json")
        }
    }
    config {
        platform {
//            kotlinMultiplatform {
//                srcDir = File("./res/kmpp/")
//                common()
//                android()
//                ios()
//                js()
//            }

            kotlinCommon {
                resourcesDir("./res/kmpp/common")
            }
            json {
                resourcesDir("./res/json")
            }
            gettext {
                resourcesDir("./res/gettext")
            }
            ini {
                resourcesDir("./res/ini")
            }
            properties {
                resourcesDir("./res/properties")
            }
            android {
                resourcesDir("./res/android")
            }
            ios {
                resourcesDir("./res/ios")
            }
        }
        source.googleSheetConfiguration()
        isDuplicateComments = false
        locales = setOf("base", "ru")
    }
}