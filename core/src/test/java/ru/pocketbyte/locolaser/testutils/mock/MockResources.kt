package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import java.io.File

open class MockResources(
    resourcesDir: File,
    name: String,
    filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, name, MockResourceFileProvider, filter) {

    override val fileExtension: String = "mock"
    override val formattingType: FormattingType = NoFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return null
    }

    override fun allFiles(locales: Set<String>): List<File> {
        return emptyList()
    }
}
