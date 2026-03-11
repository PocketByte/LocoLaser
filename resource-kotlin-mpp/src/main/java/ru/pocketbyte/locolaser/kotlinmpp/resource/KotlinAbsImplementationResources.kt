package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.io.File

abstract class KotlinAbsImplementationResources(
    dir: File,
    name: String,
    interfaceName: String?,
    resourceFileProvider: ResourceFileProvider,
    filter: ResourcesFilter?
) : KotlinAbsResources(dir, name, resourceFileProvider, filter) {

    val interfaceName: String?
    val interfacePackage: String?

    init {
        if (interfaceName != null) {
            val lastSegmentIndex = interfaceName.lastIndexOf(".")
            if (lastSegmentIndex <= 0)
                throw IllegalArgumentException("Invalid interface name: $interfaceName")

            this.interfaceName = interfaceName.substring(lastSegmentIndex + 1)
            this.interfacePackage = interfaceName.substring(0, lastSegmentIndex)
        } else {
            this.interfaceName = null
            this.interfacePackage = null
        }
    }
}
