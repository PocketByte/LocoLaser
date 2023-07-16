package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import java.io.File

class KotlinCommonResourcesConfigBuilder(
    config: KotlinCommonResourcesConfig
): BaseResourcesConfigBuilder(config) {

    /**
     * Canonical name of the Repository interface that should be should be generated.
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