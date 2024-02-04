package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class KotlinAbsProxyResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinAbsProxyResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinAbsProxyResourcesConfig {
        return KotlinAbsProxyResourcesConfig(
            workDir, resourceName, resourcesDir, implements, filter
        )
    }
}