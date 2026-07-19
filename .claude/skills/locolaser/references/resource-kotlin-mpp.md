# Resource: Kotlin Multiplatform

The `resource-kotlin-mpp` module generates Kotlin code — a shared string repository interface with platform-specific implementations for Android, iOS, and JS. It is always used as **platform** (code is written); it cannot act as source.

Requires gradle plugin `ru.pocketbyte.locolaser.kmp` or `ru.pocketbyte.locolaser.all`.

---

## Runtime dependency

The generated KMP code depends on classes from the LocoLaser runtime library. Add it to `commonMain` in your KMP module — **not** in the module that applies the LocoLaser plugin:

**build.gradle.kts:**
```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("ru.pocketbyte.locolaser:runtime:2.7.0")
        }
    }
}
```

**build.gradle:**
```groovy
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation "ru.pocketbyte.locolaser:runtime:2.7.0"
            }
        }
    }
}
```

---

## Source for KMP

The generated platform implementations delegate string lookups to the native platform string resources at runtime (Android `strings.xml`, iOS `Localizable.strings`, etc.). Therefore, **those platform string files must be populated before the KMP code runs** — typically by a separate LocoLaser config.

The recommended two-config setup:

**build.gradle.kts:**
```kotlin
localize {
    // Step 1: sync strings from source to platform string files
    config("Mobile") {
        locales = setOf("base", "en", "de")
        source { googleSheet { id = "YOUR_SHEET_ID" } }
        platform {
            android { resourcesDir = "./android/src/main/res/" }
            ios { resourcesDir = "./ios/" }
        }
    }

    // Step 2: generate KMP repository code, reading keys from the platform string files
    config("KMP") {
        locales = setOf("base", "en", "de")
        dependsOnCompileTasks()
        source { android { resourcesDir = "./android/src/main/res/" } }
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

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder
// import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
// import ru.pocketbyte.locolaser.mobile.IosResourcesConfig
localize {
    // Step 1: sync strings from source to platform string files
    config("Mobile") {
        locales = ["base", "en", "de"]
        source {
            add(GoogleSheetResourcesConfig.@Companion) { id = "YOUR_SHEET_ID" }
        }
        platform {
            add(AndroidResourcesConfig.@Companion) { resourcesDir = "./android/src/main/res/" }
            add(IosResourcesConfig.@Companion) { resourcesDir = "./ios/" }
        }
    }

    // Step 2: generate KMP repository code, reading keys from the platform string files
    config("KMP") {
        locales = ["base", "en", "de"]
        dependsOnCompileTasks()
        source {
            add(AndroidResourcesConfig.@Companion) { resourcesDir = "./android/src/main/res/" }
        }
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
```

The `source` for the KMP config is the same Android/iOS resource that acts as `platform` in the first config.

## DSL

> The examples below show only the resource block. In practice, `platform { }` is nested inside `localize { config { ... } }`.

Configures the common interface and all platform implementations in one block. Generated source directories are automatically registered in Kotlin source sets when a `Project` is provided.

**build.gradle.kts:**
```kotlin
platform {
    kotlinMultiplatform(project) {
        srcDir = "./build/generated/locolaser/"
        repositoryInterface = "com.example.StringRepository"
        repositoryClass = "com.example.StringRepositoryImpl"
        android()
        ios()
        js()
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder
platform {
    add(new KotlinMultiplatformResourcesConfigBuilder(project)) {
        srcDir = "./build/generated/locolaser/"
        repositoryInterface = "com.example.StringRepository"
        repositoryClass = "com.example.StringRepositoryImpl"
        android(); ios(); js()
    }
}
```

---

## Single-platform / plain Android module

All examples above pass `kotlinMultiplatform(project)`, which assumes a **Kotlin Multiplatform** module: when a `Project` is provided, the generated directories are auto-registered into the `commonMain` / `<platform>Main` Kotlin source sets.

A plain Android module (`com.android.application` / `com.android.library` with `kotlin-android`, no KMP source sets) has **no** `commonMain` / `androidMain` source sets. Passing `project` there fails with `Missing sourceSet `androidMain`` (or a `commonMain` error), because the builder tries to register into source sets that don't exist on a non-multiplatform Kotlin extension.

Recipe for LocoLaser **2.7.0** on a plain Android module:

- Call `kotlinMultiplatform { … }` **without** `project` (disables auto source-set registration).
- Emit only the target(s) you need (`android()`).
- Register the generated directories into the `main` source set manually.

**build.gradle.kts:**
```kotlin
localize {
    config("App") {
        locales = setOf("base", "ru")
        dependsOnCompileTasks()
        source { android { resourcesDir = "./src/main/res/" } }
        platform {
            kotlinMultiplatform {                 // NOTE: no `project`
                srcDir = "./build/generated/locolaser/"
                repositoryInterface = "AppStringRepository"
                repositoryClass = "AppStringRepositoryImpl"
                repositoryPackage = "com.example.app.localization"
                android()
            }
        }
    }
}

android {
    sourceSets.getByName("main").java.srcDirs(
        "build/generated/locolaser/commonMain/kotlin",
        "build/generated/locolaser/androidMain/kotlin",
    )
}
```

The generated files still land under `commonMain/` and `androidMain/` subdirectories of `srcDir` (the builder names output dirs by target); only the source-set *registration* differs.

---

## Parameters

> Full set of parameters and functions available inside `kotlinMultiplatform { }` is defined in `ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder`.

| Parameter | Default | Description |
|---|---|---|
| `srcDir` | `"./build/generated/locolaser/"` | Root output directory. Subdirectories per source set are created automatically. |
| `repositoryInterface` | `null` | Canonical or simple name of the interface to generate. If `null`, no interface is generated. |
| `repositoryClass` | `null` | Canonical or simple name of the implementation class to generate for each platform. |
| `repositoryPackage` | `project.group` | Package used when `repositoryInterface` or `repositoryClass` is a simple (not canonical) name. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |
| `common(action?)` | Configures the common interface. Optional — generated by default. |
| `android(action?)` | Adds an Android implementation. |
| `ios(action?)` | Adds an iOS implementation. |
| `js(action?)` | Adds a JS implementation. |
| `absKeyValue(name, action?)` | Adds an abstract key-value implementation for the given target name. |
| `absStatic(name, action?)` | Adds a static (hardcoded) implementation — useful for tests. |
| `absProxy(name, action?)` | Adds a proxy/delegating implementation. |

`absKeyValue`, `absStatic`, and `absProxy` require a platform `name` string (e.g., `"common"`, `"android"`, `"test"`).

---

### `common` action block

> Builder: `ru.pocketbyte.locolaser.kotlinmpp.builder.KmpInterfaceBuilder`.

| Parameter | Default | Description |
|---|---|---|
| `interfaceName` | `repositoryInterface` | Simple or canonical name of the interface to generate. Overrides `repositoryInterface` for this block only. |
| `interfacePackage` | `repositoryPackage` | Package for the interface name when `interfaceName` is a simple name. Overrides `repositoryPackage` for this block only. |
| `sourcesDir` | `srcDir/commonMain/kotlin/` | Output directory for generated source files. Overrides `srcDir`-based default for this block only. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

---

### `android`, `ios`, `js`, `absProxy` action blocks

> Builder: `ru.pocketbyte.locolaser.kotlinmpp.builder.KmpClassFixedFormattingBuilder`.

| Parameter | Default | Description |
|---|---|---|
| `className` | `repositoryClass` | Simple or canonical name of the class to generate. Overrides `repositoryClass` for this block only. |
| `classPackage` | `repositoryPackage` | Package for the class name when `className` is a simple name. Overrides `repositoryPackage` for this block only. |
| `sourcesDir` | `srcDir/{name}Main/kotlin/` | Output directory for generated source files. Overrides `srcDir`-based default for this block only. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

---

### `absKeyValue`, `absStatic` action blocks

> Builder: `ru.pocketbyte.locolaser.kotlinmpp.builder.KmpClassCustomFormattingBuilder`.

Same parameters as `android`/`ios`/`js` above, plus:

| Parameter | Default | Description |
|---|---|---|
| `formattingType` | parent `kotlinMultiplatform` value | Formatting type for this target only. If `null`, inherits `formattingType` from the enclosing `kotlinMultiplatform` block. See `references/formatting-types.md`. |

> **Important:** Set `formattingType` to something other than `NoFormattingType` (e.g. `JavaFormattingType`) for `absKeyValue`. With `NoFormattingType`, the generated class ignores format arguments — functions with parameters will call `getString(key)` without substituting anything. `NoFormattingType` is acceptable for `absStatic` used as a test stub, where string content doesn't matter.

---

## Full example

**build.gradle.kts:**
```kotlin
// In the shared KMP module:
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("ru.pocketbyte.locolaser:runtime:2.7.0")
        }
    }
}

// In the module that applies the LocoLaser plugin:
localize {
    config("KMP") {
        locales = setOf("base", "en", "de")
        dependsOnCompileTasks()   // generated Kotlin code must be available before compilation
        source {
            // configure your source resource here
            android()
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

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder
// import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig

// In the shared KMP module:
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation "ru.pocketbyte.locolaser:runtime:2.7.0"
            }
        }
    }
}

// In the module that applies the LocoLaser plugin:
localize {
    config("KMP") {
        locales = ["base", "en", "de"]
        dependsOnCompileTasks()
        source {
            // configure your source resource here
            add(AndroidResourcesConfig.@Companion) {}
        }
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
```
