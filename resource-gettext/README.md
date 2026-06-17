# Resource: GetText

The `resource-gettext` module reads and writes `.po` files in GNU GetText format.

Included in `plugin-all`. Requires `ru.pocketbyte.locolaser.all` plugin.

→ [Back to root README](../README.md)

---

## DSL

Resource blocks are configured through the [LocoLaser Gradle plugin](../plugin/README.md) and go inside `source { }` or `platform { }` within `localize { config { } }`.

**build.gradle.kts:**
```kotlin
gettext {
    resourcesDir = "./languages/"   // default
    resourceName = "messages"       // → languages/en/LC_MESSAGES/messages.po
    filter(".*")                    // optional; RegExp on keys
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfig
add(GetTextResourcesConfig.@Companion) {
    resourcesDir = "./languages/"
    resourceName = "messages"
}
```

---

## Parameters

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"messages"` | File name without `.po` extension. |
| `resourcesDir` | `"./"` | Base directory. Locale subdirectories are created inside it. |

---

## File layout

```
resourcesDir/
  base/LC_MESSAGES/messages.po    ← base locale
  en/LC_MESSAGES/messages.po      ← "en" locale
  de/LC_MESSAGES/messages.po      ← "de" locale
```

---

## Plural support

GetText has native plural support, but LocoLaser currently uses only the `other` quantity form. Use `trimUnsupportedQuantities = true` in the global config to discard other forms automatically.

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
            gettext {
                resourcesDir = "./src/locales/"
                resourceName = "messages"
            }
        }
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfig
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
            add(GetTextResourcesConfig.@Companion) {
                resourcesDir = "./src/locales/"
                resourceName = "messages"
            }
        }
    }
}
```
