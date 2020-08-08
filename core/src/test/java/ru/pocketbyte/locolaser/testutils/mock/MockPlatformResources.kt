package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

open class MockPlatformResources(
        resourcesDir: File,
        name: String,
        filter: ((key: String) -> Boolean)?
) : AbsPlatformResources(resourcesDir, name, filter) {

    override val formattingType: FormattingType = NoFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return null
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(0, null)
    }
}
