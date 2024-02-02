package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider

abstract class KotlinBaseResourcesConfigBuilder<out T : KotlinBaseResourcesConfig>
    : BaseResourcesConfigBuilder<T>() {

    /**
     * Canonical name of the Repository interface that should be implemented by generated class.
     * If empty there will no interfaces implemented by generated Repository class.
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