# Resource: Google Sheets

Google Sheets is typically used as **Source** (strings are read from it). It can also be used as Platform to export strings back to a sheet.

Included in `plugin-all`. Requires `ru.pocketbyte.locolaser.all` plugin.

→ [Back to root README](../README.md)

---

## DSL

Resource blocks are configured through the [LocoLaser Gradle plugin](../plugin/README.md) and go inside `localize { config { } }`.

**build.gradle.kts:**
```kotlin
source {
    googleSheet {
        id = "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
        keyColumn = "key"
        quantityColumn = "quantity"     // optional; enables plural support
        commentColumn = "comment"       // optional
        metadataColumn = "metadata"     // optional; Android-specific attributes
        worksheetTitle = "Strings"      // optional; first sheet if omitted
        credentialFile = "./service_account.json"
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
source {
    add(GoogleSheetResourcesConfig.@Companion) {
        id = "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
        keyColumn = "key"
        quantityColumn = "quantity"
        commentColumn = "comment"
        credentialFile = "./service_account.json"
    }
}
```

---

## Parameters

| Parameter | Default | Description |
|---|---|---|
| `id` | **(required)** | Spreadsheet ID from the URL: `https://docs.google.com/spreadsheets/d/{id}/`. |
| `keyColumn` | `"key"` | Header of the column containing string keys. |
| `quantityColumn` | `null` | Header of the quantity column. Enables plural support when set. |
| `commentColumn` | `null` | Header of the comment column. |
| `metadataColumn` | `null` | Header of the metadata column. Used for Android-specific string attributes. |
| `worksheetTitle` | `null` | Tab name inside the spreadsheet. If `null`, uses the first worksheet. |
| `credentialFile` | `null` | Path to a Google service account JSON file. Required for CI/headless environments. If `null`, interactive OAuth is used. |
| `formattingType` | `JavaFormattingType` | Formatting type applied to string values. |

---

## Sheet layout

LocoLaser expects locale columns alongside the key column. The column header must match the locale code exactly:

| key | base | en | de | quantity | comment |
|---|---|---|---|---|---|
| app_title | App | App | App | | Application title |
| file_count | %1$d files | %1$d files | %1$d Dateien | other | |
| file_count | %1$d file | %1$d file | %1$d Datei | one | |

- `base` column → default/fallback locale
- Repeat rows with the same key and different `quantity` values for plural forms
- Quantity values: `zero`, `one`, `two`, `few`, `many`, `other`

---

## Credentials setup

1. Go to [Google Cloud Console](https://console.cloud.google.com/) and create a project.
2. Open **Service Accounts**, create a service account, and generate a JSON key.
3. Share the spreadsheet with the service account email (Viewer access is sufficient for source; Editor for export).
4. Pass the path to the key file via `credentialFile`.

For a detailed walkthrough see [credential_file_tut/README.md](credential_file_tut/README.md).

Without `credentialFile`, LocoLaser falls back to interactive OAuth — only works on developer machines with a browser.

---

## Metadata (Android-specific)

The `metadataColumn` value is a semicolon-separated list of `key=value` pairs:

```
formatted=false;xml-cdata=true
```

Common attributes:
- `formatted=false` — marks string as not using `String.format()`
- `xml-cdata=true` — wraps value in `<![CDATA[...]]>`
- `translatable=false` — marks string as non-translatable in Android

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
                quantityColumn = "quantity"
                credentialFile = "./service_account.json"
            }
        }
        platform {
            android { resourcesDir = "./app/src/main/res/" }
        }
    }
}
```

**build.gradle:**
```groovy
// import ru.pocketbyte.locolaser.google.GoogleSheetResourcesConfig
// import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
localize {
    config {
        locales = ["base", "en", "de"]
        source {
            add(GoogleSheetResourcesConfig.@Companion) {
                id = "YOUR_SHEET_ID"
                keyColumn = "key"
                quantityColumn = "quantity"
                credentialFile = "./service_account.json"
            }
        }
        platform {
            add(AndroidResourcesConfig.@Companion) { resourcesDir = "./app/src/main/res/" }
        }
    }
}
```
