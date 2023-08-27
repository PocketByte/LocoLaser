package ru.pocketbyte.locolaser.config

import groovy.lang.Closure
import ru.pocketbyte.locolaser.utils.callWithDelegate
import java.io.File

open class ConfigBuilder {

    var workDir: File? = null

    var file: File? = null

    /**
     * Import doesn't execute without a need.
     * Defines if import should be forced even if this is not necessary.
     * True if import should be forced, false otherwise.
     */
    var forceImport: Boolean = false

    /**
     * Strategy that should be used for merge conflicts.
     * @see [ru.pocketbyte.locolaser.config.Config.ConflictStrategy]
     */
    var conflictStrategy: Config.ConflictStrategy = Config.DEFAULT_CONFLICT_STRATEGY

    /**
     * Defines temporary directory.
     */
    var tempDir: String? = null

    /**
     * Set of locales that should be handled by LocoLaser.
     * You can use [ru.pocketbyte.locolaser.resource.Resources.BASE_LOCALE] to specify base locale.
     */
    var locales: Set<String> = Config.DEFAULT_LOCALES

    /**
     * Defines time in minutes that define delay for next localization.
     * Localization will executed not more often the specified delay.
     * If force import switched on delay will be ignored.
     */
    var delay: Long = Config.DEFAULT_DELAY

    val extraParams: ExtraParams = ExtraParams()

    /**
     * Platform that contain logic of resource creation.
     */
    val platform: ResourcesSetConfigBuilder = ResourcesSetConfigBuilder()

    /**
     * Platform that contain logic of resource creation.
     */
    fun platform(action: ResourcesSetConfigBuilder.() -> Unit) {
        action.invoke(platform)
    }

    /**
     * Platform that contain logic of resource creation.
     */
    fun platform(action: Closure<Unit>) {
        action.callWithDelegate(platform)
    }

    /**
     * Source that contain resources.
     */
    val source: ResourcesSetConfigBuilder = ResourcesSetConfigBuilder(true)

    /**
     * Source that contain resources.
     */
    fun source(action: ResourcesSetConfigBuilder.() -> Unit) {
        action.invoke(source)
    }

    /**
     * Source that contain resources.
     */
    fun source(action: Closure<Unit>) {
        action.callWithDelegate(source)
    }

    /**
     * Defines if comment should be written even if it equal resource value.
     * True if comment should be written even if it equal resource value, false otherwise.
     */
    var duplicateComments: Boolean
        get() = extraParams.duplicateComments
        set(isDuplicateComments) {
            extraParams.duplicateComments = isDuplicateComments
        }

    /**
     * Defines if unsupported quantities should be throw away if is not supported by locale.
     * True if unsupported quantities should be throw away, false otherwise.
     */
    var trimUnsupportedQuantities: Boolean
        get() = extraParams.trimUnsupportedQuantities
        set(value) {
            extraParams.trimUnsupportedQuantities = value
        }

    fun build(): Config {
        val config = Config(workDir)
        config.file = file
        config.forceImport = forceImport
        config.conflictStrategy = conflictStrategy
        config.tempDirPath = tempDir
        config.locales = locales
        config.delay = delay
        config.extraParams.putAll(extraParams)
        config.platform = platform.build()
        config.source = source.build()
        return config
    }
}