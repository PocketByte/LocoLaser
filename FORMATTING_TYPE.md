# Formatting Type

Some resource configs has property **`formatting_type`**.
This property allows defining which type of formatted strings is used in current resource.

**`formatting_type`** is the String property with one of the following values:
- **`no`** - No formatting.
- **`java`** - Java formatting. For example: `"Hello, %s."`
- **`web`** - Web formatting. For example: `"Hello, {{user_name}}."`
- **`(Canonical Class Name)`** - Canonical class name of implementation of interface `FormattingType`.
  Class can be implemented as Kotlin Object.
