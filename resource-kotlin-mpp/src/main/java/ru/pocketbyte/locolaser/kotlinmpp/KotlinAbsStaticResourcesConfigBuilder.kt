package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class KotlinAbsStaticResourcesConfigBuilder
    : KotlinBaseCustomFormattingResourceConfigBuilder<KotlinAbsStaticResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinAbsStaticResourcesConfig {
        return KotlinAbsStaticResourcesConfig(
            resourceName, resourcesDir, implements, formattingType, filter
        )
    }

}