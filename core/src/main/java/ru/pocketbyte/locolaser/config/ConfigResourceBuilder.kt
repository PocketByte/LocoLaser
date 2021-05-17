package ru.pocketbyte.locolaser.config

import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig

abstract class ConfigResourceBuilder(
    val hasMainResource: Boolean = false
) {

    abstract var config: ResourcesConfig?

    private val resources = LinkedHashSet<ResourcesConfig>()
    private var innerConfig: ResourcesSetConfig? = null

    fun add(resourcesConfig: ResourcesConfig) {
        resources.add(resourcesConfig)
        if (resources.size == 1) {
            config = resourcesConfig
        } else if (config == null || config != innerConfig) {
            if (innerConfig == null) {
                innerConfig = ResourcesSetConfig(resources, if (hasMainResource) resourcesConfig else null)
            }
            config = innerConfig
        }
    }
}