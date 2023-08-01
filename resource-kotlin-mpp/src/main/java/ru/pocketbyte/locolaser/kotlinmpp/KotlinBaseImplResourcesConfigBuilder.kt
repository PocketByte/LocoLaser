package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

open class KotlinBaseImplResourcesConfigBuilder<T : KotlinBaseImplResourcesConfig>(
    config: T
): BaseResourcesConfigBuilder<T>(config) {

    /**
     * Canonical name of the Repository interface that should be implemented by generated class.
     * If empty there will no interfaces implemented by generated Repository class.
     */
    var implements: String
        get() = config.implements
        set(value) { config.implements = value }

    /**
     * Canonical name of the Repository class that should be generated.
     */
    override var resourceName: String
        get() = super.resourceName
        set(value) { super.resourceName = value }

    /**
     * Path to directory with source code.
     */
    override var resourcesDir: String?
        get() = super.resourcesDir
        set(value) { super.resourcesDir = value }

}