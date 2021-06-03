package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import java.io.File

class KotlinMultiplatformResourcesConfigBuilder {

    class PlatformBuilder {
        var className: String? = null
        var sourcesDir: File? = null
        var filter: ((key: String) -> Boolean)? = null
    }

    var repositoryInterface: String? = null
    var repositoryClass: String? = null
    var srcDir: File? = null
    var filter: ((key: String) -> Boolean)? = null

    fun srcDir(path: String) {
        srcDir = File(path)
    }

    private var commonPlatform: PlatformBuilder? = null
    private var androidPlatform: PlatformBuilder? = null
    private var iosPlatform: PlatformBuilder? = null
    private var jsPlatform: PlatformBuilder? = null

    fun common(action: PlatformBuilder.() -> Unit = {}) {
        commonPlatform = PlatformBuilder().apply {
            action(this)
        }
    }

    fun android(action: PlatformBuilder.() -> Unit = {}) {
        androidPlatform = PlatformBuilder().apply {
            action(this)
        }
    }

    fun ios(action: PlatformBuilder.() -> Unit = {}) {
        iosPlatform = PlatformBuilder().apply {
            action(this)
        }
    }

    fun js(action: PlatformBuilder.() -> Unit = {}) {
        jsPlatform = PlatformBuilder().apply {
            action(this)
        }
    }

    internal fun toResourcesConfig(): ResourcesConfig {
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

    private fun BaseResourcesConfig.fillFrom(platformBuilder: PlatformBuilder) {
        platformBuilder.className?.let { resourceName = it }
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