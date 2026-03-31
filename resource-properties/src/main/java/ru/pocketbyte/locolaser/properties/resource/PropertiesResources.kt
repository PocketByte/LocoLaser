package ru.pocketbyte.locolaser.properties.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.properties.resource.file.PropertiesResourceFile
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class PropertiesResources(
    resourcesDir: File,
    name: String,
    resourceFileProvider: ResourceFileProvider,
    filter: ResourcesFilter?
) : AbsResources(resourcesDir, name, resourceFileProvider, filter) {

    override val formattingType: FormattingType = JavaFormattingType
    override val fileExtension: String = "properties"

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            PropertiesResourceFile(getFileForLocale(localesArray[i]), localesArray[i])
        }
    }
}
