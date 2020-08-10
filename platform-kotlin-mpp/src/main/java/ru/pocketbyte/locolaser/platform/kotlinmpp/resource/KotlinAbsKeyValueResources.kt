package ru.pocketbyte.locolaser.platform.kotlinmpp.resource

import ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file.AbsKeyValuePoetClassResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import java.io.File

class KotlinAbsKeyValueResources(
        dir: File,
        name: String,
        interfaceName: String,
        filter: ((key: String) -> Boolean)?
) : AbsKotlinImplementationPlatformResources(dir, name, interfaceName, filter) {

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(AbsKeyValuePoetClassResourceFile(directory,
                this.className, this.classPackage,
                this.interfaceName, this.interfacePackage,
                this.formattingType))
    }

}