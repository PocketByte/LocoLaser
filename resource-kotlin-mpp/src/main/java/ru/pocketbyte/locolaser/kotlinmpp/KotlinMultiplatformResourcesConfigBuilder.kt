package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig.Companion.DEFAULT_INTERFACE_NAME
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig.Companion.DEFAULT_PACKAGE
import ru.pocketbyte.locolaser.utils.firstCharToUpperCase
import java.io.File

class KotlinMultiplatformResourcesConfigBuilder {

    abstract class BaseKmpBuilder {

        /**
         * Name of source set.
         * Will be used to get path to sources directory if sourcesDir is null.
         */
        abstract var sourceSet: String

        /**
         * Path to directory with source code.
         */
        var sourcesDir: File? = null

        /**
         * Filter function.
         * If defined, only strings that suits the filter will be added as Repository fields.
         */
        var filter: ((key: String) -> Boolean)? = null

        /**
         * If defined, only strings with keys that matches RegExp will be added as Repository fields.
         * @param regExp RegExp String. Only strings with keys that matches RegExp will be added as Repository fields.
         */
        fun filter(regExp: String) {
            filter = BaseResourcesConfig.regExFilter(regExp)
        }
    }

    class KmpInterfaceBuilder: BaseKmpBuilder() {

        override var sourceSet: String = "commonMain"

        /**
         * Canonical name of the Repository interface that should be generated.
         */
        var interfaceName: String? = null
    }

    class KmpClassBuilder<T : KotlinBaseImplResourcesConfig>(
        internal val name: String,
        internal val config: T
    ): BaseKmpBuilder() {

        override var sourceSet: String = "${name}Main"

        /**
         * Canonical name of the Repository class that should be generated.
         */
        var className: String? = null
    }

    /**
     * Package of the Repository that should be used in interface and class names.
     * Package can be overridden if Interface name or Class name contains Canonical name.
     */
    var repositoryPackage: String? = null

    /**
     * Canonical or Simple name of the Repository interface that
     * should be implemented by generated classes.
     * If empty there will no interfaces implemented by generated Repository classes.
     */
    var repositoryInterface: String? = null

    /**
     * Canonical or Simple name of the Repository class that
     * should be generated for each platform.
     */
    var repositoryClass: String? = null

    /**
     * Path to src directory.
     */
    var srcDir: File? = null

    /**
     * Path to src directory.
     */
    fun srcDir(path: String) {
        srcDir = File(path)
    }

    /**
     * Filter function.
     * If defined, only strings that suits the filter will be added as Repository fields.
     */
    var filter: ((key: String) -> Boolean)? = null

    /**
     * If defined, only strings with keys that matches RegExp will be added as Repository fields.
     * @param regExp RegExp String. Only strings with keys that matches RegExp will be added as Repository fields.
     */
    fun filter(regExp: String) {
        filter = BaseResourcesConfig.regExFilter(regExp)
    }

    private val platformCommon: KmpInterfaceBuilder = KmpInterfaceBuilder()
    private val platformMap = mutableMapOf<String, KmpClassBuilder<*>>()

    /**
     * Configure Repository interface in common module.
     */
    fun common(action: KmpInterfaceBuilder.() -> Unit = {}) {
        platformCommon.apply {
            action(this)
        }
    }

    /**
     * Configure Repository implementation for Android platform.
     * There is no Android implementation will be generated if Android platform wasn't be configured.
     */
    fun android(action: KmpClassBuilder<KotlinAndroidResourcesConfig>.() -> Unit = {}) {
        platform("android", { KotlinAndroidResourcesConfig() }, action)
    }

    /**
     * Configure Repository implementation for iOS platform.
     * There is no iOS implementation will be generated if iOS platform wasn't be configured.
     */
    fun ios(action: KmpClassBuilder<KotlinIosResourcesConfig>.() -> Unit = {}) {
        platform("ios", { KotlinIosResourcesConfig() }, action)
    }

    /**
     * Configure Repository implementation for JS platform.
     * There is no JS implementation will be generated if JS platform wasn't be configured.
     */
    fun js(action: KmpClassBuilder<KotlinJsResourcesConfig>.() -> Unit = {}) {
        platform("js", { KotlinJsResourcesConfig() }, action)
    }

    /**
     * Configure Repository implementation for provided platform type.
     * @param name Name of platform.
     * @param config Platform Configuration instance.
     * Class should not be abstract and should have a constructor without parameters.
     * @param action Configure action.
     */
    fun <T : KotlinBaseImplResourcesConfig> platform (
        name: String, config: () -> T,
        action: KmpClassBuilder<T>.() -> Unit = {}
    ) {
        val platformBuilder = platformMap[name]
            ?: KmpClassBuilder(name, config()).apply {
                sourceSet = "${name}Main"
                platformMap[name] = this
            }

        action(platformBuilder as KmpClassBuilder<T>)
    }

    internal fun build(): ResourcesConfig {
        val commonConfig = KotlinCommonResourcesConfig()

        val packageName = repositoryPackage ?: DEFAULT_PACKAGE

        commonConfig.resourceName = mergeName(packageName, repositoryInterface ?: DEFAULT_INTERFACE_NAME)
        repositoryInterface?.also { commonConfig.resourceName = it }
        srcDir?.also {
            commonConfig.resourcesDir = File(it, "./${platformCommon.sourceSet}/kotlin/")
        }
        filter?.also { commonConfig.filter = it }
        platformCommon.also { commonConfig.fillFrom(it) }

        val platformConfigs = platformMap.values.map { builder ->
            builder.config.also { config ->
                config.resourceName = mergeName(
                    packageName,
                    repositoryClass ?: "${builder.name.firstCharToUpperCase()}$DEFAULT_INTERFACE_NAME"
                )
                srcDir?.also { config.resourcesDir = File(it, "./${builder.sourceSet}/kotlin/") }
                filter?.also { config.filter = it }
                config.fillFrom(builder)
                config.implements = commonConfig.resourceName
            }
        }

        return ResourcesSetConfig(
            LinkedHashSet<ResourcesConfig>().apply {
                add(commonConfig)
                addAll(platformConfigs)
            }
        )
    }

    private fun BaseResourcesConfig.fillFrom(platformBuilder: BaseKmpBuilder) {
        if (platformBuilder is KmpInterfaceBuilder) {
            platformBuilder.interfaceName?.let { resourceName = it }
        } else if (platformBuilder is KmpClassBuilder<*>) {
            platformBuilder.className?.let { resourceName = it }
        }
        platformBuilder.sourcesDir?.let { resourcesDir = it }
        platformBuilder.filter?.let {
            val parentFiler = filter
            filter = if (parentFiler == null) {
                it
            } else {
                { key: String ->
                    parentFiler(key) && it(key)
                }
            }
        }
    }

    private fun mergeName(packageName: String, name: String): String {
        return if (name.contains(".")) { // is Canonical name
            name
        } else {
            "$packageName.$name"
        }
    }
}