---
name: locolaser
description: Use when working with LocoLaser — a Gradle-based localization tool that syncs strings between sources and platforms. Triggers on: LocoLaser setup, localize task, google sheets localization, Android strings.xml sync, iOS Localizable.strings, Kotlin Multiplatform string repository, resource-googlesheet, resource-mobile, resource-kotlin-mpp, resource-json, resource-gettext, resource-ini, resource-properties, localize {} DSL, import or export localization strings, plural strings sync.
---

# LocoLaser

> Current version: **2.6.0**. Use this version in all `classpath` and `id(...)` examples.
> Full release list: [GitHub Releases](https://github.com/PocketByte/LocoLaser/releases) · [Maven Central](https://central.sonatype.com/search?q=ru.pocketbyte.locolaser)

LocoLaser syncs localization strings between a **Source** resource and a **Platform** resource:

```
Resource (as Source)  ──→  core  ──→  Resource (as Platform)
```

Source and Platform are **roles**, not fixed module types. Any resource can act as Source (strings are read from it) or as Platform (strings are written to it). Google Sheets is typically Source-only in practice; file-based resources (Android, iOS, JSON, etc.) can be either.

---

## Resource types

| Resource | Module | Typical role |
|---|---|---|
| Google Sheets | `resource-googlesheet` | Source |
| Android `strings.xml` | `resource-mobile` | Platform / Source |
| iOS `Localizable.strings` | `resource-mobile` | Platform / Source |
| iOS Swift / ObjC codegen | `resource-mobile` | Platform (see `resource-mobile-codegen.md`) |
| Kotlin Multiplatform codegen | `resource-kotlin-mpp` | Platform |
| JSON (i18next) | `resource-json` | Platform / Source |
| GetText `.po` | `resource-gettext` | Platform / Source |
| INI files | `resource-ini` | Platform / Source |
| Java Properties | `resource-properties` | Platform / Source |

---

## Reference files — read before generating config

Always read the reference file for each resource involved before generating any configuration.

| Topic                                                | File |
|------------------------------------------------------|---|
| Google Sheets                                        | `references/resource-googlesheet.md` |
| Android / iOS string resources                       | `references/resource-mobile.md` |
| iOS Swift / Objective-C code generation              | `references/resource-mobile-codegen.md` |
| Kotlin Multiplatform — plugin configuration          | `references/resource-kotlin-mpp.md` |
| Kotlin Multiplatform — generated API / using classes | `references/resource-kotlin-mpp-api.md` |
| JSON                                                 | `references/resource-json.md` |
| GetText                                              | `references/resource-gettext.md` |
| INI                                                  | `references/resource-ini.md` |
| Java Properties                                      | `references/resource-properties.md` |
| Formatting / placeholders                            | `references/formatting-types.md` |

---

## Applying the plugin

### Option A: bundle artifact (simplest)

Use `plugin-all` or `plugin-kmp` — these include the plugin plus pre-bundled resource modules.

**build.gradle.kts:**
```kotlin
buildscript {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("ru.pocketbyte.locolaser:plugin-all:2.6.0")   // recommended: all resource modules
        // or: classpath("ru.pocketbyte.locolaser:plugin-kmp:2.6.0")  // KMP + JSON + Mobile + Properties
    }
}

apply(plugin = "ru.pocketbyte.locolaser.all")   // matches plugin-all
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
        // or: classpath "ru.pocketbyte.locolaser:plugin-kmp:2.6.0"
    }
}

apply plugin: "ru.pocketbyte.locolaser.all"
// or: apply plugin: "ru.pocketbyte.locolaser.kmp"
```

### Option B: plugin + separate resource modules

Use the base plugin and declare only the resource modules you need:

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
        // add only what you need
    }
}
```

**build.gradle:**
```groovy
plugins {
    id "ru.pocketbyte.locolaser" version "2.6.0"
}

buildscript {
    repositories { mavenCentral() }
    dependencies {
        classpath "ru.pocketbyte.locolaser:resource-mobile:2.6.0"
        classpath "ru.pocketbyte.locolaser:resource-googlesheet:2.6.0"
        // add only what you need
    }
}
```

**Bundle contents:**
- `plugin-all` — all resources: mobile, kotlin-mpp, json, gettext, ini, properties, googlesheet
- `plugin-kmp` — mobile, kotlin-mpp, json, properties (no gettext, ini, googlesheet)
- `plugin` (base) — no resources; add resource classpath dependencies manually

---

## Config DSL structure

**build.gradle.kts** — resource extension functions (`android`, `ios`, `googleSheet`, `json`, etc.) are in the `ru.pocketbyte.locolaser` package. Add this import:

```kotlin
import ru.pocketbyte.locolaser.*
```

```kotlin
localize {
    config("OptionalName") {
        locales = setOf("base", "en", "de", "fr")   // required
        source { /* ... */ }                          // required
        platform { /* ... */ }                        // required
        conflictStrategy = Config.ConflictStrategy.KEEP_NEW_PLATFORM
        duplicateComments = false
        trimUnsupportedQuantities = true
        workDir = file("./")
        dependsOnCompileTasks()                       // optional; call when KMP codegen must precede compilation
    }
}
```

**build.gradle** — extension functions are not available in Groovy. Use `add(ResourceConfig.@Companion) { }` inside `source { }` and `platform { }`. Each resource type requires its own explicit import:

```groovy
// import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig  (example — import only what you use)
localize {
    config("OptionalName") {
        locales = ["base", "en", "de", "fr"]   // required
        source { /* ... */ }                    // required
        platform { /* ... */ }                  // required
        conflictStrategy = Config.ConflictStrategy.KEEP_NEW_PLATFORM
        duplicateComments = false
        trimUnsupportedQuantities = true
        workDir = file("./")
        dependsOnCompileTasks()
    }
}
```

Multiple `config(...)` blocks are supported — each generates its own task set.
Unnamed config (`config { }`) produces tasks named `localize`, `localizeForce`, `localizeExportNew`.
Named config (`config("KMP") { }`) produces `localizeKMP`, `localizeKMPForce`, `localizeKMPExportNew`.

---

## Global config parameters

> Full set of parameters and functions available inside `config { }` is defined in `ru.pocketbyte.locolaser.config.ConfigBuilder`.

| Parameter | Type | Default | Description |
|---|---|---|---|
| `locales` | `Set<String>` | `{"base"}` | Locales to process. `"base"` means the default/fallback locale. |
| `conflictStrategy` | `ConflictStrategy` | `KEEP_NEW_PLATFORM` | How to resolve disagreements between source and platform. |
| `duplicateComments` | `Boolean` | `false` | Write comment even when it equals the string value. |
| `trimUnsupportedQuantities` | `Boolean` | `true` | Drop plural forms that the locale doesn't support. |
| `workDir` | `File?` | module dir | Base directory for resolving relative paths in the config. |

**Function:**

| Function | Description |
|---|---|
| `dependsOnCompileTasks()` | Makes the localization task depend on compile tasks. Call this when generated Kotlin code must be available before compilation. |

**Locales note:** Always include `"base"` unless you have a specific reason not to. It represents the default resource file (no locale suffix).

---

## Conflict strategies

| Strategy | Behavior |
|---|---|
| `REMOVE_PLATFORM` | Source wins. Platform strings absent from source are deleted. |
| `KEEP_NEW_PLATFORM` | Source wins, but platform strings not present in source are kept. |
| `KEEP_PLATFORM` | Platform always wins — source is read but platform is never overwritten. |
| `EXPORT_NEW_PLATFORM` | New platform strings (not in source) are pushed back to the source. |
| `EXPORT_PLATFORM` | All platform strings are pushed to the source. |

---

## Gradle tasks

For config named `"Name"`:
```
./gradlew localizeName           # sync (Gradle task cache applies)
./gradlew localizeNameForce      # sync, bypass cache
./gradlew localizeNameExportNew  # sync + export new platform strings to source
```

For unnamed config:
```
./gradlew localize
./gradlew localizeForce
./gradlew localizeExportNew
```

---

## Multiple configs example

**build.gradle.kts:**
```kotlin
localize {
    config("Android") {
        locales = setOf("base", "en", "de")
        source { googleSheet { id = "abc123" } }
        platform { android { resourcesDir = "./app/src/main/res/" } }
    }
    config("KMP") {
        locales = setOf("base", "en", "de")
        source { googleSheet { id = "abc123" } }
        platform {
            kotlinMultiplatform(project) {
                srcDir = "./shared/build/generated/locolaser/"
                repositoryInterface = "com.example.StringRepository"
                repositoryClass = "com.example.StringRepositoryImpl"
                android(); ios()
            }
        }
    }
}
// Tasks: localizeAndroid, localizeKMP, etc.
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder
// import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
localize {
    config("Android") {
        locales = ["base", "en", "de"]
        source { add(GoogleSheetResourcesConfig.@Companion) { id = "abc123" } }
        platform { add(AndroidResourcesConfig.@Companion) { resourcesDir = "./app/src/main/res/" } }
    }
    config("KMP") {
        locales = ["base", "en", "de"]
        source { add(GoogleSheetResourcesConfig.@Companion) { id = "abc123" } }
        platform {
            add(new KotlinMultiplatformResourcesConfigBuilder(project)) {
                srcDir = "./shared/build/generated/locolaser/"
                repositoryInterface = "com.example.StringRepository"
                repositoryClass = "com.example.StringRepositoryImpl"
                android(); ios()
            }
        }
    }
}
// Tasks: localizeAndroid, localizeKMP, etc.
```

---

## Gotchas

- **Configuration Cache**: LocoLaser tasks automatically disable Gradle's Configuration Cache — this is expected.
- **Relative paths**: All paths resolve from `workDir`, which defaults to the module directory.
- **Task caching**: `localize` is cached by Gradle. Use `localizeForce` for a guaranteed fresh run.
