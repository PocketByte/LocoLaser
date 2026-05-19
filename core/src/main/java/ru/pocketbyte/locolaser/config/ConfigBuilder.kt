package ru.pocketbyte.locolaser.config

import groovy.lang.Closure
import ru.pocketbyte.locolaser.utils.callWithDelegate
import java.io.File

/**
 * Builder for constructing a [Config] instance using a DSL-style API.
 */
open class ConfigBuilder {

    /**
     * Working directory used to resolve relative paths defined in the config.
     */
    var workDir: File? = null

    /**
     * File from which this config was read.
     */
    var file: File? = null

    /**
     * Strategy for resolving conflicts between platform and source resources during localization.
     * @see [ru.pocketbyte.locolaser.config.Config.ConflictStrategy]
     */
    var conflictStrategy: Config.ConflictStrategy = Config.DEFAULT_CONFLICT_STRATEGY

    /**
     * Set of locales that should be handled by LocoLaser.
     * You can use [ru.pocketbyte.locolaser.resource.Resources.BASE_LOCALE] to specify base locale.
     */
    var locales: Set<String> = Config.DEFAULT_LOCALES

    /**
     * Additional parameters passed to resources during read and write operations.
     */
    val extraParams: ExtraParams = ExtraParams()

    /**
     * The target platform to which localized resource files are written (e.g., Android, iOS).
     */
    val platform: ResourcesSetConfigBuilder = ResourcesSetConfigBuilder()

    /**
     * Configures the platform using the given action.
     */
    fun platform(action: ResourcesSetConfigBuilder.() -> Unit) {
        action.invoke(platform)
    }

    /**
     * Configures the platform using the given action.
     */
    fun platform(action: Closure<Unit>) {
        action.callWithDelegate(platform)
    }

    /**
     * The resource source from which localization data is read (e.g., a remote spreadsheet or file).
     */
    val source: ResourcesSetConfigBuilder = ResourcesSetConfigBuilder(true)

    /**
     * Determines whether the localization task should depend on compile tasks.
     */
    var isDependsOnCompileTasks: Boolean = false
        private set

    /**
     * Configures the source using the given action.
     */
    fun source(action: ResourcesSetConfigBuilder.() -> Unit) {
        action.invoke(source)
    }

    /**
     * Configures the source using the given action.
     */
    fun source(action: Closure<Unit>) {
        action.callWithDelegate(source)
    }

    /**
     * Enables the dependency of the localization task on compile tasks.
     */
    fun dependsOnCompileTasks() {
        isDependsOnCompileTasks = true
    }

    /**
     * Defines if a comment should be written even if it equals the resource value.
     */
    var duplicateComments: Boolean
        get() = extraParams.duplicateComments
        set(isDuplicateComments) {
            extraParams.duplicateComments = isDuplicateComments
        }

    /**
     * Defines if unsupported quantities should be thrown away if they are not supported by the locale.
     */
    var trimUnsupportedQuantities: Boolean
        get() = extraParams.trimUnsupportedQuantities
        set(value) {
            extraParams.trimUnsupportedQuantities = value
        }

    /**
     * Builds and returns a [Config] instance from the current builder state.
     */
    fun build(): Config {
        return Config(
            workDir = workDir,
            file = file,
            conflictStrategy = conflictStrategy,
            locales = locales,
            extraParams = ExtraParams().apply { putAll(extraParams) },
            platform = platform.build(workDir),
            source = source.build(workDir),
        )
    }
}
