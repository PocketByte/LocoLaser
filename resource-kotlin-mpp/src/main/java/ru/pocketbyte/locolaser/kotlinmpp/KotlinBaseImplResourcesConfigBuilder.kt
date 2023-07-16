package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import java.io.File

open class KotlinBaseImplResourcesConfigBuilder(
    private val config: KotlinBaseImplResourcesConfig
): BaseResourcesConfigBuilder(config) {

    /**
     * Canonical name of the Repository interface that should be implemented by generated class.
     * If empty there will no interfaces implemented by generated Repository class.
     */
    var implements: String?
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
    override var resourcesDir: File
        get() = super.resourcesDir
        set(value) { super.resourcesDir = value }

    /**
     * Sets Path to directory with source code.
     */
    override fun resourcesDir(path: String) {
        super.resourcesDir(path)
    }
}