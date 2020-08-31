package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.mobile.resource.file.IosSwiftResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

class IosSwiftResources(
        resourcesDir: File,
        name: String,
        tableName: String?,
        filter: ((key: String) -> Boolean)?
) : IosBaseClassResources(resourcesDir, name, tableName, filter) {

    companion object {
        const val SWIFT_FILE_EXTENSION = ".swift"
    }

    override val formattingType: FormattingType = NoFormattingType

    private val swiftFile: File
        get() {
            val sourceDir = directory
            sourceDir.mkdirs()
            return File(sourceDir, name + SWIFT_FILE_EXTENSION)
        }

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(IosSwiftResourceFile(swiftFile, name, tableName!!))
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(swiftFile)
    }
}
