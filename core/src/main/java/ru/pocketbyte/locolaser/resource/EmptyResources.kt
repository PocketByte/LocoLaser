package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary
import java.io.File

class EmptyResources: Resources {

    override val formattingType: FormattingType = NoFormattingType

    override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap? {
        return ResMap()
    }

    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        // do nothing
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(0, null)
    }

    override fun allFiles(locales: Set<String>): List<File> {
        return emptyList()
    }
}
