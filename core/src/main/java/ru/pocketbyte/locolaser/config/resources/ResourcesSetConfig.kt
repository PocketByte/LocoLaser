package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.ResourcesSet

data class ResourcesSetConfig(
    val configs: Set<ResourcesConfig>,
    val main: ResourcesConfig? = null
) : ResourcesConfig {

    override val type: String = "set"

    override val resources: ResourcesSet by lazy {
        var mainResource: Resources? = null
        val resourcesSet = LinkedHashSet<Resources>(configs.size)
        for (config in configs) {
            val resource = config.resources
            resourcesSet.add(resource)

            if (config === main)
                mainResource = resource
        }
        ResourcesSet(resourcesSet, mainResource)
    }
}
