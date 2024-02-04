package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class KotlinAndroidResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinAndroidResourcesConfig>() {
    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinAndroidResourcesConfig {
        return KotlinAndroidResourcesConfig(
            workDir, resourceName, resourcesDir, implements, filter
        )
    }
}