package ru.pocketbyte.locolaser.kotlinmpp

import groovy.lang.Closure
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig.Companion.DEFAULT_INTERFACE_NAME
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig.Companion.DEFAULT_PACKAGE
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.utils.callWithDelegate
import ru.pocketbyte.locolaser.utils.firstCharToUpperCase

class KotlinMultiplatformResourcesConfigBuilder : ResourcesConfigBuilder<ResourcesConfig> {

    abstract class BaseKmpBuilder {

        /**
         * Name of source set.
         * Will be used to get path to sources directory if sourcesDir is null.
         */
        abstract var sourceSet: String

        /**
         * Path to directory with source code.
         */
        var sourcesDir: String? = null

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
         * Package of the Repository that should be used in Interface name.
         * Package will be ignored if Interface name contains Canonical name.
         */
        var interfacePackage: String? = null

        /**
         * Canonical or Simple name of the Repository interface that should be generated.
         */
        var interfaceName: String? = null
    }

    class KmpClassBuilder<T : KotlinBaseImplResourcesConfig>(
        internal val name: String,
        internal val config: T
    ): BaseKmpBuilder() {

        override var sourceSet: String = "${name}Main"

        /**
         * Package of the Repository that should be used in class name.
         * Package will be ignored if class name contains canonical name.
         */
        var classPackage: String? = null

        /**
         * Canonical or Simple name of the Repository class that should be generated.
         */
        var className: String? = null
    }

    /**
     * Package of the Repository that should be used in interface and class names.
     * Package will be ignored if interface name or class name contains canonical name.
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
     * Source dir path.
     */
    var srcDir: String? = null

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
    fun common(action: KmpInterfaceBuilder.() -> Unit) {
        action.invoke(platformCommon)
    }

    /**
     * Configure Repository implementation for Android platform.
     * There is no Android implementation will be generated if Android platform wasn't be configured.
     */
    fun android() {
        android(null)
    }

    /**
     * Configure Repository implementation for Android platform.
     * There is no Android implementation will be generated if Android platform wasn't be configured.
     */
    fun android(action: (KmpClassBuilder<KotlinAndroidResourcesConfig>.() -> Unit)?) {
        platform("android", { KotlinAndroidResourcesConfig() }, action)
    }

    /**
     * Configure Repository implementation for iOS platform.
     * There is no iOS implementation will be generated if iOS platform wasn't be configured.
     */
    fun ios() {
        ios(null)
    }

    /**
     * Configure Repository implementation for iOS platform.
     * There is no iOS implementation will be generated if iOS platform wasn't be configured.
     */
    fun ios(action: (KmpClassBuilder<KotlinIosResourcesConfig>.() -> Unit)?) {
        platform("ios", { KotlinIosResourcesConfig() }, action)
    }

    /**
     * Configure Repository implementation for JS platform.
     * There is no JS implementation will be generated if JS platform wasn't be configured.
     */
    fun js() {
        js(null)
    }

    /**
     * Configure Repository implementation for JS platform.
     * There is no JS implementation will be generated if JS platform wasn't be configured.
     */
    fun js(action: (KmpClassBuilder<KotlinJsResourcesConfig>.() -> Unit)?) {
        platform("js", { KotlinJsResourcesConfig() }, action)
    }

    /**
     * Configure abstract KeyValue Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absKeyValue(
        name: String,
        formattingType: FormattingType = JavaFormattingType,
        action: (KmpClassBuilder<KotlinAbsKeyValueResourcesConfig>.() -> Unit)?
    ) {
        platform(name, { KotlinAbsKeyValueResourcesConfig(formattingType) }, action)
    }

    /**
     * Configure abstract Static Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absStatic(
        name: String,
        formattingType: FormattingType = JavaFormattingType,
        action: (KmpClassBuilder<KotlinAbsStaticResourcesConfig>.() -> Unit)?
    ) {
        platform(name, { KotlinAbsStaticResourcesConfig(formattingType) }, action)
    }

    /**
     * Configure abstract Proxy Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absProxy(
        name: String,
        action: (KmpClassBuilder<KotlinAbsProxyResourcesConfig>.() -> Unit)?
    ) {
        platform(name, { KotlinAbsProxyResourcesConfig() }, action)
    }

    /**
     * Configure Repository implementation for provided platform type.
     * @param name Name of platform.
     * @param config Platform Configuration instance.
     * Class should not be abstract and should have a constructor without parameters.
     */
    fun <T : KotlinBaseImplResourcesConfig> platform (
        name: String, config: () -> T
    ) {
        platform(name, config, null)
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
        action: (KmpClassBuilder<T>.() -> Unit)?
    ) {
        val platformBuilder = platformMap[name]
            ?: KmpClassBuilder(name, config()).apply {
                sourceSet = "${name}Main"
                platformMap[name] = this
            }

        action?.invoke(platformBuilder as KmpClassBuilder<T>)
    }

    override fun build(): ResourcesConfig {
        val commonConfig = KotlinCommonResourcesConfig()

        repositoryInterface?.also { commonConfig.resourceName = it }
        srcDir?.also {
            commonConfig.resourcesDirPath ="$it/${platformCommon.sourceSet}/kotlin/"
        }
        filter?.also { commonConfig.filter = it }
        platformCommon.also { commonConfig.fillFrom(it) }

        val platformConfigs = platformMap.values.map { builder ->
            builder.config.also { config ->
                srcDir?.also { config.resourcesDirPath = "$it/${builder.sourceSet}/kotlin/" }
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

    /**
     * Configure Repository interface in common module.
     */
    fun common(action: Closure<Unit>) {
        common { action.callWithDelegate(this) }
    }

    /**
     * Configure Repository implementation for Android platform.
     * There is no Android implementation will be generated if Android platform wasn't be configured.
     */
    fun android(action: Closure<Unit>) {
        android { action.callWithDelegate(this) }
    }

    /**
     * Configure Repository implementation for iOS platform.
     * There is no iOS implementation will be generated if iOS platform wasn't be configured.
     */
    fun ios(action: Closure<Unit>) {
        ios { action.callWithDelegate(this) }
    }

    /**
     * Configure Repository implementation for JS platform.
     * There is no JS implementation will be generated if JS platform wasn't be configured.
     */
    fun js(action: Closure<Unit>) {
        js { action.callWithDelegate(this) }
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
        action: Closure<Unit>
    ) {
        platform(name, config) {
            action.callWithDelegate(this)
        }
    }

    private fun BaseResourcesConfig.fillFrom(platformBuilder: BaseKmpBuilder) {
        if (platformBuilder is KmpInterfaceBuilder) {
            resourceName = mergeName(
                platformBuilder.interfacePackage
                    ?: repositoryPackage
                    ?: DEFAULT_PACKAGE,
                platformBuilder.interfaceName
                    ?: repositoryInterface
                    ?: DEFAULT_INTERFACE_NAME
            )
        } else if (platformBuilder is KmpClassBuilder<*>) {
            resourceName = mergeName(
                platformBuilder.classPackage
                    ?: repositoryPackage
                    ?: DEFAULT_PACKAGE,
                platformBuilder.className
                    ?: repositoryClass
                    ?: getDefaultClassName(platformBuilder.name)
            )
        }
        platformBuilder.sourcesDir?.let { resourcesDirPath = it }
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

    private fun getDefaultClassName(platformName: String): String {
        return "${platformName.firstCharToUpperCase()}$DEFAULT_INTERFACE_NAME"
    }
}