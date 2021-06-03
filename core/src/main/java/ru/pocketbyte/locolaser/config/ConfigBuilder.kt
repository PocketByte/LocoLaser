package ru.pocketbyte.locolaser.config

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig

class ConfigBuilder(
    private val config: Config
) {

    var isForceImport: Boolean
        get() = config.isForceImport
        set(value) { config.isForceImport = value }

    var conflictStrategy: Config.ConflictStrategy
        get() = config.conflictStrategy
        set(value) { config.conflictStrategy = value }

    var locales: Set<String>
        get() = config.locales
        set(value) { config.locales = value }

    var delay: Long
        get() = config.delay
        set(value) { config.delay = value }

    val extraParams
        get() = config.extraParams

    val platform: ConfigResourceBuilder = object : ConfigResourceBuilder() {
        override var config: ResourcesConfig?
            get() = this@ConfigBuilder.config.platform
            set(value) { this@ConfigBuilder.config.platform = value}
    }

    fun platform(action: ConfigResourceBuilder.() -> Unit) {
        action(platform)
    }

    val source: ConfigResourceBuilder = object : ConfigResourceBuilder(true) {
        override var config: ResourcesConfig?
            get() = this@ConfigBuilder.config.source
            set(value) { this@ConfigBuilder.config.source = value}
    }

    fun source(action: ConfigResourceBuilder.() -> Unit) {
        action(source)
    }

    var isDuplicateComments: Boolean
        get() = extraParams.duplicateComments
        set(isDuplicateComments) {
            extraParams.duplicateComments = isDuplicateComments
        }
}