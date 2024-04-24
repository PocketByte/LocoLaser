package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.mobile.resource.file.IosObjectiveCHResourceFile
import ru.pocketbyte.locolaser.mobile.resource.file.IosObjectiveCMResourceFile
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

class IosObjectiveCResources(
    resourcesDir: File,
    name: String,
    resourceFileProvider: ResourceFileProvider,
    tableName: String?,
    filter: ((key: String) -> Boolean)?
) : IosBaseClassResources(resourcesDir, name, resourceFileProvider, tableName, filter) {

    companion object {
        const val OBJC_H_FILE_EXTENSION = ".h"
        const val OBJC_M_FILE_EXTENSION = ".m"
    }

    private val objcHFile: File by lazy {
        getFileForLocale(Resources.BASE_LOCALE, extension = OBJC_H_FILE_EXTENSION)
    }

    private val objcMFile: File by lazy {
        getFileForLocale(Resources.BASE_LOCALE, extension = OBJC_M_FILE_EXTENSION)
    }

    override val fileExtension: String = ""

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile> {
        return arrayOf(
            IosObjectiveCHResourceFile(objcHFile, name),
            IosObjectiveCMResourceFile(objcMFile, name, tableName)
        )
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(arrayOf(objcHFile, objcMFile))
    }

    override fun allFiles(locales: Set<String>): List<File> {
        return listOf(objcHFile, objcMFile)
    }
}