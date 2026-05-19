package ru.pocketbyte.locolaser.kotlinmpp.builder

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.config.resources.filter.RegExResourcesFilter
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFiltersSet
import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder
import java.io.File

/**
 * Abstract base builder for Kotlin Multiplatform platform-specific strings repository configurations.
 *
 * Provides common configuration options — source set, sources directory, and string filter —
 * shared by all platform builders in [KotlinMultiplatformResourcesConfigBuilder].
 */
abstract class BaseKmpBuilder<
        ConfigType: BaseResourcesConfig,
        BuilderType: BaseResourcesConfigBuilder<ConfigType>>(
    private val builderFactory: ResourcesConfigBuilderFactory<ConfigType, BuilderType>,
) {

    /**
     * Name of the source set.
     * Used to determine the path to the sources directory when [sourcesDir] is not set.
     */
    abstract var sourceSet: String

    protected abstract fun getResourceName(
        mainBuilder: KotlinMultiplatformResourcesConfigBuilder
    ): String

    /**
     * Path to directory with source code.
     */
    var sourcesDir: String? = null

    /**
     * Filter function.
     * If defined, only strings that suit the filter will be added as Repository fields.
     */
    @Deprecated(
        "Lambda function for filter is deprecated and will be replaced with ResourcesFilter" +
                " in future builds. Please, use filter(filter: ResourcesFilter?) or" +
                " filter(regExp: String) instead."
    )
    var filter: ((key: String) -> Boolean)? = null

    /**
     * Filter function.
     * If defined, only strings that suit the filter will be added as Repository fields.
     */
    var resourcesFilter: ResourcesFilter? = null
        set(value) {
            field = value
            filter = null
        }

    /**
     * Sets the filter for repository fields.
     * Only strings with keys that match the filter will be added as Repository fields.
     * @param filter Filter to apply, or `null` to clear the filter.
     */
    fun filter(filter: ResourcesFilter?) {
        this.resourcesFilter = filter
        this.filter = null
    }

    /**
     * Sets the filter for repository fields using a regular expression.
     * Only strings with keys that match the RegExp will be added as Repository fields.
     * @param regExp Regular expression string to match against string keys.
     */
    fun filter(regExp: String) {
        resourcesFilter = RegExResourcesFilter(regExp)
        this.filter = null
    }

    internal fun build(
        workDir: File?,
        mainBuilder: KotlinMultiplatformResourcesConfigBuilder,
        postProcessor: BuilderType.() -> Unit = {}
    ): ConfigType {
        return builderFactory
            .getBuilder()
            .apply {
                resourcesDir = defaultSourcesDir(mainBuilder.srcDir, this@BaseKmpBuilder)
                mainBuilder.filter?.let { filter = it }
                mainBuilder.resourcesFilter?.let { resourcesFilter = it }

                configure(this)

                resourceName = getResourceName(mainBuilder)

                postProcessor.invoke(this)
            }
            .build(workDir)
    }

    protected open fun configure(builder: BuilderType) {
        this.sourcesDir?.let {
            builder.resourcesDir = it
        }

        this.filter?.let {
            builder.filter = builder.filter?.let { parentFiler ->
                { key: String ->
                    parentFiler(key) && it(key)
                }
            } ?: it
        }

        this.resourcesFilter?.let { filter ->
            builder.resourcesFilter = builder.resourcesFilter?.let { builderFiler ->
                ResourcesFiltersSet(arrayOf(builderFiler, filter))
            } ?: filter
        }
    }

    protected fun mergeName(packageName: String, name: String): String {
        return if (name.contains(".")) { // is Canonical name
            name
        } else {
            "$packageName.$name"
        }
    }

    companion object {
        /**
         * Returns the default sources directory path for the given [builder] within [rootSrcDir].
         */
        fun defaultSourcesDir(rootSrcDir: String, builder: BaseKmpBuilder<*, *>): String {
            return "${rootSrcDir}/${builder.sourceSet}/kotlin/"
        }
    }
}