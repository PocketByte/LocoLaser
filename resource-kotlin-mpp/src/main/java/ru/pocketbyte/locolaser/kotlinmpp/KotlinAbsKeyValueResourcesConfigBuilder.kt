package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class KotlinAbsKeyValueResourcesConfigBuilder
    : KotlinBaseCustomFormattingResourceConfigBuilder<KotlinAbsKeyValueResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinAbsKeyValueResourcesConfig {
        return KotlinAbsKeyValueResourcesConfig(
            resourceName, resourcesDir, implements, formattingType, filter
        )
    }

}