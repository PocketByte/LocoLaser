package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

class EmptySource(sourceConfig: SourceConfig) : Source(sourceConfig) {

    override val formattingType: FormattingType = NoFormattingType
    override val modifiedDate: Long = 0

    override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap? {
        return ResMap()
    }

    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        // do nothing
    }

    override fun close() {
        // do nothing
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(0, null)
    }
}
