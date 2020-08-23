package ru.pocketbyte.locolaser.config.resources

import ru.pocketbyte.locolaser.resource.EmptyResources
import java.io.File

class EmptyResourcesConfig : ResourcesConfig {

    companion object {
        const val TYPE = "null"
    }

    override val type: String = TYPE
    override val resources = EmptyResources()
    override val defaultTempDir = File("./temp/")

}
