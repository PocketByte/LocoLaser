# Resource: Mobile — iOS Code Generation

The `resource-mobile` module can generate Swift or Objective-C classes that provide typed access to localized strings via `NSLocalizedString`.

For Android and iOS string resource files (`.strings`, `.xml`) see `references/resource-mobile.md`.

---

## iOS Swift

> Full set of parameters and functions available inside `iosSwift { }` is defined in `ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfigBuilder`.

Generates a `.swift` file with a class containing a computed property per string key.

### DSL

> The examples below show only the resource block. In practice, `platform { }` is nested inside `localize { config { ... } }`.

**build.gradle.kts:**
```kotlin
platform {
    iosSwift {
        resourcesDir = "./Sources/Generated/"
        resourceName = "Str"           // → Str.swift
        tableName = "Localizable"      // iOS NSLocalizedString table name
        filter("^screen_.*")           // optional; RegExp on keys
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfig
platform {
    add(IosSwiftResourcesConfig.@Companion) {
        resourcesDir = "./Sources/Generated/"
        resourceName = "Str"
        tableName = "Localizable"
        filter("^screen_.*")
    }
}
```

Generated access:
```swift
Str.screen_title   // → NSLocalizedString("screen_title", tableName: "Localizable", ...)
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"Str"` | Generated class and file name (e.g. `"Str"` → `Str.swift`). |
| `resourcesDir` | `"./"` | Output directory for the generated file. |
| `tableName` | `"Localizable"` | The `.strings` table name passed to `NSLocalizedString`. |
| `resourceFileProvider` | `IosClassResourceFileProvider` | Overrides how the output file path is resolved. In most cases there is no need to change this — leave it at the default. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

---

## iOS Objective-C

> Full set of parameters and functions available inside `iosObjC { }` is defined in `ru.pocketbyte.locolaser.mobile.IosObjectiveCResourcesConfigBuilder`.

Generates a `.h` header and `.m` implementation file with `NSLocalizedString` macros.

### DSL

**build.gradle.kts:**
```kotlin
platform {
    iosObjC {
        resourcesDir = "./Classes/Generated/"
        resourceName = "Str"           // → Str.h + Str.m
        tableName = "Localizable"
        filter("^screen_.*")           // optional; RegExp on keys
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.mobile.IosObjectiveCResourcesConfig
platform {
    add(IosObjectiveCResourcesConfig.@Companion) {
        resourcesDir = "./Classes/Generated/"
        resourceName = "Str"
        tableName = "Localizable"
        filter("^screen_.*")
    }
}
```

### Parameters

Same as `iosSwift { }` above. The generated output is `.h` + `.m` instead of `.swift`.

---

## Full example

**build.gradle.kts:**
```kotlin
localize {
    config("iOSCodegen") {
        locales = setOf("base", "en", "de")
        source {
            // configure your source resource here
        }
        platform {
            iosSwift {
                resourcesDir = "./Sources/Generated/"
                resourceName = "Str"
            }
        }
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfig
localize {
    config("iOSCodegen") {
        locales = ["base", "en", "de"]
        source {
            // configure your source resource here
        }
        platform {
            add(IosSwiftResourcesConfig.@Companion) {
                resourcesDir = "./Sources/Generated/"
                resourceName = "Str"
            }
        }
    }
}
```
