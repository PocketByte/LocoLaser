package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.config.resources.filter.RegExResourcesFilter
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Base implementation of [ResourcesConfigBuilder].
 */
abstract class BaseResourcesConfigBuilder<out T : BaseResourcesConfig> : ResourcesConfigBuilder<T> {

    protected abstract fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ) : T

    /**
     * Resource name, or null to use the default name.
     */
    open var resourceName: String? = null

    /**
     * Resource directory path, or null to use the default path.
     */
    open var resourcesDir: String? = null

    /**
     * Provides the resource [File] for a given locale, directory, and name.
     */
    open var resourceFileProvider: ResourceFileProvider? = null

    /**
     * Filter function. If defined, only strings that suit the filter will be written into resource.
     */
    @Deprecated(
        "Lambda function for filter is deprecated and will be replaced with ResourcesFilter" +
                " in future builds. Please, use filter(filter: ResourcesFilter?) or" +
                " filter(regExp: String) instead.",
        level = DeprecationLevel.WARNING
    )
    var filter: ((key: String) -> Boolean)? = null

    /**
     * Filter controlling which resource keys are written.
     */
    var resourcesFilter: ResourcesFilter? = null
        set(value) {
            field = value
            filter = null
        }

    /**
     * Sets the filter that controls which resource keys are written.
     * @param filter Only strings with keys that match the filter will be written into resource.
     */
    fun filter(filter: ResourcesFilter?) {
        this.resourcesFilter = filter
        this.filter = null
    }

    /**
     * Sets a regular expression filter that controls which resource keys are written.
     * @param regExp Regular expression. Only strings with keys that match this expression will be written into resource.
     */
    fun filter(regExp: String) {
        resourcesFilter = RegExResourcesFilter(regExp)
        this.filter = null
    }

    /**
     * Builds and returns a [BaseResourcesConfig] instance from the current builder state.
     */
    final override fun build(workDir: File?): T {
        return buildConfig(
            workDir,
            resourceName, resourcesDir,
            resourceFileProvider,
            filter?.let { filter -> ResourcesFilter { filter(it) } } ?: resourcesFilter
        )
    }
}