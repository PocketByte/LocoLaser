# LocoLaser
LocoLaser - Localization tool to import localized strings from external source to your project. Utility support following platforms and sources:
- Platform Android: strings.xml
- Platform iOS: Localizable.strings
- Platform GetText: messages.pom
- Source Google Sheets


##### Related Git's
Gradle plugin: https://github.com/PocketByte/locolaser-gradle-plugin<br>
Android Example: https://github.com/PocketByte/locolaser-android-example<br>
iOS Example: https://github.com/PocketByte/locolaser-ios-example
### How does it work
At first you need to create configuration file in JSON format that contains configuration of platforms and sources. Here is the detailed instruction for configuring: [LocoLaser Config](https://github.com/PocketByte/LocoLaser/wiki/LocoLaser-Config)<br>
Example of Android config that gets strings from Google Sheets:
```json
{
    "platform" : "android",
    "source" : {
        "type" : "googlesheet",
        "column_key" : "key",
        "column_locales" : ["en", "fi"],
        "id" : "1KDu0_iel5qoNTKHZI0e4l3Uy52WisdfswYRy_GlFOPtY"
    },
    "delay" : 30
}
```
When configuration is created you should run LocoLaser with corresponding set of jar artifacts which supports platforms and sources from configuration.
Example of console command that starts LocoLaser:
``` Bash
java -cp "core.jar:platform-mobile.jar:source-googlesheet.jar" ru.pocketbyte.locolaser.Main "localization_config.json"
```
These artifacts can be download from [maven repository](https://bintray.com/pocketbyte/maven). But there is another simplests ways. If you use gradle you can use [LocoLaser gradle plugin](https://github.com/PocketByte/locolaser-gradle-plugin) instead. Otherwise in [LocaLaser iOS Example](https://github.com/PocketByte/locolaser-ios-example/) you can find [localize.command](https://github.com/PocketByte/locolaser-ios-example/blob/master/locloaser-example-ios/localize.command), the bash scripts that simplify work with LocoLaser on Unix based systems.

### Console arguments
You can override config properties by adding additional console arguments:
- **`--force`** or **`--f`** - Sets `force_import = true`.
- **`-cs`** - String. Override config property `conflict_strategy`.
- **`-delay`** - Long. Override config property `delay`.

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
