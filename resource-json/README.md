# Resources: JSON for i18next

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:resource-json:2.0.0'
}
```

### Config
JSON Resources can be defined by single string or by JSON object. In case of string you can use value `"json"`.  
JSON object should have following structure:
```
{
    "type" : "json",
    "res_name" : (String value),
    "res_dir" : (Path to dir),
    "indent": (Integer value),
    "filter" : (String value)
}
```
Properties description:  
- **`type`** - String. Type of the resource. In case of JSON should be used value `"json"`.
- **`res_name`** - String. Resource file name. Default value: `"strings"`.
- **`res_dir`** - String. Path to resources directory. Default value: `"./locales/"`.
- **`indent`** - Integer. JSON indent. Set this property to prettify result JSON. Default value: no indent. 
- **`filter`** - RegExp String. If defined, only strings with keys that matches RegExp will be written into resource.
  By default, no filter.

### Plurals
Plurals are supported in JSON resource implementation but with some restrictions.
Keys for plural string should be in following pattern `"<key>_plural_<quantity>"`, where:
- **`<key>`** - Is the key of the string.
- **`<quantity>`** - Numeric representation of quantity. To get more details please refer to official documentation of i18next.

For "OTHER" quantity also can be used simplified key pattern `"<key>_plural"`.

### Example
Here is the example of LocoLaser config where JSON used as a platform.
```json
{
    "platform" : {
        "type" : "json",
        "res_name" : "strings_intro",
        "filter" : "screen_intro__*"
    },
    "source" : "android",
    "locales" : ["en", "fi"]
}
```