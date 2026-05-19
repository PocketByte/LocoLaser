package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

/**
 * Builder for [KotlinAbsKeyValueResourcesConfig].
 *
 * Provides configuration options for the abstract key-value Kotlin Multiplatform
 * strings repository generator, including the string formatting type.
 */
class KotlinAbsKeyValueResourcesConfigBuilder
    : KotlinBaseCustomFormattingResourceConfigBuilder<KotlinAbsKeyValueResourcesConfig>() {

    override fun buildConfig(
        workDir: File?,
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ResourcesFilter?
    ): KotlinAbsKeyValueResourcesConfig {
        return KotlinAbsKeyValueResourcesConfig(
            workDir, resourceName, resourcesDir, implements, formattingType, filter
        )
    }
}
