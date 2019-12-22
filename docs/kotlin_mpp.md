### Overview
Kotlin Mobile Platform it's extension of LocoLaser. It generate common interface of repository with localized strings and also implements it for each platform. Currently it supports Android and iOS.<br>
<br>
Common interface example:
```Kotlin
interface StringRepository {
    val screen_main_hello_text: String
}
```
Android implementation example:
```Kotlin
public class AndroidStringRepository(private val context: Context): StringRepository {

    private val resIds = mutableMapOf<String, MutableMap<String, Int>>()

    override public val screen_main_hello_text: String
        get() = this.context.getString(getId("screen_main_hello_text", "string"))

    private fun getId(resName: String, defType: String): Int {
        var resMap = resIds[defType]
        if (resMap == null) {
            resMap = mutableMapOf()
            resIds[defType] = resMap
        }

        var resId = resMap[resName]
        if (resId == null) {
            resId = context.resources.getIdentifier(resName, defType, context.packageName)
            resMap[resName] = resId
        }
        return resId
    }
}
```
iOS implementation example:
```Kotlin
public class IosStringRepository(private val bundle: NSBundle, private val tableName: String): StringRepository {
    constructor(bundle: NSBundle) : this(bundle, "Localizable")
    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)
    constructor() : this(NSBundle.mainBundle(), "Localizable")

    override public val screen_main_hello_text: String
        get() = this.bundle.localizedStringForKey("screen_main_hello_text", "", this.tableName)
}
```

### Gradle dependency
```gradle
dependencies {
    localize 'ru.pocketbyte.locolaser:platform-kotlin-mobile:1.2.8'
}
```

### Config
Each class or interface that need to be generated should be described by separated platform config. Config can be defined by JSON object.<br>
<br>
In general config of one source file should have following structure:
```
{
    "type": ("kotlin-common" | "kotlin-android" | "kotlin-ios"),
    "res_name" : (String value, Canonical Java name),
    "res_dir" : (Path to dir)
}
```
Properties description:<br>
- **`type`** - String. Type of the platform. For common kotlin interface it should be `"kotlin-common"`
- **`res_name`** - String. Desirable canonical name of repository interface or class implementation.
- **`res_dir`** - String. Path to directory with interface or class file.

### Implementation Configs
Android/iOS repository implementation config require one more additional parameter:
- **`implements`** - String. Canonical name of repository interface that should be implemented. In most cases it will have same value that `res_name` from `"kotlin-common"` config.

So, Android/iOS config should have following structure:
```
{
    "type": ("kotlin-android" | "kotlin-ios"),
    "res_name" : (String value, Canonical Java name),
    "res_dir" : (Path to dir),
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

### Usage
All configs of kotlin-mobile describes write only resources. So, to get list of strings of your application you should also add at least one config that describe resource that can be read. Here is the example of full config:
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
    "temp_dir": "./build/temp/"
}
```
This config has also description of [Android and iOS](mobile.md) resources. They are readable and Repository will be fulfilled by strings from this resource files.

### Example project
You can find example project in [locolaser-kotlin-mpp-example](https://github.com/PocketByte/locolaser-kotlin-mpp-example)
