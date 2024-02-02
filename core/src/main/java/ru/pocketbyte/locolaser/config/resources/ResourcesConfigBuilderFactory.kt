package ru.pocketbyte.locolaser.config.resources

interface ResourcesConfigBuilderFactory<
    out ConfigType: ResourcesConfig,
    out BuilderType : ResourcesConfigBuilder<ConfigType>
> {
    fun getBuilder() : BuilderType
}