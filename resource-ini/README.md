# Resources: INI

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:resource-ini:2.0.0'
}
```

### Config
INI Resources can be defined by single string or by JSON object. In case of string you can use value `"ini"`.  
JSON object should have following structure:
```
{
    "type" : "ini",
    "res_name" : (String value),
    "res_dir" : (Path to dir)
}
```
Properties description:  
- **`type`** - String. Type of the resource. In case of INI should be used value `"ini"`.
- **`res_name`** - String. Resource file name. Default value: `"data"`.
- **`res_dir`** - String. Path to resources directory. Default value: `"./"`.

### Plurals
Plurals are fully supported in INI resource implementation.

### Example
Here is the example of LocoLaser config where INI file used as a platform.
```json
{
    "platform" : {
        "type" : "ini",
        "res_dir" : "./lang/"
    },
    "source" : "android",
    "locales" : ["en", "fi"]
}
```