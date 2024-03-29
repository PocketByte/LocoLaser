package ru.pocketbyte.locolaser.config

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import java.io.File

open class ConfigBuilder(
    private val config: Config
) {
    /**
     * Import doesn't execute without a need.
     * Defines if import should be forced even if this is not necessary.
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
     * Defines temporary directory.
     */
    var tempDir: File?
        get() = config.tempDir
        set(value) { config.tempDir = value }

    /**
     * Defines temporary directory.
     */
    fun tempDir(path: String) {
        tempDir = File(path)
    }

    /**
     * Set of locales that should be handled by LocoLaser.
     * You can use [ru.pocketbyte.locolaser.resource.Resources.BASE_LOCALE] to specify base locale.
     */
    var locales: Set<String>
        get() = config.locales
        set(value) { config.locales = value }

    /**
     * Defines time in minutes that define delay for next localization.
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
     * Defines if comment should be written even if it equal resource value.
     * True if comment should be written even if it equal resource value, false otherwise.
     */
    var isDuplicateComments: Boolean
        get() = extraParams.duplicateComments
        set(isDuplicateComments) {
            extraParams.duplicateComments = isDuplicateComments
        }

    /**
     * Defines if unsupported quantities should be throw away if is not supported by locale.
     * True if unsupported quantities should be throw away, false otherwise.
     */
    var ExtraParams.trimUnsupportedQuantities: Boolean
        get() = extraParams.trimUnsupportedQuantities
        set(value) {
            extraParams.trimUnsupportedQuantities = value
        }
}