package ru.pocketbyte.locolaser.config

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import java.io.File

open class ConfigBuilder(
    private val config: Config
) {
    /**
     * Import doesn't execute without a need.
     * Define if import should be forced even if this is not necessary.
     * True if import should be forced, false otherwise.
     */
    var isForceImport: Boolean
        get() = config.isForceImport
        set(value) { config.isForceImport = value }

    /**
     * Strategy that should be used for merge conflicts.
     * @see [ru.pocketbyte.locolaser.config.Config.ConflictStrategy]
     */
    var conflictStrategy: Config.ConflictStrategy
        get() = config.conflictStrategy
        set(value) { config.conflictStrategy = value }

    /**
     * Define temporary directory.
     */
    var tempDir: File?
        get() = config.tempDir
        set(value) { config.tempDir = value }

    /**
     * Define temporary directory.
     */
    fun tempDir(path: String) {
        tempDir = File(path)
    }

    /**
     * Set of locales that should be handled by LocoLaser.
     * You can use [Resources.BASE_LOCALE] to specify base locale.
     */
    var locales: Set<String>
        get() = config.locales
        set(value) { config.locales = value }

    /**
     * Define time in milliseconds that define delay for next localization.
     * Localization will executed not more often the specified delay.
     * If force import switched on delay will be ignored.
     */
    var delay: Long
        get() = config.delay
        set(value) { config.delay = value }

    val extraParams
        get() = config.extraParams

    /**
     * Platform that contain logic of resource creation.
     */
    val platform: ConfigResourceBuilder = object : ConfigResourceBuilder() {
        override var config: ResourcesConfig?
            get() = this@ConfigBuilder.config.platform
            set(value) { this@ConfigBuilder.config.platform = value}
    }

    /**
     * Platform that contain logic of resource creation.
     */
    fun platform(action: ConfigResourceBuilder.() -> Unit) {
        action(platform)
    }

    /**
     * Source that contain resources.
     */
    val source: ConfigResourceBuilder = object : ConfigResourceBuilder(true) {
        override var config: ResourcesConfig?
            get() = this@ConfigBuilder.config.source
            set(value) { this@ConfigBuilder.config.source = value}
    }

    /**
     * Source that contain resources.
     */
    fun source(action: ConfigResourceBuilder.() -> Unit) {
        action(source)
    }

    /**
     * Define if comment should be written even if it equal resource value.
     * True if comment should be written even if it equal resource value, false otherwise.
     */
    var isDuplicateComments: Boolean
        get() = extraParams.duplicateComments
        set(isDuplicateComments) {
            extraParams.duplicateComments = isDuplicateComments
        }
}