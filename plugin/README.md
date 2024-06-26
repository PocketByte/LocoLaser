# LocoLaser Gradle plugin

LocoLaser - Is Localization tool for many types of string resources.  
This gradle plugin simplify work with LocoLaser by adding several tasks and extensions.

### Usage

##### 1. Apply plugin

In **`build.gradle.kts`** of the module, apply LocoLaser plugin:

```kotlin
plugins {
    id("ru.pocketbyte.locolaser").version("2.3.1")
}
```

##### 2. Add dependency

Choose which type of artifact you will use and also add it as **`classpath`** dependency
in **`build.gradle.kts`** of the module:

```kotlin
buildscript {
    repositories {
        mavenCentral() // For resources artifacts
        ...
    }
    dependencies {
        classpath("ru.pocketbyte.locolaser:resource-mobile:2.3.1")
        classpath("ru.pocketbyte.locolaser:resource-googlesheet:2.3.1")
        ...
    }
}
```

##### 2. Create configuration

In same **`build.gradle.kts`** create localization configuration:

```kotlin
import ru.pocketbyte.locolaser.*
import ru.pocketbyte.locolaser.plugin.localize

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

Here `kotlinMultiplatform`, `android`, `ios` and `json` is an extensions for `ConfigResourceBuilder`.
Each locolaser artifact that you add in section `buildscript.dependencies` adds its own Kotlin Extension.

##### Extension

Plugin add **`localize`** extension. This extension has following methods:
- **`config(name, configSetup)`** - Creates config with provided name and configure it with closure configSetup.
- **`configFromFile(name, file, workDir)`** - **Depecated**. Don't use it. Will be removed in future.

##### Tasks

Plugin add 3 tasks into **`localization`** group:
- **`localize[ConfigName]`** - Run LocoLaser with default parameters;
- **`localize[ConfigName]Force`** - Run LocoLaser with force (ignoring cache);
- **`localize[ConfigName]ExportNew`** - Run LocoLaser with force and conflict strategy = export_new_platform.

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
