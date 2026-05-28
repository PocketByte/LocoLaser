# Resource: INI Files

The `resource-ini` module reads and writes INI-format localization files.

Requires gradle plugin `ru.pocketbyte.locolaser.all`.

## DSL

> Place the block inside `platform { }` (primary use case) or `source { }`. Configuration is the same for both roles. In practice, `platform { }` and `source { }` are nested inside `localize { config { ... } }`.

**build.gradle.kts:**
```kotlin
ini {
    resourcesDir = "./"       // default
    resourceName = "data"     // → data.ini
    filter("^screen_.*")      // optional; RegExp on keys
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.ini.IniResourcesConfig
add(IniResourcesConfig.@Companion) {
    resourcesDir = "./"
    resourceName = "data"
    filter("^screen_.*")
}
```

---

## Parameters

> Full set of parameters and functions available inside `ini { }` is defined in `ru.pocketbyte.locolaser.ini.IniResourcesConfigBuilder`.

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"data"` | File name without `.ini` extension. |
| `resourcesDir` | `"./"` | Directory for the INI file. |
| `formattingType` | `JavaFormattingType` | Formatting type applied to string values. See `references/formatting-types.md`. |
| `resourceFileProvider` | `IniResourceFileProvider` | Overrides how the resource file path is resolved. In most cases there is no need to change this — leave it at the default. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

---

## File naming

INI stores **all locales in a single file**. The filename does not include the locale:

```
resourcesDir/
  data.ini     ← contains all locales
```

Inside the file, locales are separated by `[locale]` section headers:

```ini
# AUTO-GENERATED FILE. DO NOT MODIFY.
[base]
app_title = My App
welcome = Hello!

[en]
app_title = My App
welcome = Hello!

[de]
app_title = Meine App
welcome = Hallo!
```

---

## Plural support

All 6 plural quantities are supported: `zero`, `one`, `two`, `few`, `many`, `other`.

---

## Full example

**build.gradle.kts:**
```kotlin
localize {
    config("Config") {
        locales = setOf("base", "en", "de")
        source {
            // configure your source resource here
        }
        platform {
            ini {
                resourcesDir = "./resources/lang/"
                resourceName = "strings"
            }
        }
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.ini.IniResourcesConfig
localize {
    config("Config") {
        locales = ["base", "en", "de"]
        source {
            // configure your source resource here
        }
        platform {
            add(IniResourcesConfig.@Companion) {
                resourcesDir = "./resources/lang/"
                resourceName = "strings"
            }
        }
    }
}
```
