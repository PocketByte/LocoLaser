# LocoLaser

LocoLaser - Localization tool to import localized strings from external source to your project.
Utility support following resource types:
- Google Sheets
- Android resources: strings.xml
- iOS resources: Localizable.strings
- Kotlin Multiplatform: Generates repository classes for Android, iOS, macOS and JavaSript that can be used in common code.
- GetText resources: messages.pom
- Java Script i18next: strings.json
- INI File: strings.ini
- Java properties: strings.properties

##### Migration to 2.0.0

Please read [Migration instruction](docs/migration.md) to migrate to version 2.0.0

##### Related Git's

Android Example: https://github.com/PocketByte/locolaser-android-example  
iOS Example: https://github.com/PocketByte/locolaser-ios-example  
Kotlin MPP Example: https://github.com/PocketByte/locolaser-kotlin-mpp-example  

### How does it work
You need to apply LocoLaser plugin in `build.gradle.kts` and configure `localize` extension.  
Example of Android configuration that gets strings from Google Sheets:

```kotlin
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("ru.pocketbyte.locolaser:plugin-all:2.3.0")
    }
}

plugins {
    apply("ru.pocketbyte.locolaser.all")
}

localize {
    config {
        platform {
            android()
        }
        source {
            googleSheet {
                id = "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
                keyColumn = "key"
                commentColumn = "base"
                quantityColumn = "quantity"
                credentialFile = "../service_account.json"
            }
        }
        locales = setOf("base", "fi")
    }
}
```

When configuration is done it creates localization tasks in `localization` group.

### Config overrides

You can override config properties by calling corresponded tasks:
- `localize[ConfigName]ExportNew`: Exports new strings from platform resources to source.
- `localize[ConfigName]Force`: Forces localization ignoring cache.

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
