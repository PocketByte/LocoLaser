package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [KotlinAbsStaticResourcesConfig].
 *
 * Provides configuration options for the abstract static Kotlin Multiplatform strings repository generator,
 * including the string formatting type.
 *
 * The generated class implements the strings repository interface and returns string values
 * as hardcoded static constants. Particularly useful for testing, as it eliminates the need
 * for a real string source and makes test output predictable.
 */
class KotlinAbsStaticResourcesConfigBuilder
    : KotlinBaseCustomFormattingResourceConfigBuilder<KotlinAbsStaticResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): KotlinAbsStaticResourcesConfig {
        return KotlinAbsStaticResourcesConfig(
            workDir, resourceName, resourcesDir, implements, formattingType, filter
        )
    }

}
