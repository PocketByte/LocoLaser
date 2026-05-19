package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.resource.EmptyResources

/**
 * A no-op implementation of [ResourcesConfig] representing an absent or unconfigured resource.
 */
class EmptyResourcesConfig : ResourcesConfig {

    companion object {
        /** Type identifier for an empty resources config. */
        const val TYPE = "null"
    }

    override val type: String = TYPE

    override val resources = EmptyResources()
}
