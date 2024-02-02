package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

class KotlinCommonResourcesConfigBuilder
    : BaseResourcesConfigBuilder<KotlinCommonResourcesConfig>() {

    override fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?
    ): KotlinCommonResourcesConfig {
        return KotlinCommonResourcesConfig(
            resourceName, resourcesDir, filter
        )
    }

    /**
     * Canonical name of the Repository interface that should be should be generated.
     */
    override var resourceName: String? = null

    /**
     * Path to directory with source code.
     */
    override var resourcesDir: String? = null

    /**
     * ResourceFileProvider provides resource File depending on locale, directory and name.
     */
    override var resourceFileProvider: ResourceFileProvider?
        get() = null
        set(value) {
            throw UnsupportedOperationException(
                "Changing of resourceFileProvider is not supported for Kotlin Class based resources"
            )
        }
}