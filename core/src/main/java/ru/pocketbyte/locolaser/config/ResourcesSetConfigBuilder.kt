package ru.pocketbyte.locolaser.config

import groovy.lang.Closure
import org.gradle.internal.HasInternalProtocol
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.EmptyResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.utils.callWithDelegate
import java.io.File

@HasInternalProtocol
class ResourcesSetConfigBuilder(
    private val hasMainResource: Boolean = false
) {

    private val resources = LinkedHashSet<ResourcesConfigBuilder<*>>()

    fun add(builder: ResourcesConfigBuilder<*>) {
        resources.add(builder)
    }

    fun add(builderFactory: ResourcesConfigBuilderFactory<*, *>) {
        add(builderFactory.getBuilder())
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

    fun <T : ResourcesConfigBuilder<*>> add(
        builderFactory: ResourcesConfigBuilderFactory<*, T>,
        configurator: T.() -> Unit
    ) {
        add(builderFactory.getBuilder(), configurator)
    }

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