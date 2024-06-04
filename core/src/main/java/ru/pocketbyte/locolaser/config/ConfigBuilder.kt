package ru.pocketbyte.locolaser.config

import groovy.lang.Closure
import ru.pocketbyte.locolaser.utils.callWithDelegate
import java.io.File

open class ConfigBuilder {

    var workDir: File? = null

    var file: File? = null

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

    var isDependsOnCompileTasks: Boolean = false
        private set

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

    fun dependsOnCompileTasks() {
        isDependsOnCompileTasks = true
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
        return Config(
            workDir = workDir,
            file = file,
            conflictStrategy = conflictStrategy,
            tempDirPath = tempDir,
            locales = locales,
            extraParams = ExtraParams().apply { putAll(extraParams) },
            platform = platform.build(workDir),
            source = source.build(workDir),
        )
    }
}
