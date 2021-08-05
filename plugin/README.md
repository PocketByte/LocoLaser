# LocoLaser Gradle plugin
LocoLaser - Is Localization tool for many types of string resources. See https://github.com/PocketByte/LocoLaser/ for more details.  
This gradle plugin simplify work with LocoLaser by adding several tasks and extensions.
### Usage
##### 1. Apply plugin
In **`build.gradle.kts`** of the module apply LocoLaser
```kotlin
plugins {
    id("ru.pocketbyte.locolaser").version("2.2.1")
}
```
##### 2. Add dependency
Choose which type of artifact you will use and also add it as **`classpath`** dependency in **`build.gradle.kts`** of the module:
```kotlin
buildscript {
    repositories {
        mavenCentral() // For resources artifacts
        ...
    }
    dependencies {
        classpath("ru.pocketbyte.locolaser:resource-mobile:2.2.1")
        classpath("ru.pocketbyte.locolaser:resource-googlesheet:2.2.1")
        ...
    }
}
```
##### 2. Create configuration
In same **`build.gradle.kts`** create localization configuration:
```kotlin
// 2: Configure localization config
localize {
    config("KMP") {
        platform {
            kotlinMultiplatform {
                srcDir("${project.projectDir}/build/generated/src/")
                android()
                ios()
                js()
            }
        }
        source {
            android { resourcesDir("../app_android/src/main/res") }
            ios { resourcesDir("../app_ios/locolaser-kotlin-multiplatform-example/") }
            json { resourcesDir("../app_js/src/main/resources/locales/") }
        }
    }
}
```

##### Tasks
Plugin add 3 tasks into **`localization`** group:
- **`localize[ConfigName]`** - Run LocoLaser with default parameters;
- **`localize[ConfigName]Force`** - Run LocoLaser with force;
- **`localize[ConfigName]ExportNew`** - Run LocoLaser with force and conflict strategy = export_new_platform.

**Note:** Before start one of this tasks make sure that you place configuration file in the root of the module folder.
About configuration file format you can read in https://github.com/PocketByte/LocoLaser/blob/master/README.md

##### Extension
Plugin add **`localize`** extension. This extension has following methods:
- **`config(name, configSetup)`** - Creates config with provided name and configure it with closure configSetup.
- **`configFromFile(name, file, workDir)`** - Creates config with provided name from JSON File with localization config. Default value `"localize_config.json"`

### License
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
