package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.mobile.resource.file.IosSwiftResourceFile
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File

class IosSwiftResources(
    resourcesDir: File,
    name: String,
    resourceFileProvider: ResourceFileProvider,
    tableName: String?,
    filter: ((key: String) -> Boolean)?
) : IosBaseClassResources(resourcesDir, name, resourceFileProvider, tableName, filter) {

    companion object {
        const val SWIFT_FILE_EXTENSION = "swift"
    }

    override val fileExtension: String = SWIFT_FILE_EXTENSION

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile> {
        return arrayOf(
            IosSwiftResourceFile(getFileForLocale(Resources.BASE_LOCALE), name, tableName)
        )
    }

    override fun allFiles(locales: Set<String>): List<File> {
        return listOf(getFileForLocale(Resources.BASE_LOCALE))
    }
}
