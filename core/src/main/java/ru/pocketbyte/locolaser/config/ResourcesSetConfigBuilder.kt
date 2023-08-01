package ru.pocketbyte.locolaser.config

import groovy.lang.Closure
import org.gradle.internal.HasInternalProtocol
import ru.pocketbyte.locolaser.config.resources.EmptyResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.utils.callWithDelegate

@HasInternalProtocol
class ResourcesSetConfigBuilder(
    private val hasMainResource: Boolean = false
) {

    private val resources = LinkedHashSet<ResourcesConfig>()

    fun add(resourcesConfig: ResourcesConfig) {
        resources.add(resourcesConfig)
    }

    fun add(builder: ResourcesConfigBuilder<*>) {
        add(builder.build())
    }

    fun <T : ResourcesConfigBuilder<*>> add(builder: T, configurator: T.() -> Unit) {
        configurator.invoke(builder)
        add(builder)
    }

    fun <T : ResourcesConfigBuilder<*>> add(builder: T, configurator: Closure<Unit>) {
        add(builder) {
            configurator.callWithDelegate(this)
        }
    }

    fun build(): ResourcesConfig {
        return if (resources.isEmpty()) {
            EmptyResourcesConfig()
        } else if (resources.size == 1) {
            resources.first()
        } else {
            ResourcesSetConfig(resources, if (hasMainResource) resources.first() else null)
        }
    }
}