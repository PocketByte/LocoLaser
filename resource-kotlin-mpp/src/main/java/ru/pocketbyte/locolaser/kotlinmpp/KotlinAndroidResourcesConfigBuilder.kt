package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class KotlinAndroidResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinAndroidResourcesConfig>() {
    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinAndroidResourcesConfig {
        return KotlinAndroidResourcesConfig(
            resourceName, resourcesDir, implements, filter
        )
    }
}