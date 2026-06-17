# Resource: Java Properties

The `resource-properties` module reads and writes Java `.properties` files.

Included in `plugin-all` and `plugin-kmp`. Requires `ru.pocketbyte.locolaser.kmp` or `ru.pocketbyte.locolaser.all` plugin.

→ [Back to root README](../README.md)

---

## DSL

Resource blocks are configured through the [LocoLaser Gradle plugin](../plugin/README.md) and go inside `source { }` or `platform { }` within `localize { config { } }`.

**build.gradle.kts:**
```kotlin
properties {
    resourcesDir = "./locales/"   // default
    resourceName = "strings"      // → locales/strings_en.properties
    filter("^app_.*")             // optional; RegExp on keys
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.properties.PropertiesResourcesConfig
add(PropertiesResourcesConfig.@Companion) {
    resourcesDir = "./locales/"
    resourceName = "strings"
    filter("^app_.*")
}
```

---

## Parameters

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"strings"` | File base name without extension. |
| `resourcesDir` | `"./locales/"` | Directory for properties files. |
| `formattingType` | `JavaFormattingType` | Formatting type applied to string values. |

---

## File layout

```
resourcesDir/
  strings.properties       ← base locale
  strings_en.properties    ← "en" locale
  strings_de.properties    ← "de" locale
```

Pattern: `<resourceName>.properties` for base, `<resourceName>_<locale>.properties` for others.

---

## Plural support

Only the `other` quantity is supported. Use `trimUnsupportedQuantities = true` in the global config to discard other plural forms automatically.

---

## Full example

**build.gradle.kts:**
```kotlin
import ru.pocketbyte.locolaser.*

localize {
    config("Backend") {
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
            properties {
                resourcesDir = "./src/main/resources/i18n/"
                resourceName = "messages"
            }
        }
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.properties.PropertiesResourcesConfig
localize {
    config("Backend") {
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
            add(PropertiesResourcesConfig.@Companion) {
                resourcesDir = "./src/main/resources/i18n/"
                resourceName = "messages"
            }
        }
    }
}
```
