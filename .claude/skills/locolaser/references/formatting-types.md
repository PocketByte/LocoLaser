# Formatting Types

Formatting types define how argument placeholders are represented in localization strings. They control how placeholders are parsed, validated, and converted between formats.

Built-in types are Kotlin singleton objects. Import them explicitly — they are **not** covered by `import ru.pocketbyte.locolaser.*`:

```kotlin
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
```

---

## Built-in types

### `JavaFormattingType` — indexed placeholders

Format: `%1$s`, `%2$d`, `%3$f`

```
"Hello, %1$s. You have %2$d items."
```

- Arguments are positional and typed.
- Default for Android, iOS, Java Properties, INI, Google Sheets.
- Use when the platform uses `String.format()` or Kotlin's `format()`.

### `WebFormattingType` — named placeholders

Format: `{{name}}`, `{{count}}`

```
"Hello, {{user_name}}. You have {{count}} items."
```

- Arguments are referenced by name, not position.
- Default for JSON (i18next) and web frameworks.
- Use for JS/web platforms.

### `NoFormattingType` — no placeholders

```
"Hello, user. You have items."
```

- No placeholder parsing; format arguments are ignored.
- Default for Kotlin MPP abs-variants (`absKeyValue`, `absStatic`).
- Use for purely static strings or test stubs where argument content doesn't matter.

---

## Which type to choose

| Situation | Type |
|---|---|
| Android / iOS / Java / Kotlin strings | `JavaFormattingType` |
| Web / i18next JSON | `WebFormattingType` |
| No dynamic values in any string | `NoFormattingType` |
| Mixed platforms | Use `JavaFormattingType` on source; LocoLaser converts to each platform's format |

---

## Where to set formatting type

### On the source (most common)

Set on the source resource so LocoLaser knows how to interpret placeholders:

```kotlin
source {
    googleSheet {
        id = "..."
        formattingType = JavaFormattingType
    }
}
```

### On a Kotlin MPP abs-variant

```kotlin
platform {
    kotlinMultiplatform {
        absKeyValue("common") {
            formattingType = JavaFormattingType
        }
    }
}
```

---

## Custom formatting type

Implement the `FormattingType` interface and pass the instance directly:

```kotlin
object MyFormattingType : FormattingType {
    override val argumentsSubstitution = FormattingType.ArgumentsSubstitution.BY_INDEX
    override fun argumentsFromValue(value: String): List<FormattingArgument>? { ... }
    override fun convert(value: ResValue): ResValue { ... }
    override fun convertToJava(value: ResValue): ResValue { ... }
}

// usage:
source {
    googleSheet {
        formattingType = MyFormattingType
    }
}
```
