package ru.pocketbyte.locolaser.ini.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.ini.resource.file.IniResourceFile
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

class IniResources(
    resourcesDir: File,
    fileName: String,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, fileName, resourceFileProvider, filter) {

    override val formattingType: FormattingType = JavaFormattingType
    override val fileExtension: String = "ini"

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(IniResourceFile(getFileForLocale(Resources.BASE_LOCALE), locales))
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(getFileForLocale(Resources.BASE_LOCALE))
    }
}
