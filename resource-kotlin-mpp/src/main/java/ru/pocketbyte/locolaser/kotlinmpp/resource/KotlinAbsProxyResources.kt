package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.kotlinmpp.resource.file.AbsProxyClassResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class KotlinAbsProxyResources(
    dir: File,
    name: String,
    interfaceName: String,
    filter: ((key: String) -> Boolean)?
) : AbsKotlinImplementationResources(dir, name, interfaceName, filter) {

    override val formattingType = JavaFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(
            AbsProxyClassResourceFile(
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
