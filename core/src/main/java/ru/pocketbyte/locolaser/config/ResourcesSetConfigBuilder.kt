package ru.pocketbyte.locolaser.config

import groovy.lang.Closure
import org.gradle.internal.HasInternalProtocol
import ru.pocketbyte.locolaser.config.resources.EmptyResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.utils.callWithDelegate
import java.io.File

/**
 * Builder for assembling a set of [ResourcesConfig] instances used as platform or source.
 *
 * @param hasMainResource If true, the first added resource is designated as the main resource
 * of the resulting [ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig].
 */
@HasInternalProtocol
class ResourcesSetConfigBuilder(
    private val hasMainResource: Boolean = false
) {

    private val resources = LinkedHashSet<ResourcesConfigBuilder<*>>()

    /**
     * Adds the given resource config builder to the set.
     */
    fun add(builder: ResourcesConfigBuilder<*>) {
        resources.add(builder)
    }

    /**
     * Adds a resource config builder obtained from the given factory to the set.
     */
    fun add(builderFactory: ResourcesConfigBuilderFactory<*, *>) {
        add(builderFactory.getBuilder())
    }

    /**
     * Adds the given resource config builder to the set after applying the given configuration action.
     */
    fun <T : ResourcesConfigBuilder<*>> add(builder: T, configurator: T.() -> Unit) {
        configurator.invoke(builder)
        add(builder)
    }

    /**
     * Adds the given resource config builder to the set after applying the given Groovy closure as a configuration action.
     */
    fun <T : ResourcesConfigBuilder<*>> add(builder: T, configurator: Closure<Unit>) {
        add(builder) {
            configurator.callWithDelegate(this)
        }
    }

    /**
     * Adds a resource config builder obtained from the given factory to the set after applying the given configuration action.
     */
    fun <T : ResourcesConfigBuilder<*>> add(
        builderFactory: ResourcesConfigBuilderFactory<*, T>,
        configurator: T.() -> Unit
    ) {
        add(builderFactory.getBuilder(), configurator)
    }

    /**
     * Adds a resource config builder obtained from the given factory to the set after applying the given Groovy closure as a configuration action.
     */
    fun <T : ResourcesConfigBuilder<*>> add(
        builderFactory: ResourcesConfigBuilderFactory<*, T>,
        configurator: Closure<Unit>
    ) {
        add(builderFactory) {
            configurator.callWithDelegate(this)
        }
    }

    internal fun build(workDir: File?): ResourcesConfig {
        return if (resources.isEmpty()) {
            EmptyResourcesConfig()
        } else if (resources.size == 1) {
            resources.first().build(workDir)
        } else {
            resources.map { it.build(workDir) }.toSet().let {
                ResourcesSetConfig(it, if (hasMainResource) it.first() else null)
            }
        }
    }
}