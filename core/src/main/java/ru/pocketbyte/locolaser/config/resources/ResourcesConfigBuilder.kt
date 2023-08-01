package ru.pocketbyte.locolaser.config.resources

interface ResourcesConfigBuilder<T : ResourcesConfig> {
    fun build() : T
}