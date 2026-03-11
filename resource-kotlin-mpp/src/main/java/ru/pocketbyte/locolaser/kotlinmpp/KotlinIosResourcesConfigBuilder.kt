package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

class KotlinIosResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinIosResourcesConfig>(

) {
    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): KotlinIosResourcesConfig {
        return KotlinIosResourcesConfig(
            workDir, resourceName, resourcesDir, implements, filter
        )
    }
}