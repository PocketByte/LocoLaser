# Resource: GetText

The `resource-gettext` module reads and writes `.po` files in GNU GetText format.

Requires gradle plugin `ru.pocketbyte.locolaser.all`.

## DSL

> Place the block inside `platform { }` (primary use case) or `source { }`. Configuration is the same for both roles. In practice, `platform { }` and `source { }` are nested inside `localize { config { ... } }`.

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
    filter(".*")
}
```

---

## Parameters

> Full set of parameters and functions available inside `gettext { }` is defined in `ru.pocketbyte.locolaser.gettext.GetTextResourcesConfigBuilder`.

| Parameter | Default | Description |
|---|---|---|
| `resourceName` | `"messages"` | File name without `.po` extension. |
| `resourcesDir` | `"./"` | Base directory. Locale subdirectories are created inside it. |
| `resourceFileProvider` | `GetTextResourceFileProvider` | Overrides how the resource file path is resolved. In most cases there is no need to change this — leave it at the default. |
| `resourcesFilter` | none | `ResourcesFilter` instance; only keys that pass the filter are synced. Prefer `filter(regExp)` instead — set this directly only when regex filtering does not cover the case or there is a significant performance gain. |

| Function | Description |
|---|---|
| `filter(regExp: String)` | **Preferred.** Sets `resourcesFilter` to a `RegExResourcesFilter` matching the given regular expression. |
| `filter(filter: ResourcesFilter?)` | Sets `resourcesFilter` to a custom `ResourcesFilter` instance. Use only when regex filtering does not cover the case or there is a significant performance gain. |

---

## File naming

```
resourcesDir/
  base/LC_MESSAGES/messages.po    ← base locale
  en/LC_MESSAGES/messages.po      ← "en" locale
  de/LC_MESSAGES/messages.po      ← "de" locale
```

GetText follows the standard Unix locale directory structure with an `LC_MESSAGES/` subdirectory.

---

## Plural support

GetText has native plural support, but LocoLaser currently uses only the `other` quantity form from the source. When reading `.po` files as source, only `other` is imported.

Use `trimUnsupportedQuantities = true` in the global config to automatically discard other plural forms when writing.

---

## Full example

**build.gradle.kts:**
```kotlin
localize {
    config("Backend") {
        locales = setOf("base", "en", "de")
        conflictStrategy = Config.ConflictStrategy.KEEP_NEW_PLATFORM
        trimUnsupportedQuantities = true
        source {
            // configure your source resource here
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
// import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfig
localize {
    config("Backend") {
        locales = ["base", "en", "de"]
        conflictStrategy = Config.ConflictStrategy.KEEP_NEW_PLATFORM
        trimUnsupportedQuantities = true
        source {
            // configure your source resource here
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
