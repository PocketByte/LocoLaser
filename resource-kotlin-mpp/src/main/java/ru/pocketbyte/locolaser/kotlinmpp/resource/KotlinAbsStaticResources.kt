package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.kotlinmpp.resource.file.KotlinAbsStaticResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class KotlinAbsStaticResources(
    dir: File,
    name: String,
    interfaceName: String,
    override val formattingType: FormattingType,
    filter: ((key: String) -> Boolean)?
) : AbsKotlinImplementationResources(dir, name, interfaceName, filter) {

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(
            KotlinAbsStaticResourceFile(
                directory, className, classPackage,
                interfaceName, interfacePackage,
                formattingType
            )
        )
    }
}
