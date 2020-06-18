package ru.pocketbyte.locolaser.platform.kotlinmpp.resource

import ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file.KotlinCommonResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File

class KotlinCommonResources(
        dir: File,
        name: String,
        filter: ((key: String) -> Boolean)?
) : AbsKotlinPlatformResources(dir, name, filter) {

    override fun getResourceFiles(locales: Set<String>): Array<ResourceFile> {
        return arrayOf(KotlinCommonResourceFile(directory, this.className, this.classPackage))
    }
}
