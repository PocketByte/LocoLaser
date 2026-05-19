package ru.pocketbyte.locolaser.config.resources

import java.io.File

/**
 * Builder interface for constructing [ResourcesConfig] instances.
 */
interface ResourcesConfigBuilder<out T : ResourcesConfig> {

    /**
     * Builds and returns a [ResourcesConfig] instance, using [workDir] to resolve relative paths.
     */
    fun build(workDir: File?) : T
}
