# Platform: Android and iOS

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:platform-mobile:1.2.1'
}
```

### Config
Mobile Platform can be defined by single string or by JSON object. In case of string you can use following values:
- *`android`* - Adroid platform (Android XML Resource files). Default temp folder: "./build/tmp/";
- *`ios`* - iOS platform. (iOS string resource files). Default temp folder: "../DerivedData/LocoLaserTemp/".

JSON object should has following structure:
```
{
    "type" : ("android" | "ios"),
    "res_name" : (String value),
    "res_dir" : (Path to dir)
}
```
Properties description:<br>
- **`type`** - String. Type of the platform ("android" or "ios").
- **`res_name`** - String. Resource name.
  * Default Android: "strings",
  * Default iOS: "Localizable".
- **`res_dir`** - String. Path to resources directory.
  * Default Android: "./src/main/res/",
  * Default iOS: "./".

### Code generation
Also, in case of iOS platform you able to use platforms that generate a special Class files that simplify work with string resources. Code generaton config has following structure:
```
{
    "type" : ("ios_swift" | "ios_objc"),
    "res_name" : (String value),
    "res_dir" : (Path to dir),
    "table_name" : (String value)
}
```
Properties description:<br>
- **`type`** - String. Type of the platform:
  * *`ios_swift`* - gerate Swift class file",
  * *`ios_objc`* - gerate Objective-C class files",
- **`res_name`** - String. Name of the class. Default value is *`Str`*;
- **`res_dir`** - String. Path to source code directory. Default value is *`./`*;
- **`table_name`** - String. Name of the table in iOS bundle. Default value is *`Localizable`*.

**Note:** Don't forget to include generated file into XCode project.

### Example
Example of iOS config that generates Swift Class (*`Str.swift`*) depends on String resources:
```json
{
    "platform" : [
        "ios",
        {
            "type" : "ios_swift",
            "res_dir": "./utils/"
        }
    ],
    "source" : null,
    "delay" : 30
}
```