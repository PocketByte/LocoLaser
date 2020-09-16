package ru.pocketbyte.locolaser.properties.resource

import ru.pocketbyte.locolaser.properties.resource.file.PropertiesResourceFile
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

class PropertiesResources(
        resourcesDir: File,
        fileName: String,
        filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, fileName, filter) {

    override val formattingType: FormattingType = WebFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            PropertiesResourceFile(getFileForLocale(localesArray[i]), localesArray[i])
        }
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(getFileForLocale(locale))
    }

    private fun getFileForLocale(locale: String): File {
        val localeFolder = File(directory, getLocaleDirName(locale))
        localeFolder.mkdirs()
        return File(localeFolder, "$name.properties")
    }

    private fun getLocaleDirName(locale: String): String {
        return "$locale/"
    }
}
