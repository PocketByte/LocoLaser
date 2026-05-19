package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

/** Abstract builder for [KotlinBaseResourcesConfig] subclasses. */
abstract class KotlinBaseResourcesConfigBuilder<out T : KotlinBaseResourcesConfig>
    : BaseResourcesConfigBuilder<T>() {

    /**
     * Fully qualified name of the interface that the generated repository class will implement.
     * If `null` or empty, no interface will be implemented.
     */
    var implements: String? = null

    /**
     * Canonical name of the Repository class that should be generated.
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
