# LocoLaser
![Maven Central](https://img.shields.io/maven-central/v/ru.pocketbyte.locolaser/plugin-all) [![License](https://img.shields.io/badge/License-Apache/2.0-blue.svg)](LICENSE) [![Claude Code Skill](https://img.shields.io/badge/Claude_Code-Skill-D97757)](https://github.com/PocketByte/LocoLaser/blob/master/.claude/skills/locolaser/SKILL.md)

LocoLaser is a Gradle plugin that syncs localization strings between a **Source** resource and a **Platform** resource:

```
Resource (as Source)  ──→  LocoLaser  ──→  Resource (as Platform)
```

Source and Platform are **roles**, not fixed types. Any resource can act as either. Google Sheets is typically used as Source; file-based resources (Android, iOS, JSON, etc.) can be both.

---

## Supported resource types

| Resource | Module | Typical role | README |
|---|---|---|---|
| Google Sheets | `resource-googlesheet` | Source | [→](resource-googlesheet/README.md) |
| Android `strings.xml` | `resource-mobile` | Platform / Source | [→](resource-mobile/README.md) |
| iOS `Localizable.strings` | `resource-mobile` | Platform / Source | [→](resource-mobile/README.md) |
| iOS Swift / ObjC codegen | `resource-mobile` | Platform | [→](resource-mobile/README.md) |
| Kotlin Multiplatform codegen | `resource-kotlin-mpp` | Platform | [→](resource-kotlin-mpp/README.md) |
| JSON (i18next) | `resource-json` | Platform / Source | [→](resource-json/README.md) |
| GetText `.po` | `resource-gettext` | Platform / Source | [→](resource-gettext/README.md) |
| INI files | `resource-ini` | Platform / Source | [→](resource-ini/README.md) |
| Java Properties | `resource-properties` | Platform / Source | [→](resource-properties/README.md) |

---

## Related examples

- [Android Example](https://github.com/PocketByte/locolaser-android-example)
- [iOS Example](https://github.com/PocketByte/locolaser-ios-example)
- [Kotlin MPP Example](https://github.com/PocketByte/locolaser-kotlin-mpp-example)

---

## Applying the plugin

> See also: [plugin/README.md](plugin/README.md)

### Option A: bundle artifact (recommended)

`plugin-all` includes the plugin and all resource modules in one dependency.

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

**build.gradle:**
```groovy
buildscript {
    repositories {
        mavenCentral()
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath "ru.pocketbyte.locolaser:plugin-all:2.6.0"
    }
}

apply plugin: "ru.pocketbyte.locolaser.all"
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

## Configuration DSL

Add this import when using `build.gradle.kts`:
```kotlin
import ru.pocketbyte.locolaser.*
```

```kotlin
localize {
    config {                                    // unnamed → tasks: localize, localizeForce, localizeExportNew
        locales = setOf("base", "en", "de")    // required; "base" = default/fallback locale
        source { /* ... */ }                   // required
        platform { /* ... */ }                 // required
        conflictStrategy = Config.ConflictStrategy.KEEP_NEW_PLATFORM
        trimUnsupportedQuantities = true
    }

    config("KMP") {                            // named → tasks: localizeKMP, localizeKMPForce, localizeKMPExportNew
        locales = setOf("base", "en", "de")
        dependsOnCompileTasks()                // call when KMP codegen must precede compilation
        source { /* ... */ }
        platform { /* ... */ }
    }
}
```

### Conflict strategies

| Strategy | Behavior |
|---|---|
| `REMOVE_PLATFORM` | Source wins; platform strings absent from source are deleted. |
| `KEEP_NEW_PLATFORM` | Source wins, but platform strings not in source are kept. |
| `KEEP_PLATFORM` | Platform always wins — source is never overwritten. |
| `EXPORT_NEW_PLATFORM` | New platform strings (not in source) are pushed back to source. |
| `EXPORT_PLATFORM` | All platform strings are pushed to source. |

---

## Gradle tasks

```
./gradlew localize              # sync (Gradle task cache applies)
./gradlew localizeForce         # sync, bypass cache
./gradlew localizeExportNew     # sync + export new platform strings to source
```

For named configs, tasks are prefixed with the config name: `localizeKMP`, `localizeKMPForce`, etc.

---

## Android example

Sync strings from Google Sheets to `strings.xml`:

**build.gradle.kts:**
```kotlin
import ru.pocketbyte.locolaser.*

localize {
    config {
        locales = setOf("base", "en", "de")
        source {
            googleSheet {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                quantityColumn = "quantity"
                credentialFile = "./service_account.json"
            }
        }
        platform {
            android {
                resourcesDir = "./app/src/main/res/"
            }
        }
    }
}
```

---

## Kotlin Multiplatform example

Two-config setup — first populate platform string files, then generate the KMP repository:

**build.gradle.kts:**
```kotlin
import ru.pocketbyte.locolaser.*

localize {
    // Step 1: sync strings from Google Sheets to Android/iOS resource files
    config("Mobile") {
        locales = setOf("base", "en", "de")
        source {
            googleSheet {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                credentialFile = "./service_account.json"
            }
        }
        platform {
            android { resourcesDir = "./android/src/main/res/" }
            ios { resourcesDir = "./ios/" }
        }
    }

    // Step 2: generate KMP repository code from the Android resource files
    config("KMP") {
        locales = setOf("base", "en", "de")
        dependsOnCompileTasks()
        source {
            android { resourcesDir = "./android/src/main/res/" }
        }
        platform {
            kotlinMultiplatform(project) {
                srcDir = "./shared/build/generated/locolaser/"
                repositoryInterface = "com.example.StringRepository"
                repositoryClass = "com.example.StringRepositoryImpl"
                android()
                ios()
            }
        }
    }
}
```

The generated KMP code requires a runtime dependency in your shared module:

**build.gradle.kts (shared module):**
```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("ru.pocketbyte.locolaser:runtime:2.6.0")
        }
    }
}
```

---

## Google Sheets setup

1. Create a service account in Google Cloud Console and download the JSON key file.
2. Share the spreadsheet with the service account email (Viewer access is sufficient).
3. Pass the path to `credentialFile` in your config.

Without `credentialFile`, LocoLaser falls back to interactive OAuth — only works on developer machines with a browser.

### Sheet layout

| key | base | en | de | quantity |
|---|---|---|---|---|
| app_title | App | App | App | |
| file_count | %1$d files | %1$d files | %1$d Dateien | other |
| file_count | %1$d file | %1$d file | %1$d Datei | one |

- `base` column → default/fallback locale
- Add a `quantity` column and repeat rows to support plural forms

---

## Notes

- **Configuration Cache**: LocoLaser tasks automatically disable Gradle's Configuration Cache — this is expected.
- **Relative paths**: All paths resolve from `workDir`, which defaults to the module directory.
- **Task caching**: `localize` is cached by Gradle. Use `localizeForce` for a guaranteed fresh run.

---

## Migration

See [Migration guide](docs/migration.md) for upgrading from earlier versions.

---

## License

```
Copyright © 2017 Denis Shurygin. All rights reserved.
Contacts: <mail@pocketbyte.ru>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```