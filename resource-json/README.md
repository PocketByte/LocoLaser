# Resource: JSON (i18next)

The `resource-json` module reads and writes JSON files in i18next format.

Included in `plugin-all` and `plugin-kmp`. Requires `ru.pocketbyte.locolaser.kmp` or `ru.pocketbyte.locolaser.all` plugin.

→ [Back to root README](../README.md)

---

## DSL

Resource blocks are configured through the [LocoLaser Gradle plugin](../plugin/README.md) and go inside `source { }` or `platform { }` within `localize { config { } }`.

**build.gradle.kts:**
```kotlin
json {
    resourcesDir = "./locales/"   // default
    resourceName = "strings"      // → locales/en/strings.json
    indent = 2                    // optional; omit for compact JSON
    filter("^web_.*")             // optional; RegExp on keys
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.json.JsonResourcesConfig
add(JsonResourcesConfig.@Companion) {
    resourcesDir = "./locales/"
    resourceName = "strings"
    indent = 2
    filter("^web_.*")
}
```

---

## Parameters

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"strings"` | File name without `.json` extension. |
| `resourcesDir` | `"./"` | Base directory. Locale subdirectories are created inside it. |
| `indent` | `-1` | Spaces for pretty-printing. `-1` produces compact JSON. |
| `pluralKeyRule` | `KeyPluralizationRule.Postfix.Named()` | Rule for encoding plural keys. |
| `formattingType` | `WebFormattingType` | Formatting type applied to string values. |

---

## File layout

```
resourcesDir/
  base/strings.json    ← base locale
  en/strings.json      ← "en" locale
  de/strings.json      ← "de" locale
```

File format:
```json
{
  "app_title": "My App",
  "welcome_message": "Hello, {{name}}!"
}
```

---

## Plural support

Plural forms are encoded as separate keys with a postfix.

**`KeyPluralizationRule.Postfix.Named()`** (default — modern i18next):

| Quantity | Key pattern |
|---|---|
| `other` | `file_count_plural` |
| `one` | `file_count_plural_one` |
| `zero` | `file_count_plural_zero` |

**`KeyPluralizationRule.Postfix.Numeric()`** (legacy i18next):

| Quantity | Key pattern |
|---|---|
| `other` | `file_count_plural` |
| `one` | `file_count_plural_1` |
| `zero` | `file_count_plural_0` |

---

## Full example

**build.gradle.kts:**
```kotlin
import ru.pocketbyte.locolaser.*

localize {
    config("Web") {
        locales = setOf("base", "en", "de", "fr")
        source {
            googleSheet {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                credentialFile = "./service_account.json"
            }
        }
        platform {
            json {
                resourcesDir = "./src/locales/"
                resourceName = "translation"
                indent = 2
            }
        }
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.json.JsonResourcesConfig
localize {
    config("Web") {
        locales = ["base", "en", "de", "fr"]
        source {
            add(GoogleSheetResourcesConfig.@Companion) {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                credentialFile = "./service_account.json"
            }
        }
        platform {
            add(JsonResourcesConfig.@Companion) {
                resourcesDir = "./src/locales/"
                resourceName = "translation"
                indent = 2
            }
        }
    }
}
```
