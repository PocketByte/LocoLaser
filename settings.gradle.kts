pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}

rootProject.name = "locolaser"

include(":core")
include(":resource-googlesheet")
include(":resource-mobile")
include(":resource-gettext")
include(":resource-json")
include(":resource-kotlin-mpp")
include(":resource-ini")
include(":resource-properties")
include(":runtime")

include(":plugin")
include(":plugin:plugin-kmp")
include(":plugin:plugin-all")

includeBuild("playground")
