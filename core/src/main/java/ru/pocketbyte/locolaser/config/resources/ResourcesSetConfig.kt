package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.ResourcesSet

import java.io.File
import java.util.LinkedHashSet

class ResourcesSetConfig(
    val configs: Set<ResourcesConfig>,
    val main: ResourcesConfig? = null
) : Config.Child(), ResourcesConfig {

    override val type: String = "set"

    override var parent: Config?
        get() = super.parent
        set(value) {
            super.parent = value
            configs.forEach { (it as? Config.Child)?.parent = value }
        }

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

    override val defaultTempDirPath: String
        get() = configs.first().defaultTempDirPath
}
