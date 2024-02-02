package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.kotlinmpp.resource.file.KotlinAbsProxyResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class KotlinAbsProxyResources(
    dir: File,
    name: String,
    interfaceName: String?,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : KotlinAbsImplementationResources(dir, name, interfaceName, resourceFileProvider, filter) {

    override val formattingType = JavaFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(
            KotlinAbsProxyResourceFile(
                directory, className, classPackage,
                interfaceName ?: throwNoInterface(),
                interfacePackage ?: throwNoInterface(),
                formattingType
            )
        )
    }

    private fun throwNoInterface(): String {
        throw IllegalArgumentException(
            "Interface missing. KotlinAbsProxy requires interface implementation."
        )
    }
}
