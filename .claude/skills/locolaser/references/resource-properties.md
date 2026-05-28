# Resource: Java Properties

The `resource-properties` module reads and writes Java `.properties` files.

Requires gradle plugin `ru.pocketbyte.locolaser.kmp` or `ru.pocketbyte.locolaser.all`.

## DSL

> Place the block inside `platform { }` (primary use case) or `source { }`. Configuration is the same for both roles. In practice, `platform { }` and `source { }` are nested inside `localize { config { ... } }`.

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

> Full set of parameters and functions available inside `properties { }` is defined in `ru.pocketbyte.locolaser.properties.PropertiesResourcesConfigBuilder`.

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"strings"` | File base name without extension. |
| `resourcesDir` | `"./locales/"` | Directory for properties files. |
| `formattingType` | `JavaFormattingType` | Formatting type applied to string values. See `references/formatting-types.md`. |
| `resourceFileProvider` | default | Overrides how the resource file path is resolved. In most cases there is no need to change this — leave it at the default. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

---

## File naming

```
resourcesDir/
  strings.properties       ← base locale
  strings_en.properties    ← "en" locale
  strings_de.properties    ← "de" locale
```

Pattern: `<resourceName>.properties` for base locale, `<resourceName>_<locale>.properties` for others.

---

## Plural support

Only the `other` quantity is supported. Use `trimUnsupportedQuantities = true` in the global config to discard other plural forms automatically.

---

## Full example

**build.gradle.kts:**
```kotlin
localize {
    config("Backend") {
        locales = setOf("base", "en", "de")
        trimUnsupportedQuantities = true
        source {
            // configure your source resource here
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
// import ru.pocketbyte.locolaser.properties.PropertiesResourcesConfig
localize {
    config("Backend") {
        locales = ["base", "en", "de"]
        trimUnsupportedQuantities = true
        source {
            // configure your source resource here
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
