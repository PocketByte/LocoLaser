package ru.pocketbyte.locolaser.kotlinmpp

import groovy.lang.Closure
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.kotlinmpp.builder.BaseKmpClassBuilder
import ru.pocketbyte.locolaser.kotlinmpp.builder.CustomFormattingClassBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.builder.FixedFormattingClassBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.builder.KmpClassCustomFormattingBuilder
import ru.pocketbyte.locolaser.kotlinmpp.builder.KmpClassFixedFormattingBuilder
import ru.pocketbyte.locolaser.kotlinmpp.builder.KmpInterfaceBuilder
import ru.pocketbyte.locolaser.utils.callWithDelegate

class KotlinMultiplatformResourcesConfigBuilder : ResourcesConfigBuilder<ResourcesConfig> {

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

    private val platformCommon: KmpInterfaceBuilder = KmpInterfaceBuilder()
    private val platformMap = mutableMapOf<String, BaseKmpClassBuilder<*, *>>()

    /**
     * If defined, only strings with keys that matches RegExp will be added as Repository fields.
     * @param regExp RegExp String. Only strings with keys that matches RegExp will be added as Repository fields.
     */
    fun filter(regExp: String) {
        filter = BaseResourcesConfig.regExFilter(regExp)
    }

    /**
     * Configure Repository interface in common module.
     */
    fun common(action: KmpInterfaceBuilder.() -> Unit) {
        action.invoke(platformCommon)
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
    fun android() {
        android(null)
    }

    /**
     * Configure Repository implementation for Android platform.
     * There is no Android implementation will be generated if Android platform wasn't be configured.
     */
    fun android(action: (KmpClassFixedFormattingBuilder.() -> Unit)?) {
        platform("android", KotlinAndroidResourcesConfig, action ?: {})
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
    fun ios() {
        ios(null)
    }

    /**
     * Configure Repository implementation for iOS platform.
     * There is no iOS implementation will be generated if iOS platform wasn't be configured.
     */
    fun ios(action: (KmpClassFixedFormattingBuilder.() -> Unit)?) {
        platform("ios", KotlinIosResourcesConfig, action ?: {})
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
    fun js() {
        js(null)
    }

    /**
     * Configure Repository implementation for JS platform.
     * There is no JS implementation will be generated if JS platform wasn't be configured.
     */
    fun js(action: (KmpClassFixedFormattingBuilder.() -> Unit)?) {
        platform("js", KotlinJsResourcesConfig, action ?: {})
    }

    /**
     * Configure Repository implementation for JS platform.
     * There is no JS implementation will be generated if JS platform wasn't be configured.
     */
    fun js(action: Closure<Unit>) {
        js { action.callWithDelegate(this) }
    }

    /**
     * Configure abstract KeyValue Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absKeyValue(
        name: String,
        action: (KmpClassCustomFormattingBuilder.() -> Unit)?
    ) {
        platformWithFormat(name, KotlinAbsKeyValueResourcesConfig, action ?: {})
    }

    /**
     * Configure abstract KeyValue Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absKeyValue(
        name: String,
        action: Closure<Unit>
    ) {
        absKeyValue(name) { action.callWithDelegate(this) }
    }

    /**
     * Configure abstract Static Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absStatic(
        name: String,
        action: (KmpClassCustomFormattingBuilder.() -> Unit)?
    ) {
        platformWithFormat(name, KotlinAbsStaticResourcesConfig, action ?: {})
    }

    /**
     * Configure abstract Static Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absStatic(
        name: String,
        action: Closure<Unit>
    ) {
        absStatic(name) { action.callWithDelegate(this) }
    }

    /**
     * Configure abstract Proxy Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absProxy(
        name: String,
        action: (KmpClassFixedFormattingBuilder.() -> Unit)?
    ) {
        platform(name, KotlinAbsProxyResourcesConfig, action ?: {})
    }

    /**
     * Configure abstract Proxy Repository implementation for provided platform name.
     * This config generates abstract strings Repository implementation, that can be used in any target.
     */
    fun absProxy(
        name: String,
        action: Closure<Unit>
    ) {
        absProxy(name) { action.callWithDelegate(this) }
    }

    /**
     * Configure Repository implementation for provided platform type.
     * @param name Name of platform.
     * @param config Platform Configuration instance.
     * Class should not be abstract and should have a constructor without parameters.
     * @param action Configure action.
     */
    fun platform (
        name: String,
        builderFactory: FixedFormattingClassBuilderFactory,
        action: (KmpClassFixedFormattingBuilder.() -> Unit)
    ) {
        val platformBuilder = platformMap[name] as? KmpClassFixedFormattingBuilder
            ?: KmpClassFixedFormattingBuilder(name, builderFactory).apply {
                sourceSet = "${name}Main"
                platformMap[name] = this
            }

        action(platformBuilder)
    }

    /**
     * Configure Repository implementation for provided platform type.
     * @param name Name of platform.
     * @param config Platform Configuration instance.
     * Class should not be abstract and should have a constructor without parameters.
     * @param action Configure action.
     */
    fun platform (
        name: String,
        builderFactory: FixedFormattingClassBuilderFactory,
        action: Closure<Unit>
    ) {
        platform(name, builderFactory) {
            action.callWithDelegate(this)
        }
    }

    /**
     * Configure Repository implementation for provided platform type.
     * @param name Name of platform.
     * @param config Platform Configuration instance.
     * Class should not be abstract and should have a constructor without parameters.
     * @param action Configure action.
     */
    private fun platformWithFormat (
        name: String,
        builderFactory: CustomFormattingClassBuilderFactory,
        action: (KmpClassCustomFormattingBuilder.() -> Unit)
    ) {
        val platformBuilder = platformMap[name] as? KmpClassCustomFormattingBuilder
            ?: KmpClassCustomFormattingBuilder(name, builderFactory).apply {
                sourceSet = "${name}Main"
                platformMap[name] = this
            }

        action(platformBuilder)
    }

    override fun build(): ResourcesConfig {
        val resultSet = LinkedHashSet<ResourcesConfig>()

        val commonConfig = platformCommon.build(this)
        resultSet.add(commonConfig)

        platformMap.values.forEach { builder ->
            builder.build(this) {
                this.implements = commonConfig.resourceName
            }.let {
                resultSet.add(it)
            }
        }

        return ResourcesSetConfig(resultSet)
    }
}