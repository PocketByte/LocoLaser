package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class KotlinJsResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinJsResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinJsResourcesConfig {
        return KotlinJsResourcesConfig(
            workDir, resourceName, resourcesDir, implements, filter
        )
    }
}