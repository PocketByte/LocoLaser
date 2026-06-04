package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [KotlinAndroidResourcesConfig].
 *
 * Provides configuration options for the Kotlin Multiplatform Android strings repository generator.
 */
@Deprecated("Use KotlinMultiplatformResourcesConfigBuilder instead.", level = DeprecationLevel.WARNING)
class KotlinAndroidResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinAndroidResourcesConfig>() {
    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): KotlinAndroidResourcesConfig {
        return KotlinAndroidResourcesConfig(
            workDir, resourceName, resourcesDir, implements, filter
        )
    }
}
