package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import java.io.File

class KotlinMultiplatformResourcesConfigBuilder {

    open class BaseKmpBuilder {
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
        /**
         * Canonical name of the Repository interface that should be generated.
         */
        var interfaceName: String? = null
    }

    class KmpClassBuilder: BaseKmpBuilder() {
        /**
         * Canonical name of the Repository class that should be generated.
         */
        var className: String? = null
    }

    /**
     * Canonical name of the Repository interface that should be implemented by generated classes.
     * If empty there will no interfaces implemented by generated Repository classes.
     */
    var repositoryInterface: String? = null

    /**
     * Canonical name of the Repository class that should be generated for each platform.
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

    private var commonPlatform: KmpInterfaceBuilder? = null
    private var androidPlatform: KmpClassBuilder? = null
    private var iosPlatform: KmpClassBuilder? = null
    private var jsPlatform: KmpClassBuilder? = null

    /**
     * Configure Repository interface in common module.
     */
    fun common(action: KmpInterfaceBuilder.() -> Unit = {}) {
        commonPlatform = KmpInterfaceBuilder().apply {
            action(this)
        }
    }

    /**
     * Configure Repository implementation for Android platform.
     * There is no Android implementation will be generated if Android platform wasn't be configured.
     */
    fun android(action: KmpClassBuilder.() -> Unit = {}) {
        androidPlatform = KmpClassBuilder().apply {
            action(this)
        }
    }

    /**
     * Configure Repository implementation for iOS platform.
     * There is no iOS implementation will be generated if iOS platform wasn't be configured.
     */
    fun ios(action: KmpClassBuilder.() -> Unit = {}) {
        iosPlatform = KmpClassBuilder().apply {
            action(this)
        }
    }

    /**
     * Configure Repository implementation for JS platform.
     * There is no JS implementation will be generated if JS platform wasn't be configured.
     */
    fun js(action: KmpClassBuilder.() -> Unit = {}) {
        jsPlatform = KmpClassBuilder().apply {
            action(this)
        }
    }

    internal fun build(): ResourcesConfig {
        val commonConfig = KotlinCommonResourcesConfig()
        val androidConfig = if (androidPlatform != null) KotlinAndroidResourcesConfig() else null
        val iosConfig = if (iosPlatform != null) KotlinIosResourcesConfig() else null
        val jsConfig = if (jsPlatform != null) KotlinJsResourcesConfig() else null

        repositoryInterface?.also {
            commonConfig.resourceName = it
        }

        repositoryClass?.also { className ->
            val applyAction: (it: KotlinBaseImplResourcesConfig) -> Unit = {
                it.resourceName = className
            }
            androidConfig?.also { applyAction(it) }
            iosConfig?.also { applyAction(it) }
            jsConfig?.also { applyAction(it) }
        }
        srcDir?.also {
            commonConfig.resourcesDir = File(it, "./commonMain/kotlin/")
            androidConfig?.resourcesDir = File(it, "./androidMain/kotlin/")
            iosConfig?.resourcesDir = File(it, "./iosMain/kotlin/")
            jsConfig?.resourcesDir = File(it, "./jsMain/kotlin/")
        }
        filter?.also {
            commonConfig.filter = it
            androidConfig?.filter = it
            iosConfig?.filter = it
            jsConfig?.filter = it
        }

        commonPlatform?.also {
            commonConfig.fillFrom(it)
        }
        androidPlatform?.also {
            androidConfig?.fillFrom(it)
            androidConfig?.implements = commonConfig.resourceName
        }
        iosPlatform?.also {
            iosConfig?.fillFrom(it)
            iosConfig?.implements = commonConfig.resourceName
        }
        jsPlatform?.also {
            jsConfig?.fillFrom(it)
            jsConfig?.implements = commonConfig.resourceName
        }

        return ResourcesSetConfig(
            LinkedHashSet<ResourcesConfig>().apply {
                add(commonConfig)
                androidConfig?.also { add(it) }
                iosConfig?.also { add(it) }
                jsConfig?.also { add(it) }
            }
        )
    }

    private fun BaseResourcesConfig.fillFrom(platformBuilder: BaseKmpBuilder) {
        if (platformBuilder is KmpInterfaceBuilder) {
            platformBuilder.interfaceName?.let { resourceName = it }
        } else if (platformBuilder is KmpClassBuilder) {
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
}