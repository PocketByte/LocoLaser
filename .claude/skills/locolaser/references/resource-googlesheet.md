# Resource: Google Sheets

Google Sheets is typically used as **Source** (strings are read from it). It can also be used as Platform to export strings back to a sheet.

Requires gradle plugin `ru.pocketbyte.locolaser.all`.

## DSL

> The examples below show only the resource block. In practice, `source { }` and `platform { }` are nested inside `localize { config { ... } }`.

As source (primary use case — strings are read from the spreadsheet):

**build.gradle.kts:**
```kotlin
source {
    googleSheet {
        id = "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
        keyColumn = "key"
        quantityColumn = "quantity"           // optional; enables plural support
        commentColumn = "comment"             // optional
        metadataColumn = "metadata"           // optional; Android-specific attributes
        worksheetTitle = "Strings"            // optional; first sheet if omitted
        credentialFile = "./service_account.json"
        formattingType = JavaFormattingType
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
source {
    add(GoogleSheetResourcesConfig.@Companion) {
        id = "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
        keyColumn = "key"
        quantityColumn = "quantity"
        commentColumn = "comment"
        metadataColumn = "metadata"
        worksheetTitle = "Strings"
        credentialFile = "./service_account.json"
        formattingType = JavaFormattingType.INSTANCE
    }
}
```

As platform (not the primary use case — used for exporting strings back to the sheet):

**build.gradle.kts:**
```kotlin
platform {
    googleSheet {
        // configuration is the same as in the source block above
    }
}
```

**build.gradle:**
```groovy
platform {
    add(GoogleSheetResourcesConfig.@Companion) {
        // configuration is the same as in the source block above
    }
}
```

---

## Parameters

> Full set of parameters and functions available inside `googleSheet { }` is defined in `ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfigBuilder`. Column parameters (`keyColumn`, `quantityColumn`, `commentColumn`, `metadataColumn`) are defined in `ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfigBuilder`.

| Parameter | Default | Description |
|---|---|---|
| `id` | — **(required)** | Spreadsheet ID from the URL: `https://docs.google.com/spreadsheets/d/{id}/`. |
| `keyColumn` | `"key"` | Header of the column containing string keys. |
| `quantityColumn` | `null` | Header of the quantity column. Enables plural support when set. |
| `commentColumn` | `null` | Header of the comment column. Comments appear as code comments in platform files. |
| `metadataColumn` | `null` | Header of the metadata column. Used for Android-specific string attributes. |
| `worksheetTitle` | `null` | Tab name inside the spreadsheet. If `null`, uses the first worksheet. |
| `credentialFile` | `null` | Path to a Google service account JSON file. Required for CI/headless environments. If `null`, interactive OAuth is used. |
| `formattingType` | `JavaFormattingType` | Formatting type applied to string values. See `references/formatting-types.md`. |

---

## Sheet column layout

LocoLaser expects locale columns alongside the key column. The column header must match the locale code exactly:

| key | base | en | de | quantity | comment |
|---|---|---|---|---|---|
| app_title | App | App | App | | Application title |
| items_count | %1$d items | %1$d items | %1$d Elemente | | |
| items_count | %1$d item | %1$d item | %1$d Element | one | |

- `base` column → default/fallback locale resource
- Other locale columns → named locale resources
- Use `quantityColumn` + repeated key rows for plural forms

---

## Plural support

Add a quantity column (name passed to `quantityColumn`) and repeat the key row for each plural form:

| key | base | en | quantity |
|---|---|---|---|
| file_count | %1$d files | %1$d files | other |
| file_count | %1$d file | %1$d file | one |
| file_count | %1$d files | %1$d files | zero |

Quantity values: `zero`, `one`, `two`, `few`, `many`, `other`.

Use `trimUnsupportedQuantities = true` in the global config to drop forms unsupported by the target platform automatically.

---

## Metadata (Android-specific)

The `metadataColumn` value is a semicolon-separated list of `key=value` pairs:

```
formatted=false;xml-cdata=true
```

This produces:
```xml
<string name="my_key" formatted="false"><![CDATA[value]]></string>
```

Common attributes:
- `formatted=false` — marks string as not using `String.format()`
- `xml-cdata=true` — wraps value in `<![CDATA[...]]>`
- `translatable=false` — marks string as non-translatable in Android

Metadata is source-only — it cannot be read back from Android XML files on re-import.

---

## Credentials setup

1. Create a service account in Google Cloud Console.
2. Download the JSON key file.
3. Share the spreadsheet with the service account email (Viewer access is enough for source).

Without `credentialFile`, LocoLaser attempts interactive OAuth — only works on developer machines with a browser.

---

## Full example

**build.gradle.kts:**
```kotlin
localize {
    config("Strings") {
        locales = setOf("base", "en", "de")
        source {
            googleSheet {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                quantityColumn = "quantity"
                credentialFile = "./service_account.json"
                formattingType = JavaFormattingType
            }
        }
        platform {
            // configure your platform resource here
        }
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
localize {
    config("Strings") {
        locales = ["base", "en", "de"]
        source {
            add(GoogleSheetResourcesConfig.@Companion) {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                quantityColumn = "quantity"
                credentialFile = "./service_account.json"
                formattingType = JavaFormattingType.INSTANCE
            }
        }
        platform {
            // configure your platform resource here
        }
    }
}
```
