package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class KotlinJsResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinJsResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinJsResourcesConfig {
        return KotlinJsResourcesConfig(
            resourceName, resourcesDir, implements, filter
        )
    }
}