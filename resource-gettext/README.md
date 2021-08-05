# Resources: GetText

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:resource-gettext:2.0.0'
}
```

### Config
GetText Resources can be defined by single string or by JSON object. In case of string you can use value `"gettext"`.  
JSON object should have following structure:
```
{
    "type" : "gettext",
    "res_name" : (String value),
    "res_dir" : (Path to dir),
    "filter" : (String value)
}
```
Properties description:  
- **`type`** - String. Type of the resource. In case of GetText should be used value `"gettext"`.
- **`res_name`** - String. Resource file name. Default value: `"messages"`.
- **`res_dir`** - String. Path to resources directory. Default value: `"./languages/"`.
- **`filter`** - RegExp String. If defined, only strings with keys that matches RegExp will be written into resource.
  By default, no filter.

### Plurals
Plurals are not supported in GetText resource implementation. All quantities except OTHER will be ignored.

### Example
Here is the example of LocoLaser config where GetText used as a platform.
```json
{
    "platform" : {
        "type" : "gettext",
        "res_name" : "messages_intro",
        "filter" : "screen_intro__*"
    },
    "source" : "android",
    "locales" : ["en", "fi"]
}
```