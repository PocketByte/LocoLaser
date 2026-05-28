# Resource: Mobile — Android & iOS String Resources

The `resource-mobile` module handles Android `strings.xml` and iOS `Localizable.strings` / `.stringsdict` files.

For iOS Swift / Objective-C code generation see `references/resource-mobile-codegen.md`.

Requires gradle plugin `ru.pocketbyte.locolaser.kmp` or `ru.pocketbyte.locolaser.all`.

---

## Android

> Full set of parameters and functions available inside `android { }` is defined in `ru.pocketbyte.locolaser.mobile.AndroidResourcesConfigBuilder`.

### DSL

> Place the block inside `platform { }` (primary use case) or `source { }`. Configuration is the same for both roles. In practice, `platform { }` and `source { }` are nested inside `localize { config { ... } }`.

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
| `resourceFileProvider` | `AndroidResourceFileProvider` | Overrides how the resource file path is resolved. In most cases there is no need to change this — leave it at the default. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

### File naming

```
resourcesDir/
  values/strings.xml        ← base locale
  values-en/strings.xml     ← "en" locale
  values-de/strings.xml     ← "de" locale
```

Locale code is appended as-is after `values-`. Use the locale string exactly as you define it in the `locales` config property.

### Plural support

All 6 Android quantities are supported: `zero`, `one`, `two`, `few`, `many`, `other`.

Generated XML:
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

> Full set of parameters and functions available inside `ios { }` is defined in `ru.pocketbyte.locolaser.mobile.IosResourcesConfigBuilder`.

### DSL

> Place the block inside `platform { }` (primary use case) or `source { }`. Configuration is the same for both roles.

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
| `resourceFileProvider` | `IosResourceFileProvider` | Overrides how the resource file path is resolved. In most cases there is no need to change this — leave it at the default. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

### File naming

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

> Full set of parameters and functions available inside `iosPlist { }` is defined in `ru.pocketbyte.locolaser.mobile.IosPlistResourcesConfigBuilder`.

Used for localizing `Info.plist` entries. Configuration is the same as `ios { }`.

> Place the block inside `platform { }` (primary use case) or `source { }`. Configuration is the same for both roles.

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

## Full example

**build.gradle.kts:**
```kotlin
localize {
    config("Mobile") {
        locales = setOf("base", "en", "de")
        source {
            // configure your source resource here
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
// import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
// import ru.pocketbyte.locolaser.mobile.IosResourcesConfig
localize {
    config("Mobile") {
        locales = ["base", "en", "de"]
        source {
            // configure your source resource here
        }
        platform {
            add(AndroidResourcesConfig.@Companion) { resourcesDir = "./android/src/main/res/" }
            add(IosResourcesConfig.@Companion) { resourcesDir = "./ios/" }
        }
    }
}
```

Multiple resource blocks can be listed inside a single `platform { }` call.
