package ru.pocketbyte.locolaser.config.resources

import java.io.File

interface ResourcesConfigBuilder<out T : ResourcesConfig> {
    fun build(workDir: File?) : T
}