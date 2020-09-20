# Platform: Kotlin Mobile Multiplatform

### Overview
Kotlin Mobile Platform it's extension of LocoLaser.
It generates the interface of common repository with strings resources and then implementations for each platform.
Currently, it supports Android, iOS and JavaScript.<br>
<br>
Common interface example:
```Kotlin
interface StringRepository {
    val screen_main_hello_text: String
}
```
Each platform implements following interface using corresponded platform dependent features.

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:resource-kotlin-mobile:2.0.0'
}
```

### Config
Each class or interface that need to be generated should be described by separated platform config.
Config can be defined by JSON object.<br>
<br>
In general config of one source file should have following structure:
```
{
    "type": ("kotlin-common" | "kotlin-android" | "kotlin-ios"),
    "res_name" : (String value, Canonical Java name),
    "res_dir" : (Path to dir),
    "filter" : (String value)
}
```
Properties description:<br>
- **`type`** - String. Type of the platform. For common kotlin interface it should be `"kotlin-common"`.
- **`res_name`** - String. Desirable canonical name of repository interface or class implementation.
- **`res_dir`** - String. Path to directory with interface or class file.
- **`filter`** - RegExp String. If defined, only strings with keys that matches RegExp will be written into resource. By default no filter.

### Implementation Configs
Repository implementation config require one more additional parameter:
- **`implements`** - String. Canonical name of repository interface that should be implemented.
 In most cases it will have the same value that `res_name` from `"kotlin-common"` config.

So, Android,iOS or JS config should have following structure:
```
{
    "type": ("kotlin-android" | "kotlin-ios", "kotlin-js"),
    "res_name" : (String value, Canonical Java name),
    "res_dir" : (Path to dir),
    "filter" : (String value),
    "implements" : (String value, Canonical Java name)
}
```

### Default values
For `"kotlin-common"`:
- **`res_dir`** - `"./src/commonMain/kotlin/"`
- **`res_name`** - `"ru.pocketbyte.locolaser.kmpp.StringRepository"`

For `"kotlin-android"`:
- **`res_dir`** - `"./src/androidMain/kotlin/"`
- **`res_name`** - `"ru.pocketbyte.locolaser.kmpp.AndroidStringRepository"`
- **`implements`** - `ru.pocketbyte.locolaser.kmpp.StringRepository`

For `"kotlin-ios"`:
- **`res_dir`** - `"./src/iosMain/kotlin/"`
- **`res_name`** - `"ru.pocketbyte.locolaser.kmpp.IosStringRepository"`
- **`implements`** - `ru.pocketbyte.locolaser.kmpp.StringRepository`

For `"kotlin-js"`:
- **`res_dir`** - `"./src/jsMain/kotlin/"`
- **`res_name`** - `"ru.pocketbyte.locolaser.kmpp.JsStringRepository"`
- **`implements`** - `ru.pocketbyte.locolaser.kmpp.StringRepository`

### Usage
All configs of kotlin-mobile describes write only resources.
So, to get list of strings of your application you should also add at least one config that describe resource that can be read.
Here is the example of full config:
```
{
    "platform": [
        {
            "type": "android",
            "res_dir": "./app_android/src/main/res/"
        },
        {
            "type": "ios",
            "res_dir": "./app_ios/locolaser-kotlin-multiplatform-example/"
        },
        {
            "type": "kotlin-common",
            "res_dir": "./common/build/generated/kotlin/",
            "res_name": "ru.pocketbyte.locolaser.example.repository.StringRepository"
        },
        {
            "type": "kotlin-android",
            "res_dir": "./app_android/build/generated/kotlin/",
            "res_name": "ru.pocketbyte.locolaser.example.repository.AndroidStringRepository",
            "implements": "ru.pocketbyte.locolaser.example.repository.StringRepository"
        },
        {
            "type": "kotlin-ios",
            "res_dir": "./app_ios/build/generated/kotlin/",
            "res_name": "ru.pocketbyte.locolaser.example.repository.IosStringRepository",
            "implements": "ru.pocketbyte.locolaser.example.repository.StringRepository"
        }
    ],
    "source": "null",
    "locales" : ["base"],
    "temp_dir": "./build/temp/"
}
```
This config has also description of [Android and iOS](../resource-mobile/README.md) resources.
They are readable and Repository will be fulfilled by strings from this resource files.

### Custom Repository
You able to implement your custom repository for any platform.
To do it you should use type `kotlin-abs-key-value`.
Then LocoLaser will generate implementation of abstract repository with interface `StringProvider`, which should be implemented and provided to repository class thru constructor.

If you would to generate formatting functions for formatted strings you should provide formatting type thru the property **`formatting_type`**.

It's the String property with one of the following values:
- **`no`** - No formatting.
- **`java`** - Java formatting. For example: `"Hello, %s."`
- **`web`** - Web formatting. For example: `"Hello, {{user_name}}."`
- **`(Canonical Class Name)`** - Canonical class name of implementation of interface `FormattingType`. Class can be implemented as Kotlin Object.

So, Custom config should have following structure:
```
{
    "type": "kotlin-abs-key-value",
    "res_name" : (String value, Canonical Java name),
    "res_dir" : (Path to dir),
    "filter" : (String value),
    "implements" : (String value, Canonical Java name),
    "formatting_type" : (String value, Canonical Java name)
}
```

Default values for `"kotlin-abs-key-value"`:
- **`res_dir`** - `"./src/main/kotlin/"`
- **`res_name`** - `"ru.pocketbyte.locolaser.kmpp.AbsKeyValueStringRepository"`
- **`filter`** - By default no filter.
- **`implements`** - `"ru.pocketbyte.locolaser.kmpp.StringRepository"`
- **`formatting_type`** - `"no"`

### Example project
You can find example project in [locolaser-kotlin-mpp-example](https://github.com/PocketByte/locolaser-kotlin-mpp-example)
