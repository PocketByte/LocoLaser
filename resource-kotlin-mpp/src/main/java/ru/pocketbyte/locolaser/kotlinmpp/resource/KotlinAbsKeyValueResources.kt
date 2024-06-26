package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.kotlinmpp.resource.file.KotlinAbsKeyValueResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import java.io.File

class KotlinAbsKeyValueResources(
    dir: File,
    name: String,
    interfaceName: String?,
    override val formattingType: FormattingType,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : KotlinAbsImplementationResources(dir, name, interfaceName, resourceFileProvider, filter) {

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile> {
        return arrayOf(KotlinAbsKeyValueResourceFile(directory,
            this.className, this.classPackage,
            this.interfaceName, this.interfacePackage,
            this.formattingType
        ))
    }
}