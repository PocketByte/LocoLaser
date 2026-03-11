package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.config.resources.filter.RegExResourcesFilter
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

abstract class BaseResourcesConfigBuilder<out T : BaseResourcesConfig> : ResourcesConfigBuilder<T> {

    protected abstract fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ) : T

    /**
     * Resource name or null if should be used default name.
     */
    open var resourceName: String? = null

    /**
     * Resource directory path or null if should be used default.
     */
    open var resourcesDir: String? = null

    /**
     * ResourceFileProvider provides resource File depending on locale, directory and name.
     */
    open var resourceFileProvider: ResourceFileProvider? = null

    /**
     * Filter function. If defined, only strings that suits the filter will be written into resource.
     */
    @Deprecated(
        "Lambda function for filter is deprecated and will be replaced with ResourcesFilter" +
                " in future builds. Please, use filter(filter: ResourcesFilter?) or" +
                " filter(regExp: String) instead."
    )
    var filter: ((key: String) -> Boolean)? = null

    var resourcesFilter: ResourcesFilter? = null
        set(value) {
            field = value
            filter = null
        }

    /**
     * If defined, only strings with keys that matches filter will be written into resource.
     * @param filter Only strings with keys that matches filter will be written into resource.
     */
    fun filter(filter: ResourcesFilter?) {
        this.resourcesFilter = filter
        this.filter = null
    }

    /**
     * If defined, only strings with keys that matches RegExp will be written into resource.
     * @param regExp RegExp String. Only strings with keys that matches RegExp will be written into resource.
     */
    fun filter(regExp: String) {
        resourcesFilter = RegExResourcesFilter(regExp)
        this.filter = null
    }

    final override fun build(workDir: File?): T {
        return buildConfig(
            workDir,
            resourceName, resourcesDir,
            resourceFileProvider,
            filter?.let { filter -> ResourcesFilter { filter(it) } } ?: resourcesFilter
        )
    }
}