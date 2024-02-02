package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.kotlinmpp.resource.file.KotlinAndroidResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType

import java.io.File

class KotlinAndroidResources(
    dir: File,
    name: String,
    interfaceName: String?,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : KotlinAbsImplementationResources(dir, name, interfaceName, resourceFileProvider, filter) {

    override val formattingType: FormattingType = JavaFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(KotlinAndroidResourceFile(directory,
                this.className, this.classPackage,
                this.interfaceName, this.interfacePackage))
    }
}
