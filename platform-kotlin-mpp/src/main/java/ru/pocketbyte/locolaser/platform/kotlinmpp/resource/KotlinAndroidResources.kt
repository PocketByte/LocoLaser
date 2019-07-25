package ru.pocketbyte.locolaser.platform.kotlinmpp.resource

import ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file.KotlinAndroidResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File

class KotlinAndroidResources(
        dir: File,
        name: String,
        interfaceName: String
) : AbsKotlinImplementationPlatformResources(dir, name, interfaceName) {

    override fun getResourceFiles(locales: Set<String>): Array<ResourceFile> {
        return arrayOf(KotlinAndroidResourceFile(file,
                this.className, this.classPackage,
                this.interfaceName, this.interfacePackage))
    }
}
