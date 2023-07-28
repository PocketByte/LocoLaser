package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.mobile.resource.file.IosPlistResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import java.io.File

class IosPlistResources(
    resourcesDir: File,
    name: String,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : AbsIosStringsResources(resourcesDir, name, resourceFileProvider, filter) {

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            IosPlistResourceFile(getFileForLocale(localesArray[i]), localesArray[i])
        }
    }
}