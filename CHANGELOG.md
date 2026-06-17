# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.7.0] - 2026-06-17

### Added
- Custom `FormattingType` support for Properties, Json and Ini resources ([#27](https://github.com/PocketByte/LocoLaser/issues/27))
- Consumer Project feature for Playground module
- Resource filtering support in read operations

### Deprecated
- `Config.file` and `ConfigBuilder.file` — obsolete, no longer in use (compile error)
- Kotlin MPP DSL extension functions; prefer `kotlinMultiplatform { }` block instead:
  - `kotlinCommon {}` — prefer `kotlinMultiplatform.common`
  - `kotlinAndroid {}` — prefer `kotlinMultiplatform.android`
  - `kotlinIos {}` — prefer `kotlinMultiplatform.ios`
  - `kotlinJs {}` — prefer `kotlinMultiplatform.js`
  - `kotlinAbsKeyValue {}` — prefer `kotlinMultiplatform.absKeyValue`
  - `kotlinAbsStatic {}` — prefer `kotlinMultiplatform.absStatic`
  - `kotlinAbsProxy {}` — prefer `kotlinMultiplatform.absProxy`
- Kotlin MPP config builder classes; prefer `KotlinMultiplatformResourcesConfigBuilder` instead:
  `KotlinCommonResourcesConfigBuilder`, `KotlinAndroidResourcesConfigBuilder`,
  `KotlinIosResourcesConfigBuilder`, `KotlinJsResourcesConfigBuilder`,
  `KotlinAbsKeyValueResourcesConfigBuilder`, `KotlinAbsStaticResourcesConfigBuilder`,
  `KotlinAbsProxyResourcesConfigBuilder`

### Fixed
- Properties multiline value parsing ([#28](https://github.com/PocketByte/LocoLaser/issues/28))
- Properties source: apply `formattingType` on write
- Dependabot security alerts
- Miscellaneous small issue fixes
- yarn.lock

### Changed
- Migrate all dependencies to Gradle Version Catalog (TOML)
- Replace `buildSrc` with `build-logic` convention plugins
- Move modules build logic into precompiled script plugins
- Bump KotlinPoet to 2.3.0
- Improve KDoc documentation for all public entities

## [2.6.0] - 2026-03-13

### Changed
- Migrate publishing to `com.gradleup.nmcp` plugin
- Migrate `settings.gradle` to Kotlin DSL (`.kts`)
- Replace `tasks.create` with `tasks.register` throughout the build
- Upgrade Kotlin and AGP versions

### Fixed
- Use correct Java toolchain for the runtime module
- Fix `PropertiesResourceFile` issue

### Removed
- Dead code

## [2.5.0] - 2026-03-11

### Changed
- Refactor `Config` to implement `Serializable`
- Convert filter lambdas to named classes
- Add `@Suppress` annotation in generated KMP repository for iOS platform

## [2.4.3] - 2026-03-11

### Fixed
- Unicode handling in iOS strings mapping

## [2.4.2] - 2024-07-07

### Changed
- Refactor KMP iOS repository class generator ([#24](https://github.com/PocketByte/LocoLaser/issues/24))

## [2.4.1] - 2024-06-09

### Fixed
- `LocalizeTask` regression

### Removed
- `tempDir` property
- JSON-based configs

## [2.4.0] - 2024-06-04

### Added
- Gradle task caching support (`@CacheableTask` / up-to-date checks)

### Changed
- Improve Kotlin Multiplatform platform support

### Removed
- Config parsers and `Summary` class

## [2.3.2] - 2024-03-15

### Fixed
- `NoFormattingType` handling

## [2.3.1] - 2024-02-13

### Added
- `ResourceFileProvider` abstraction for pluggable file handling
- `JvmBundleStringProvider` string provider implementation
- `localizedStringWithFormat` and `stringWithFormat` helpers in KMP runtime
- Additional multiplatform publication targets
- Comments support in Android resource files
- Two Playground examples (Kotlin DSL and Groovy)

### Fixed
- Gradle plugin wiring issues
- iOS Simulator and Java version compatibility
- WorkDir resolution in builders and Groovy build scripts
- GoogleSheets tests
- Parsing corner cases

### Changed
- Config made immutable; `workDir` moved inside `ResourceConfig`
- `ConfigBuilder` work-dir refactoring
- Use `tasks.register` instead of `tasks.create`
- Project structure reorganisation
- Kotlin runtime module extracted into a dedicated subproject
- Kotlin MPP resource classes refactored (`AbsStatic`, nullability cleanup)
- `trimUnsupportedQuantities` config parameter added for plural handling
- Plurals support added for Properties resource
- Upgrade Kotlin to 1.6.10
- Migrate to new Google Sheets API
- Bump library dependency versions

## [2.2.0] - 2021-06-04

### Added
- Gradle plugin merged into the main repository

### Fixed
- Kotlin Multiplatform compilation

### Changed
- Migrate build scripts to Kotlin DSL
- Refactor plugin to use Gradle configuration DSL instead of JSON configs
- Restore fat JARs for distribution

[Unreleased]: https://github.com/PocketByte/LocoLaser/compare/2.6.0...HEAD
[2.6.0]: https://github.com/PocketByte/LocoLaser/compare/2.5.0...2.6.0
[2.5.0]: https://github.com/PocketByte/LocoLaser/compare/2.4.3...2.5.0
[2.4.3]: https://github.com/PocketByte/LocoLaser/compare/2.4.2...2.4.3
[2.4.2]: https://github.com/PocketByte/LocoLaser/compare/2.4.1...2.4.2
[2.4.1]: https://github.com/PocketByte/LocoLaser/compare/2.4.0...2.4.1
[2.4.0]: https://github.com/PocketByte/LocoLaser/compare/2.3.2...2.4.0
[2.3.2]: https://github.com/PocketByte/LocoLaser/compare/2.3.1...2.3.2
[2.3.1]: https://github.com/PocketByte/LocoLaser/compare/2.2.0...2.3.1
[2.2.0]: https://github.com/PocketByte/LocoLaser/releases/tag/2.2.0
