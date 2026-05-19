package ru.pocketbyte.locolaser.config.resources

/**
 * Factory interface for creating [ResourcesConfigBuilder] instances.
 *
 * @param ConfigType The type of [ResourcesConfig] the builder produces.
 * @param BuilderType The type of [ResourcesConfigBuilder] this factory creates.
 */
interface ResourcesConfigBuilderFactory<
    out ConfigType: ResourcesConfig,
    out BuilderType : ResourcesConfigBuilder<ConfigType>
> {
    /**
     * Creates and returns a new [BuilderType] instance.
     */
    fun getBuilder() : BuilderType
}
