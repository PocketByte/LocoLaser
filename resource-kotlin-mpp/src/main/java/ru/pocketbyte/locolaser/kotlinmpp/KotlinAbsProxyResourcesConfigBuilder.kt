package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class KotlinAbsProxyResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinAbsProxyResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinAbsProxyResourcesConfig {
        return KotlinAbsProxyResourcesConfig(
            resourceName, resourcesDir, implements, filter
        )
    }
}