# Resource: INI Files

The `resource-ini` module reads and writes INI-format localization files.

Included in `plugin-all`. Requires `ru.pocketbyte.locolaser.all` plugin.

→ [Back to root README](../README.md)

---

## DSL

Resource blocks are configured through the [LocoLaser Gradle plugin](../plugin/README.md) and go inside `source { }` or `platform { }` within `localize { config { } }`.

**build.gradle.kts:**
```kotlin
ini {
    resourcesDir = "./"      // default
    resourceName = "data"    // → data.ini
    filter("^screen_.*")     // optional; RegExp on keys
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

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"data"` | File name without `.ini` extension. |
| `resourcesDir` | `"./"` | Directory for the INI file. |
| `formattingType` | `JavaFormattingType` | Formatting type applied to string values. |

---

## File layout

INI stores **all locales in a single file**. Locales are separated by `[locale]` section headers:

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
import ru.pocketbyte.locolaser.*

localize {
    config {
        locales = setOf("base", "en", "de")
        source {
            googleSheet {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                credentialFile = "./service_account.json"
            }
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
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.ini.IniResourcesConfig
localize {
    config {
        locales = ["base", "en", "de"]
        source {
            add(GoogleSheetResourcesConfig.@Companion) {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                credentialFile = "./service_account.json"
            }
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
