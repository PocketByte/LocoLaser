package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [KotlinAbsProxyResourcesConfig].
 *
 * Provides configuration options for the abstract proxy Kotlin Multiplatform strings repository generator.
 *
 * The generated class implements the strings repository interface but delegates all string lookups
 * to another instance of the same interface via an abstract `stringRepository` property.
 * This is useful for wrapping an existing repository implementation — for example, to add logging,
 * swap the string source at runtime, or combine multiple providers.
 */
@Deprecated("Use KotlinMultiplatformResourcesConfigBuilder instead.", level = DeprecationLevel.WARNING)
class KotlinAbsProxyResourcesConfigBuilder
    : KotlinBaseResourcesConfigBuilder<KotlinAbsProxyResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): KotlinAbsProxyResourcesConfig {
        return KotlinAbsProxyResourcesConfig(
            workDir, resourceName, resourcesDir, implements, filter
        )
    }
}
