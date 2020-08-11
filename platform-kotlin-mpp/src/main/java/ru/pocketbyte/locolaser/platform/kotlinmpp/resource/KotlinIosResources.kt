package ru.pocketbyte.locolaser.platform.kotlinmpp.resource

import ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file.KotlinIosResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType

import java.io.File

class KotlinIosResources(
        dir: File,
        name: String,
        interfaceName: String,
        filter: ((key: String) -> Boolean)?
) : AbsKotlinImplementationPlatformResources(dir, name, interfaceName, filter) {

    override val formattingType: FormattingType = JavaFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(KotlinIosResourceFile(directory,
                this.className, this.classPackage,
                this.interfaceName, this.interfacePackage))
    }
}
