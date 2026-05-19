package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.ResourcesSet

/**
 * A [ResourcesConfig] that aggregates multiple [ResourcesConfig] instances into a set.
 */
data class ResourcesSetConfig(
    /**
     * The set of [ResourcesConfig] instances in this aggregation.
     */
    val configs: Set<ResourcesConfig>,

    /**
     * The primary [ResourcesConfig] within the set, or null if none is designated.
     */
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
