package ru.pocketbyte.locolaser.platform.ini.resource

import ru.pocketbyte.locolaser.platform.ini.resource.file.IniResourceFile
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

class IniResources(
        resourcesDir: File,
        fileName: String,
        filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, fileName, filter) {

    override val formattingType: FormattingType = JavaFormattingType

    private val resourceFile: File
        get() {
            val localeFolder = File(directory, "/")
            localeFolder.mkdirs()
            return File(localeFolder, "$name.ini")
        }

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(IniResourceFile(resourceFile, locales))
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(resourceFile)
    }
}
