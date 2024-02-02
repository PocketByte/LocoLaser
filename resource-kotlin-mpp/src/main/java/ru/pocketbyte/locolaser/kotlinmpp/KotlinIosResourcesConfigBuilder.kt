package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class KotlinIosResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinIosResourcesConfig>(

) {
    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinIosResourcesConfig {
        return KotlinIosResourcesConfig(
            resourceName, resourcesDir, implements, filter
        )
    }
}