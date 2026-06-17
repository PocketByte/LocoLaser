# LocoLaser Gradle Plugin
![Maven Central](https://img.shields.io/maven-central/v/ru.pocketbyte.locolaser/plugin-all) [![License](https://img.shields.io/badge/License-Apache/2.0-blue.svg)](../LICENSE)

Gradle plugin that adds the `localize {}` DSL extension and localization tasks to your build.

---

## Applying

### Option A: bundle artifact (recommended)

Includes the plugin and resource modules in a single dependency — no separate `classpath` entries needed.

**build.gradle.kts:**
```kotlin
buildscript {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("ru.pocketbyte.locolaser:plugin-all:2.6.0")
        // or: classpath("ru.pocketbyte.locolaser:plugin-kmp:2.6.0")
    }
}

apply(plugin = "ru.pocketbyte.locolaser.all")
// or: apply(plugin = "ru.pocketbyte.locolaser.kmp")
```

Bundle contents:
- `plugin-all` — all resources: mobile, kotlin-mpp, json, gettext, ini, properties, googlesheet
- `plugin-kmp` — mobile, kotlin-mpp, json, properties (no gettext, ini, googlesheet)

### Option B: base plugin + explicit resource modules

**build.gradle.kts:**
```kotlin
plugins {
    id("ru.pocketbyte.locolaser").version("2.6.0")
}

buildscript {
    repositories { mavenCentral() }
    dependencies {
        classpath("ru.pocketbyte.locolaser:resource-mobile:2.6.0")
        classpath("ru.pocketbyte.locolaser:resource-googlesheet:2.6.0")
    }
}
```

---

## DSL

```kotlin
import ru.pocketbyte.locolaser.*

localize {
    config {                                    // unnamed → tasks: localize, localizeForce, localizeExportNew
        locales = setOf("base", "en", "de")
        source { /* ... */ }
        platform { /* ... */ }
    }

    config("KMP") {                            // named → tasks: localizeKMP, localizeKMPForce, localizeKMPExportNew
        locales = setOf("base", "en", "de")
        dependsOnCompileTasks()                // call when KMP codegen must precede compilation
        source { /* ... */ }
        platform { /* ... */ }
    }
}
```

Each resource module added as a `classpath` dependency registers its own builder extensions for `source {}` and `platform {}` blocks.

---

## Tasks

The plugin adds tasks to the **`localization`** group. For an unnamed config:

| Task | Behavior |
|---|---|
| `localize` | Sync strings (Gradle task cache applies) |
| `localizeForce` | Sync strings, bypass cache |
| `localizeExportNew` | Sync + export new platform strings to source |

For a config named `KMP`, tasks are `localizeKMP`, `localizeKMPForce`, `localizeKMPExportNew`.
