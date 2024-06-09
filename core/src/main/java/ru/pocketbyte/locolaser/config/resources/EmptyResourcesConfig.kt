package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.resource.EmptyResources

class EmptyResourcesConfig : ResourcesConfig {

    companion object {
        const val TYPE = "null"
    }

    override val type: String = TYPE
    override val resources = EmptyResources()
}
