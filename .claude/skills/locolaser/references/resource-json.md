# Resource: JSON

The `resource-json` module reads and writes JSON files in i18next format.

Requires gradle plugin `ru.pocketbyte.locolaser.kmp` or `ru.pocketbyte.locolaser.all`.

## DSL

> Place the block inside `platform { }` (primary use case) or `source { }`. Configuration is the same for both roles. In practice, `platform { }` and `source { }` are nested inside `localize { config { ... } }`.

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

> Full set of parameters and functions available inside `json { }` is defined in `ru.pocketbyte.locolaser.json.JsonResourcesConfigBuilder`.

| Parameter | Default                                | Description |
|---|----------------------------------------|---|
| `resourceName` | `"strings"`                            | File name without `.json` extension. |
| `resourcesDir` | `"./"`                                 | Base directory. Locale subdirectories are created inside it. |
| `indent` | `-1`                                   | Number of spaces for pretty-printing. `-1` produces compact JSON. |
| `pluralKeyRule` | `KeyPluralizationRule.Postfix.Named()` | Rule for encoding plural keys. See Plural support section. Requires `import ru.pocketbyte.locolaser.json.KeyPluralizationRule`. |
| `formattingType` | `WebFormattingType`                    | Formatting type applied to string values. See `references/formatting-types.md`. |
| `resourceFileProvider` | default                                | Overrides how the resource file path is resolved. In most cases there is no need to change this — leave it at the default. |
| `resourcesFilter` | none                                   | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

---

## File naming

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

Plural forms are encoded as separate keys with a postfix. Two rules are available via `pluralKeyRule`:

**`KeyPluralizationRule.Postfix.Named()`** (default — modern i18next):

| Quantity | Key pattern |
|---|---|
| `other` | `file_count_plural` |
| `one` | `file_count_plural_one` |
| `zero` | `file_count_plural_zero` |
| `few` | `file_count_plural_few` |
| `many` | `file_count_plural_many` |

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
localize {
    config("Web") {
        locales = setOf("base", "en", "de", "fr")
        source {
            // configure your source resource here
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
// import ru.pocketbyte.locolaser.json.JsonResourcesConfig
localize {
    config("Web") {
        locales = ["base", "en", "de", "fr"]
        source {
            // configure your source resource here
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
