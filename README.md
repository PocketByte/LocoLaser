# LocoLaser
It's the simple tool for Android and iOS to import localized strings from Google Sheets.
### How to use
##### Android studio. Gradle:
1) Create folder '**_build-tool-libs_**' inside module folder.<br>
2) Put '**_locolaser-google.jar_**' into '**_build-tool-libs_**'.<br>
3) Put config file into module folder.<br>

Project file structure should looks following:
```
Module folder
├─ build
│   └─ ...
├─ build-tool-libs
│   └─ locolaser-google.jar
├─ src
│   └─ ...
│   ...
├─ build.gradle
└─ locolaser_config.json
```

4) Edit module **_build.gradle_** to:<br>
```java
apply plugin: 'com.android.application'

android {
    ...
}

// Add new configuration
configurations {
    build_tool
}

dependencies {

    ...

    // Add LocoLaser jar to the build_tool configuration
    build_tool files('build-tool-libs/locolaser-google.jar')
}

// Run LocoLaser before build
preBuild.doFirst {
    javaexec {
        classpath configurations.build_tool
        main = "ru.pocketbyte.locolaser.Main"
        args = ["locolazer_config.json"]
        ignoreExitValue = false
    }
}
```
Done!
##### Console command example:
```
java -jar locolaser-google.jar locolaser_config.json
```

### Config structure
Config is a JSON object that must contain configuration of platform and source.<br>
For example:
```json
{
    "platform":"android",
    "source": {
        "type":"googlesheet",
        "column_key":"key",
        "column_locales":["en", "fi"],
        "id":"1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
    }
}
```
##### Platform
Platform can be defined by single string("android" or "ios") or by JSON object.<br>
JSON object should contain following properties:<br>
- **`type`** - String. Type of the platform ("android" or "ios").
- **`res_name`** - String. Resource name.
  * Default Android: "strings",
  * Default iOS: "Localizable".
- **`res_dir`** - String. Path to resources directory.
  * Default Android: ".\\\\src\\\\main\\\\res\\\\",
  * Default iOS: ".\\\\".
- **`temp_dir`** - String. Path to directory with temporary files.
  * Default Android: ".\\\\build\\\\tmp\\\\",
  * Default iOS: ".\\\\tmp\\\\".

##### Google Sheet
Google Sheet should contain following properties:<br>
- **`type`** - String. Type of the source. Must be "googlesheet". Not necessary property but if you want to be sure that you use right tool you should define it.
- **`id`** - String. ID of the Google Sheet. You can get it from sheet url (https://docs.google.com/spreadsheets/d/**sheet_id**).
- **`worksheet_title`** - Title of the worksheet with localized strings. Not necessary property, by default will used first worksheet of the sheet.
- **`column_key`** - String. Column title which contain string key's.
- **`column_locales`** - String array. Array of the column titles which contain localized strings. Column title will be used for locale name. First locale will import as base locale.
- **`column_quantity`** - String. Column title which contain quantity. Not necessary property, by default no quantities.
- **`column_comment`** - String. Column title which contain comment. Not necessary property, by default no comments.
- **`credential_file`** - String. Path to OAUth credential file. Not necessary property.

##### Other config properties
- **`root_dir`** - String. Path to root directory. Other related paths will be related to this root dir. By default is directory of the configuration file.
- **`force_import`** - Boolean. Import doesn't execute without a need, but if `force_import` is `true` import will be executed any way.
- **`conflict_strategy`** - String. Define which action should performed for conflicts. There is 3 actions:
  * `remove_local` - Remove local resources and replace it with resources from sheet. Default value.
  * `keep_new_local` - Keep local resource if sheet doesn't contain this resource.
  * `export_new_local` -  Resource should be exported from locale resources into sheet if sheet doesn't contain this resource.
- **`duplicate_comments`** - Boolean. If false comment will not be written if it equal localized string. Default value false.
- **`delay`** - Long. Time in minutes that define delay for next localization. Localization will executed not more often the specified delay. If force import switch on delay will be ignored.

### Console arguments
You can override config properties by adding following additional console arguments:
- **`--force`** or **`--f`** - Sets `force_import = true`.
- **`--export`** or **`--e`** - Sets `conflict_strategy = export_new_local`.
- **`-delay`** - Long. Override config property `delay`.

For example:
```
java -jar locolaser-google.jar locolaser_config.json --f --e
```

## License
```
Copyright © 2017 Denis Shurygin. All rights reserved.
Contacts: <mail@pocketbyte.ru>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
