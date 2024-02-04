package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class KotlinAbsStaticResourcesConfigBuilder
    : KotlinBaseCustomFormattingResourceConfigBuilder<KotlinAbsStaticResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinAbsStaticResourcesConfig {
        return KotlinAbsStaticResourcesConfig(
            workDir, resourceName, resourcesDir, implements, formattingType, filter
        )
    }

}