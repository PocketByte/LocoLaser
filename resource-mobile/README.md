# Resource: Android & iOS

The `resource-mobile` module handles:
- Android `strings.xml` resource files
- iOS `Localizable.strings` / `.stringsdict` resource files
- iOS Swift / Objective-C code generation

Included in `plugin-all` and `plugin-kmp`. Requires `ru.pocketbyte.locolaser.kmp` or `ru.pocketbyte.locolaser.all` plugin.

→ [Back to root README](../README.md)

---

## Android

### DSL

Resource blocks are configured through the [LocoLaser Gradle plugin](../plugin/README.md) and go inside `source { }` or `platform { }` within `localize { config { } }`.

**build.gradle.kts:**
```kotlin
android {
    resourcesDir = "./src/main/res/"   // default
    resourceName = "strings"           // → values/strings.xml
    filter("^screen_.*")               // optional; RegExp on keys
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
add(AndroidResourcesConfig.@Companion) {
    resourcesDir = "./src/main/res/"
    resourceName = "strings"
    filter("^screen_.*")
}
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"strings"` | XML file name without extension. |
| `resourcesDir` | `"./src/main/res/"` | Directory containing `values/`, `values-en/`, etc. |

### File layout

```
resourcesDir/
  values/strings.xml        ← base locale
  values-en/strings.xml     ← "en" locale
  values-de/strings.xml     ← "de" locale
```

### Plural support

All 6 Android quantities are supported: `zero`, `one`, `two`, `few`, `many`, `other`.

```xml
<plurals name="file_count">
    <item quantity="one">%1$d file</item>
    <item quantity="other">%1$d files</item>
</plurals>
```

### Metadata

Android-specific string attributes come from the `metadataColumn` in Google Sheets:
- `formatted=false` → `<string name="key" formatted="false">...</string>`
- `xml-cdata=true` → wraps value in `<![CDATA[...]]>`
- `translatable=false` → `<string name="key" translatable="false">...</string>`

---

## iOS (Localizable.strings)

### DSL

Resource blocks are configured through the [LocoLaser Gradle plugin](../plugin/README.md) and go inside `source { }` or `platform { }` within `localize { config { } }`.

**build.gradle.kts:**
```kotlin
ios {
    resourcesDir = "./"
    resourceName = "Localizable"   // → Localizable.strings
    filter("^screen_.*")           // optional; RegExp on keys
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.mobile.IosResourcesConfig
add(IosResourcesConfig.@Companion) {
    resourcesDir = "./"
    resourceName = "Localizable"
    filter("^screen_.*")
}
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"Localizable"` | File base name without extension. |
| `resourcesDir` | `"./"` | Root directory. |

### File layout

```
resourcesDir/
  Base.lproj/Localizable.strings      ← base locale
  Base.lproj/Localizable.stringsdict  ← base locale plurals
  en.lproj/Localizable.strings        ← "en" locale
  en.lproj/Localizable.stringsdict    ← "en" locale plurals
```

The base locale maps to `Base.lproj/` (iOS convention). Non-base locales use `{locale}.lproj/`.

### Plural support

iOS plural support is limited. Use `trimUnsupportedQuantities = true` in the global config to discard unsupported forms automatically.

---

## iOS Plist

Used for localizing `Info.plist` entries. Configuration is identical to `ios { }`.

Resource blocks are configured through the [LocoLaser Gradle plugin](../plugin/README.md) and go inside `source { }` or `platform { }` within `localize { config { } }`.

**build.gradle.kts:**
```kotlin
iosPlist {
    resourcesDir = "./strings/"
    resourceName = "Localizable"
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.mobile.IosPlistResourcesConfig
add(IosPlistResourcesConfig.@Companion) {
    resourcesDir = "./strings/"
    resourceName = "Localizable"
}
```

---

## iOS Code Generation

Generates Swift or Objective-C classes providing typed access to localized strings via `NSLocalizedString`.

### iOS Swift

Configured through the [LocoLaser Gradle plugin](../plugin/README.md) and goes inside `localize { config { } }`.

**build.gradle.kts:**
```kotlin
platform {
    iosSwift {
        resourcesDir = "./Sources/Generated/"
        resourceName = "Str"        // → Str.swift
        tableName = "Localizable"   // NSLocalizedString table name
        filter("^screen_.*")        // optional; RegExp on keys
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
    }
}
```

Generated usage:
```swift
Str.screen_title   // → NSLocalizedString("screen_title", tableName: "Localizable", ...)
```

### iOS Objective-C

Configured through the [LocoLaser Gradle plugin](../plugin/README.md) and goes inside `localize { config { } }`.

**build.gradle.kts:**
```kotlin
platform {
    iosObjC {
        resourcesDir = "./Classes/Generated/"
        resourceName = "Str"        // → Str.h + Str.m
        tableName = "Localizable"
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
    }
}
```

### Code generation parameters

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"Str"` | Generated class / file name. |
| `resourcesDir` | `"./"` | Output directory for the generated file. |
| `tableName` | `"Localizable"` | The `.strings` table name passed to `NSLocalizedString`. |

**Note:** Add the generated file to your Xcode project manually.

---

## Full example

**build.gradle.kts:**
```kotlin
import ru.pocketbyte.locolaser.*

localize {
    config {
        locales = setOf("base", "en", "de")
        trimUnsupportedQuantities = true
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
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
// import ru.pocketbyte.locolaser.mobile.IosResourcesConfig
localize {
    config {
        locales = ["base", "en", "de"]
        trimUnsupportedQuantities = true
        source {
            add(GoogleSheetResourcesConfig.@Companion) {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                credentialFile = "./service_account.json"
            }
        }
        platform {
            add(AndroidResourcesConfig.@Companion) { resourcesDir = "./android/src/main/res/" }
            add(IosResourcesConfig.@Companion) { resourcesDir = "./ios/" }
        }
    }
}
```
