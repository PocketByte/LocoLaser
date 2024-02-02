package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.kotlinmpp.resource.file.KotlinJsResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType

import java.io.File

class KotlinJsResources(
    dir: File,
    name: String,
    interfaceName: String?,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : KotlinAbsImplementationResources(dir, name, interfaceName, resourceFileProvider, filter) {

    override val formattingType: FormattingType = WebFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(KotlinJsResourceFile(directory,
                this.className, this.classPackage,
                this.interfaceName, this.interfacePackage))
    }
}
