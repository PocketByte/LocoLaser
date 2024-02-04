package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class KotlinAbsKeyValueResourcesConfigBuilder
    : KotlinBaseCustomFormattingResourceConfigBuilder<KotlinAbsKeyValueResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinAbsKeyValueResourcesConfig {
        return KotlinAbsKeyValueResourcesConfig(
            workDir, resourceName, resourcesDir, implements, formattingType, filter
        )
    }

}