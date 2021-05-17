# LocoLaser Gradle plugin
LocoLaser - Localization tool for Android and iOS. See https://github.com/PocketByte/LocoLaser/ for more details.
<br>This gradle plugin simplify work with LocoLaser by adding several tasks and extensions.
### Usage
##### 1. Apply plugin
Build script snippet for use in all Gradle versions:
```gradle
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.ru.pocketbyte.locolaser:plugin:1.0.4"
  }
}

apply plugin: "ru.pocketbyte.locolaser"
```
Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```gradle
plugins {
  id "ru.pocketbyte.locolaser" version "1.0.4"
}
```
##### 2. Add dependency
Choose which type of artifact you will use and add it as **`localize`** dependency:
```gradle
dependencies {
    // Artifact for work with Mobile Platforms
    localize "ru.pocketbyte.locolaser:platform-mobile:1.2.8"
}
```
##### Tasks
Plugin add 3 tasks into **`localization`** group:
- **`localize`** - Run LocoLaser with default parameters;
- **`localizeForce`** - Run LocoLaser with force;
- **`localizeExportNew`** - Run LocoLaser with force and conflict strategy = export_new_platform.

**Note:** Before start one of this tasks make sure that you place configuration file in the root of the module folder. About configuration file format you can read in https://github.com/PocketByte/LocoLaser/blob/master/README.md
##### Extension
Plugin add **`localize`** extension. This extension has following property:
- **`config`** - File name with localization config. Default value `"localize_config.json"`

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
