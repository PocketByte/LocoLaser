package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary
import java.io.File

open class MockResources(
        resourcesDir: File,
        name: String,
        filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, name, MockResourceFileProvider(), filter) {

    private class MockResourceFileProvider : ResourceFileProvider {
        override fun get(locale: String, directory: File, name: String, extension: String): File {
            return File(File(directory, locale), "$name.$extension")
        }
    }

    override val formattingType: FormattingType = NoFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return null
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(0, null)
    }
}
