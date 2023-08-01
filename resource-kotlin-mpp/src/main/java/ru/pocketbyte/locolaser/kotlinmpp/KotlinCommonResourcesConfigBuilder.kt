package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

class KotlinCommonResourcesConfigBuilder
    : BaseResourcesConfigBuilder<KotlinCommonResourcesConfig>(KotlinCommonResourcesConfig()) {

    /**
     * Canonical name of the Repository interface that should be should be generated.
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