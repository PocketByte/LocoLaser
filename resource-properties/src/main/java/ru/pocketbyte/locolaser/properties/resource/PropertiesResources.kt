package ru.pocketbyte.locolaser.properties.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.properties.resource.file.PropertiesResourceFile
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import java.io.File

class PropertiesResources(
    resourcesDir: File,
    name: String,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, name, resourceFileProvider, filter) {

    override val formattingType: FormattingType = WebFormattingType
    override val fileExtension: String = "properties"

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            PropertiesResourceFile(getFileForLocale(localesArray[i]), localesArray[i])
        }
    }
}
