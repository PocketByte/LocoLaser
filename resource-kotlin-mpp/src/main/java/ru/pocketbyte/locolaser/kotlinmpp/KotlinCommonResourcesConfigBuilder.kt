package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [KotlinCommonResourcesConfig].
 *
 * Provides configuration options for the Kotlin Multiplatform Common strings repository generator,
 * which produces a Kotlin interface with string accessor properties for use in shared (`commonMain`) code.
 */
@Deprecated("Use KotlinMultiplatformResourcesConfigBuilder instead.", level = DeprecationLevel.WARNING)
class KotlinCommonResourcesConfigBuilder
    : BaseResourcesConfigBuilder<KotlinCommonResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): KotlinCommonResourcesConfig {
        return KotlinCommonResourcesConfig(
            workDir, resourceName, resourcesDir, filter
        )
    }

    /**
     * Canonical name of the Repository interface that should be generated.
     */
    override var resourceName: String? = null

    /**
     * Path to directory with source code.
     */
    override var resourcesDir: String? = null

    /**
     * Always throws [UnsupportedOperationException]. Changing the resource file provider
     * is not supported for Kotlin class-based resources.
     */
    override var resourceFileProvider: ResourceFileProvider?
        get() = null
        set(value) {
            throw UnsupportedOperationException(
                "Changing of resourceFileProvider is not supported for Kotlin Class based resources"
            )
        }
}