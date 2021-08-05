# Resources: Android and iOS

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:resource-mobile:2.0.0'
}
```

### Config
Mobile Resources can be defined by single string or by JSON object. In case of string you can use following values:
- *`android`* - Android platform (Android XML Resource files). Default temp folder: `"./build/tmp/"`;
- *`ios`* - iOS platform. (iOS string resource files). Default temp folder: `"../DerivedData/LocoLaserTemp/"`.

JSON object should has following structure:
```
{
    "type" : ("android" | "ios"),
    "res_name" : (String value),
    "res_dir" : (Path to dir),
    "filter" : (String value)
}
```
Properties description:  
- **`type`** - String. Type of the platform (`"android"` or `"ios"`).
- **`res_name`** - String. Resource name.
  * Default Android: `"strings"`,
  * Default iOS: `"Localizable"`.
- **`res_dir`** - String. Path to resources directory.
  * Default Android: `"./src/main/res/"`,
  * Default iOS: `"./"`.
- **`filter`** - RegExp String. If defined, only strings with keys that matches RegExp will be written into resource.
  By default, no filter.

### Code generation
Also, in case of iOS platform you able to use platforms that generate a special Class files that simplify work with string resources.
Code generation config has following structure:
```
{
    "type" : ("ios_swift" | "ios_objc"),
    "res_name" : (String value),
    "res_dir" : (Path to dir),
    "table_name" : (String value)
}
```
Properties description:  
- **`type`** - String. Type of the platform:
  * *`ios_swift`* - generate Swift class file,
  * *`ios_objc`* - generate Objective-C class files,
- **`res_name`** - String. Name of the class. Default value is *`Str`*;
- **`res_dir`** - String. Path to source code directory. Default value is *`./`*;
- **`table_name`** - String. Name of the table in iOS bundle. Default value is *`Localizable`*.

**Note:** Don't forget to include generated file into XCode project.

### Example
Example of iOS config that generates Swift Class (*`Str.swift`*) depends on String resources:
```json
{
    "platform" : {
        "type" : "ios_swift",
        "res_dir": "./utils/"
    },
    "source" : "ios",
    "locales" : ["base"],
    "delay" : 30
}
```

### Metadata
Android also handle following metadata of the string: `"formatted=(true|false)"` and `"xml-cdata=(true|false)"`.  
If metadata `"formatted"` exists the string xml element will contain `"formatted"` attribute.  
If metadata `"xml-cdata"` equal true the string will be surrounded by CDATA bracers.  
**IMPORTANT NOTE**: Except of other metadata "xml-cdata" can't be read from resource file and can be provided only from external source, for example, from metadata column in [Google Sheet](../resource-googlesheet/README.md).
