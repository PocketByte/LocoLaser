# Resources: Properties

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:resource-properties:2.0.0'
}
```

### Config
Properties Resources can be defined by single string or by JSON object. In case of string you can use value `"properties"`.<br>
JSON object should has following structure:
```
{
    "type" : "properties",
    "res_name" : (String value),
    "res_dir" : (Path to dir)
}
```
Properties description:<br>
- **`type`** - String. Type of the resource. In case of Properties should be used value `"properties"`.
- **`res_name`** - String. Resource file name. Default value: `"strings"`.
- **`res_dir`** - String. Path to resources directory. Default value: `"./locales/"`.

### Plurals
Plurals are not supported in Properties resource implementation. All quantities except OTHER will be ignored.

### Example
Here is the example of LocoLaser config where as a platform used Properties file.
```json
{
    "platform" : {
        "type" : "properties",
        "res_dir" : "./lang/"
    },
    "source" : "android",
    "locales" : ["en", "fi"]
}
```