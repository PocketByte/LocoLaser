package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.ResourcesSet

import java.io.File
import java.util.LinkedHashSet

class ResourcesSetConfig(
        private val configs: Set<ResourcesConfig>,
        private val main: ResourcesConfig? = null
) : Config.Child(), ResourcesConfig {

    override val type: String = "set"

    override val resources: ResourcesSet
        get() {
            var mainResource: Resources? = null
            val resourcesSet = LinkedHashSet<Resources>(configs.size)
            for (config in configs) {
                val resource = config.resources
                resourcesSet.add(resource)

                if (config === main)
                    mainResource = resource
            }
            return ResourcesSet(resourcesSet, mainResource)
        }

    override val defaultTempDir: File
        get() = configs.first().defaultTempDir
}
