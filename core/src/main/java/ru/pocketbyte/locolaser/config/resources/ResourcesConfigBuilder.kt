package ru.pocketbyte.locolaser.config.resources

interface ResourcesConfigBuilder<out T : ResourcesConfig> {
    fun build() : T
}