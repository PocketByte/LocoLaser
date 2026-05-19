package ru.pocketbyte.locolaser.kotlinmpp.builder

import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinCommonResourcesConfigBuilder
import ru.pocketbyte.locolaser.kotlinmpp.KotlinMultiplatformResourcesConfigBuilder


/**
 * Builder for the Kotlin Multiplatform common strings repository interface configuration.
 *
 * Configures the interface generated for shared (`commonMain`) code.
 */
class KmpInterfaceBuilder : BaseKmpBuilder<
        KotlinCommonResourcesConfig,
        KotlinCommonResourcesConfigBuilder>(KotlinCommonResourcesConfig) {

    override var sourceSet: String = "commonMain"

    /**
     * Package of the Repository that should be used in the interface name.
     * Package will be ignored if the interface name contains a canonical name.
     */
    var interfacePackage: String? = null

    /**
     * Canonical or simple name of the Repository interface that should be generated.
     */
    var interfaceName: String? = null

    override fun getResourceName(
        mainBuilder: KotlinMultiplatformResourcesConfigBuilder
    ): String {
        return mergeName(
            interfacePackage
                ?: mainBuilder.repositoryPackage
                ?: KotlinBaseResourcesConfig.DEFAULT_PACKAGE,
            interfaceName
                ?: mainBuilder.repositoryInterface
                ?: KotlinBaseResourcesConfig.DEFAULT_INTERFACE_NAME
        )
    }
}
