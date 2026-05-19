package ru.pocketbyte.locolaser.kotlinmpp

import groovy.lang.Closure
import org.gradle.api.Project
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.config.resources.filter.RegExResourcesFilter
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.kotlinmpp.builder.BaseKmpBuilder
import ru.pocketbyte.locolaser.kotlinmpp.builder.BaseKmpClassBuilder
import ru.pocketbyte.locolaser.kotlinmpp.builder.CustomFormattingClassBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.builder.FixedFormattingClassBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.builder.KmpClassCustomFormattingBuilder
import ru.pocketbyte.locolaser.kotlinmpp.builder.KmpClassFixedFormattingBuilder
import ru.pocketbyte.locolaser.kotlinmpp.builder.KmpInterfaceBuilder
import ru.pocketbyte.locolaser.kotlinmpp.extension.kotlin
import ru.pocketbyte.locolaser.utils.callWithDelegate
import java.io.File

/**
 * Builder for Kotlin Multiplatform localization resources configuration.
 *
 * Configures a strings repository interface for common code and its platform-specific implementations
 * (Android, iOS, JS, or custom targets). When a [Project] is provided, generated source directories
 * are automatically registered in the corresponding Kotlin source sets.
 */
class KotlinMultiplatformResourcesConfigBuilder(
    private val project: Project?
) : ResourcesConfigBuilder<ResourcesConfig> {

    /**
     * Package of the Repository that should be used in interface and class names.
     * Package will be ignored if interface name or class name contains a canonical name.
     * By default, the package is taken from `project.group` if it is provided.
     */
    var repositoryPackage: String? = null
        get() = if (field == null) {
            project?.group?.toString()
        } else {
            field
        }

    /**
     * Canonical or simple name of the Repository interface that
     * should be implemented by generated classes.
     * If `null` or empty, no interface will be implemented by generated Repository classes.
     */
    var repositoryInterface: String? = null

    /**
     * Canonical or simple name of the Repository class that
     * should be generated for each platform.
     */
    var repositoryClass: String? = null

    /**
     * Source dir path.
     */
    var srcDir: String = "./build/generated/locolaser/"

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

    private val platformCommon: KmpInterfaceBuilder = KmpInterfaceBuilder()
    private val platformMap = mutableMapOf<String, BaseKmpClassBuilder<*, *>>()

    init {
        project?.afterEvaluate {
            it.kotlin {
                sourceSets.apply {
                    commonMain {
                        val sourcesDir = platformCommon.sourcesDir
                            ?: BaseKmpBuilder.defaultSourcesDir(srcDir, platformCommon)

                        kotlin.srcDir(sourcesDir)
                    }

                    platformMap.values.forEach { builder ->
                        val sourcesDir = builder.sourcesDir
                            ?: BaseKmpBuilder.defaultSourcesDir(srcDir, builder)
                        val sourceSet = findByName(builder.sourceSet)
                            ?: throw IllegalArgumentException(
                                "Missing sourceSet `${builder.sourceSet}`"
                            )

                        sourceSet.kotlin.srcDir(sourcesDir)
                    }
                }
            }
        }
    }

    /**
     * If defined, only strings with keys that match the filter will be added as Repository fields.
     * @param filter Filter to apply. Only strings with matching keys will be added as Repository fields.
     */
    fun filter(filter: ResourcesFilter?) {
        this.resourcesFilter = filter
        this.filter = null
    }

    /**
     * If defined, only strings with keys that match the RegExp will be added as Repository fields.
     * @param regExp Regular expression string. Only strings with matching keys will be added as Repository fields.
     */
    fun filter(regExp: String) {
        resourcesFilter = RegExResourcesFilter(regExp)
        this.filter = null
    }

    /**
     * Configures generation of a strings repository interface for common (shared) code.
     * @param action Configuration block applied to [KmpInterfaceBuilder].
     */
    fun common(action: KmpInterfaceBuilder.() -> Unit) {
        action.invoke(platformCommon)
    }

    /**
     * Configures generation of a strings repository interface for common (shared) code.
     * @param action Configuration block applied to [KmpInterfaceBuilder].
     */
    fun common(action: Closure<Unit>) {
        common { action.callWithDelegate(this) }
    }

    /**
     * Configures generation of a strings repository implementation for the Android platform.
     * No Android implementation will be generated if the Android platform is not configured.
     */
    fun android() {
        android(null)
    }

    /**
     * Configures generation of a strings repository implementation for the Android platform.
     * No Android implementation will be generated if the Android platform is not configured.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
     */
    fun android(action: (KmpClassFixedFormattingBuilder.() -> Unit)?) {
        platform("android", KotlinAndroidResourcesConfig, action ?: {})
    }

    /**
     * Configures generation of a strings repository implementation for the Android platform.
     * No Android implementation will be generated if the Android platform is not configured.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
     */
    fun android(action: Closure<Unit>) {
        android { action.callWithDelegate(this) }
    }

    /**
     * Configures generation of a strings repository implementation for the iOS platform.
     * No iOS implementation will be generated if the iOS platform is not configured.
     */
    fun ios() {
        ios(null)
    }

    /**
     * Configures generation of a strings repository implementation for the iOS platform.
     * No iOS implementation will be generated if the iOS platform is not configured.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
     */
    fun ios(action: (KmpClassFixedFormattingBuilder.() -> Unit)?) {
        platform("ios", KotlinIosResourcesConfig, action ?: {})
    }

    /**
     * Configures generation of a strings repository implementation for the iOS platform.
     * No iOS implementation will be generated if the iOS platform is not configured.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
     */
    fun ios(action: Closure<Unit>) {
        ios { action.callWithDelegate(this) }
    }

    /**
     * Configures generation of a strings repository implementation for the JS platform.
     * No JS implementation will be generated if the JS platform is not configured.
     */
    fun js() {
        js(null)
    }

    /**
     * Configures generation of a strings repository implementation for the JS platform.
     * No JS implementation will be generated if the JS platform is not configured.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
     */
    fun js(action: (KmpClassFixedFormattingBuilder.() -> Unit)?) {
        platform("js", KotlinJsResourcesConfig, action ?: {})
    }

    /**
     * Configures generation of a strings repository implementation for the JS platform.
     * No JS implementation will be generated if the JS platform is not configured.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
     */
    fun js(action: Closure<Unit>) {
        js { action.callWithDelegate(this) }
    }

    /**
     * Configures generation of an abstract key-value strings repository implementation
     * for the provided platform name, usable in any target.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param action Configuration block applied to [KmpClassCustomFormattingBuilder].
     */
    fun absKeyValue(
        name: String,
        action: (KmpClassCustomFormattingBuilder.() -> Unit)?
    ) {
        platformWithFormat(name, KotlinAbsKeyValueResourcesConfig, action ?: {})
    }

    /**
     * Configures generation of an abstract key-value strings repository implementation
     * for the provided platform name, usable in any target.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param action Configuration block applied to [KmpClassCustomFormattingBuilder].
     */
    fun absKeyValue(
        name: String,
        action: Closure<Unit>
    ) {
        absKeyValue(name) { action.callWithDelegate(this) }
    }

    /**
     * Configures generation of an abstract static strings repository implementation
     * for the provided platform name, usable in any target.
     *
     * The generated class returns string values as hardcoded static constants,
     * which eliminates the need for a real string source and makes test output predictable.
     * Particularly useful for testing.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param action Configuration block applied to [KmpClassCustomFormattingBuilder].
     */
    fun absStatic(
        name: String,
        action: (KmpClassCustomFormattingBuilder.() -> Unit)?
    ) {
        platformWithFormat(name, KotlinAbsStaticResourcesConfig, action ?: {})
    }

    /**
     * Configures generation of an abstract static strings repository implementation
     * for the provided platform name, usable in any target.
     *
     * The generated class returns string values as hardcoded static constants,
     * which eliminates the need for a real string source and makes test output predictable.
     * Particularly useful for testing.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param action Configuration block applied to [KmpClassCustomFormattingBuilder].
     */
    fun absStatic(
        name: String,
        action: Closure<Unit>
    ) {
        absStatic(name) { action.callWithDelegate(this) }
    }

    /**
     * Configures generation of an abstract proxy strings repository implementation
     * for the provided platform name, usable in any target.
     *
     * The generated class delegates all string lookups to another instance of the same interface
     * via an abstract `stringRepository` property — useful for wrapping an existing repository
     * implementation, for example to add logging, swap the string source at runtime,
     * or combine multiple providers.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
     */
    fun absProxy(
        name: String,
        action: (KmpClassFixedFormattingBuilder.() -> Unit)?
    ) {
        platform(name, KotlinAbsProxyResourcesConfig, action ?: {})
    }

    /**
     * Configures generation of an abstract proxy strings repository implementation
     * for the provided platform name, usable in any target.
     *
     * The generated class delegates all string lookups to another instance of the same interface
     * via an abstract `stringRepository` property — useful for wrapping an existing repository
     * implementation, for example to add logging, swap the string source at runtime,
     * or combine multiple providers.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
     */
    fun absProxy(
        name: String,
        action: Closure<Unit>
    ) {
        absProxy(name) { action.callWithDelegate(this) }
    }

    /**
     * Configures generation of a strings repository implementation for the provided platform type.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param builderFactory Factory that provides the platform-specific resources config builder.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
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
     * Configures generation of a strings repository implementation for the provided platform type.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param builderFactory Factory that provides the platform-specific resources config builder.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
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
     * Configures generation of a strings repository implementation for the provided platform type.
     * @param name Name of the platform target (e.g. `"common"`, `"android"`, `"ios"`, `"js"`).
     * @param builderFactory Factory that provides the platform-specific resources config builder.
     * @param action Configuration block applied to [KmpClassFixedFormattingBuilder].
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

    override fun build(workDir: File?): ResourcesConfig {
        val resultSet = LinkedHashSet<ResourcesConfig>()

        val commonConfig = platformCommon.build(workDir, this)
        resultSet.add(commonConfig)

        platformMap.values.forEach { builder ->
            builder.build(workDir, this) {
                this.implements = commonConfig.resourceName
            }.let {
                resultSet.add(it)
            }
        }

        return ResourcesSetConfig(resultSet)
    }
}
