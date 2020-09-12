# LocoLaser Config

Config is a file with JSON object that must contain configuration of resources (platform and source):
```
{
    "platform" : (String, JSON object or Array of Strings and JSON objects),
    "source" : (String, JSON object or Array of Strings and JSON objects),
    "locales" : (Array of strings),
    "conflict_strategy" : (
        "remove_platform"   |
        "keep_new_platform" |
        "export_new_platform"
    ),
    "work_dir" : (Path to dir),
    "temp_dir" : (Path to dir),
    "force_import" : (false | true),
    "duplicate_comments" : (false | true),
    "delay" : (Integer value)
}
```
##### Resources
Resource can be defined by single string("android", "ios" etc.), by JSON object that represent configured resource or 
by Array of resources that can contain both Strings and JSON objects.<br>
Currently LocoLaser support following resources:
- [Android and iOS](mobile.md)
- [Kotlin Mobile Multiplatform](kotlin_mpp.md)
- JavaScript (by i18next)
- GetText
- INI file
- [Google Sheet](google_sheet.md)
- **NULL** (Also you able to use string **`null`** to define empty resource)

Any Resource can be used as a Platform or as a Source.
Configuration fields **`platform`** and **`source`** are mandatory, so you could provide at least one of them. If you wouldn't you should use **NULL** Resource type.

##### Other config properties
- **`locales`** - Array of String. List of locales that should be handled by LocoLaser. Mandatory field. You can use keyword `base` to specify base locale.
- **`work_dir`** - String. Path to work directory. Other related paths will be related to this work dir. By default is directory of the configuration file.
- **`temp_dir`** - String. Path to directory for temporary files. By default will used default temp folder of the first platform.
- **`force_import`** - Boolean. Import doesn't execute without a need, but if `force_import` is `true` import will be executed any way.
- **`conflict_strategy`** - String. Define which action should performed for conflicts. There is 3 actions:
  * `remove_platform` - Remove platform resources and replace it with resources from sheet. Default value.
  * `keep_new_platform` - Keep new platform resources if sheet doesn't contain this resources.
  * `export_new_platform` -  New platform resources should be exported into source if source doesn't contain this resources.
- **`duplicate_comments`** - Boolean. If false comment will not be written if it equal localized string. Default value false.
- **`delay`** - Long. Time in minutes that define delay for next localization. Localization will executed not more often the specified delay. If force import switch on delay will be ignored.

##### Example
```json
{
    "platform" : "android",
    "source" : {
        "type" : "googlesheet",
        "column_key" : "key",
        "id" : "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
    },
    "locales" : ["en", "fi"],
    "delay" : 30
}
```

#### Override config properties via console arguments
You can override config properties by adding additional console arguments:
- **`--force`** or **`--f`** - Sets `force_import = true`.
- **`-cs`** - String. Override config property `conflict_strategy`.
- **`-delay`** - Long. Override config property `delay`.
- **`-workDir`** - String. Override config property `work_dir`.
- **`-tempDir`** - String. Override config property `temp_dir`.

For example:
``` Bash
java -cp "core.jar:platform-mobile.jar:source-googlesheet.jar" ru.pocketbyte.locolaser.Main "localization_config.json" --f -cs export_new_platform
```