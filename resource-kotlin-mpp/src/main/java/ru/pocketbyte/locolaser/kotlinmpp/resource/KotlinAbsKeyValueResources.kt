package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.kotlinmpp.resource.file.AbsKeyValuePoetClassResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import java.io.File

class KotlinAbsKeyValueResources(
    dir: File,
    name: String,
    interfaceName: String,
    override val formattingType: FormattingType,
    filter: ((key: String) -> Boolean)?
) : AbsKotlinImplementationResources(dir, name, interfaceName, filter) {

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile> {
        return arrayOf(AbsKeyValuePoetClassResourceFile(directory,
            this.className, this.classPackage,
            this.interfaceName, this.interfacePackage,
            this.formattingType
        ))
    }
}