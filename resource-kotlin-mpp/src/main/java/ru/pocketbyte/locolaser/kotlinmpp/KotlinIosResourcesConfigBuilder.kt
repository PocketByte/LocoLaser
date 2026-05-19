package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [KotlinIosResourcesConfig].
 *
 * Provides configuration options for the Kotlin Multiplatform iOS strings repository generator.
 */
class KotlinIosResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinIosResourcesConfig>() {

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
