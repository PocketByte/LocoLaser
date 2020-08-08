package ru.pocketbyte.locolaser.platform.mobile.resource

import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosPlistResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

class IosPlistResources(
        resourcesDir: File,
        name: String,
        filter: ((key: String) -> Boolean)?
) : AbsIosStringsResources(resourcesDir, name, filter) {

    override val formattingType: FormattingType = JavaFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            IosPlistResourceFile(getFileForLocale(localesArray[i]), localesArray[i])
        }
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(getFileForLocale(locale))
    }
}