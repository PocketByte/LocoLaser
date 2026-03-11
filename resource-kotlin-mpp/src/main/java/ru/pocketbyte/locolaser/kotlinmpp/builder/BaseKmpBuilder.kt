package ru.pocketbyte.locolaser.kotlinmpp.builder

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.config.resources.filter.RegExResourcesFilter
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFiltersSet
import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder
import java.io.File

abstract class BaseKmpBuilder<
        ConfigType: BaseResourcesConfig,
        BuilderType: BaseResourcesConfigBuilder<ConfigType>>(
    private val builderFactory: ResourcesConfigBuilderFactory<ConfigType, BuilderType>,
) {

    /**
     * Name of source set.
     * Will be used to get path to sources directory if sourcesDir is null.
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
     * If defined, only strings that suits the filter will be added as Repository fields.
     */
    @Deprecated(
        "Lambda function for filter is deprecated and will be replaced with ResourcesFilter" +
                " in future builds. Please, use filter(filter: ResourcesFilter?) or" +
                " filter(regExp: String) instead."
    )
    var filter: ((key: String) -> Boolean)? = null

    /**
     * Filter function.
     * If defined, only strings that suits the filter will be added as Repository fields.
     */
    var resourcesFilter: ResourcesFilter? = null
        set(value) {
            field = value
            filter = null
        }

    /**
     * If defined, only strings with keys that matches filter will be added as Repository fields.
     * @param filter Only strings with keys that matches filter will be added as Repository fields.
     */
    fun filter(filter: ResourcesFilter?) {
        this.resourcesFilter = filter
        this.filter = null
    }

    /**
     * If defined, only strings with keys that matches RegExp will be added as Repository fields.
     * @param regExp RegExp String. Only strings with keys that matches RegExp will be added as Repository fields.
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
        fun defaultSourcesDir(rootSrcDir: String, builder: BaseKmpBuilder<*, *>): String {
            return "${rootSrcDir}/${builder.sourceSet}/kotlin/"
        }
    }
}