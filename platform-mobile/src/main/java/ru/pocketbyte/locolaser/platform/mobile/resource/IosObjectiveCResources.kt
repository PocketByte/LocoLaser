package ru.pocketbyte.locolaser.platform.mobile.resource

import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosObjectiveCHResourceFile
import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosObjectiveCMResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

class IosObjectiveCResources(
        resourcesDir: File,
        name: String,
        tableName: String?,
        filter: ((key: String) -> Boolean)?
) : IosBaseClassResources(resourcesDir, name, tableName, filter) {

    companion object {
        const val OBJC_H_FILE_EXTENSION = ".h"
        const val OBJC_M_FILE_EXTENSION = ".m"
    }

    private val objcHFile: File
        get() {
            val sourceDir = directory
            sourceDir.mkdirs()
            return File(sourceDir, name + OBJC_H_FILE_EXTENSION)
        }

    private val objcMFile: File
        get() {
            val sourceDir = directory
            sourceDir.mkdirs()
            return File(sourceDir, name + OBJC_M_FILE_EXTENSION)
        }

    override fun getResourceFiles(locales: Set<String>): Array<ResourceFile> {
        return arrayOf(IosObjectiveCHResourceFile(objcHFile, name), IosObjectiveCMResourceFile(objcMFile, name, tableName!!))
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(arrayOf<File?>(objcHFile, objcMFile))
    }
}