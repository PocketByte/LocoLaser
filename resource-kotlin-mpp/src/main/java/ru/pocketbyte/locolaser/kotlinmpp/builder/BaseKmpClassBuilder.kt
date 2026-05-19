package ru.pocketbyte.locolaser.kotlinmpp.builder

import ru.pocketbyte.locolaser.config.resources.ResourcesConfigBuilderFactory
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig.Companion.DEFAULT_INTERFACE_NAME
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig.Companion.DEFAULT_PACKAGE
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder
import ru.pocketbyte.locolaser.utils.firstCharToUpperCase

/**
 * Abstract base builder for Kotlin Multiplatform platform-specific strings repository class configurations.
 *
 * Extends [BaseKmpBuilder] with class name and package configuration for the generated repository class.
 */
abstract class BaseKmpClassBuilder<
        ConfigType: KotlinBaseResourcesConfig,
        BuilderType: KotlinBaseResourcesConfigBuilder<ConfigType>>(
    internal val name: String,
    builderFactory: ResourcesConfigBuilderFactory<ConfigType, BuilderType>,
): BaseKmpBuilder<ConfigType, BuilderType>(builderFactory) {

    override var sourceSet: String = "${name}Main"

    /**
     * Package of the Repository that should be used in the class name.
     * Package will be ignored if the class name contains a canonical name.
     */
    var classPackage: String? = null

    /**
     * Canonical or simple name of the Repository class that should be generated.
     */
    var className: String? = null

    override fun getResourceName(
        mainBuilder: KotlinMultiplatformResourcesConfigBuilder
    ): String {
        return mergeName(
            classPackage
                ?: mainBuilder.repositoryPackage
                ?: DEFAULT_PACKAGE,
            className
                ?: mainBuilder.repositoryClass
                ?: getDefaultClassName(name)
        )
    }

    private fun getDefaultClassName(platformName: String): String {
        return "${platformName.firstCharToUpperCase()}$DEFAULT_INTERFACE_NAME"
    }
}
