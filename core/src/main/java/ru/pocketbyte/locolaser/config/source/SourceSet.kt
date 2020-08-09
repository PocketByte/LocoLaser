/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.merge
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.MixedFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary
import kotlin.math.max

/**
 * @author Denis Shurygin
 */
class SourceSet(
    private val sources: Set<Source>,
    private val default: Source
) : Source() {

    override val formattingType: FormattingType
        get() {
            val firstItemType = if (sources.isEmpty()) {
                NoFormattingType
            } else {
                sources.elementAt(0).formattingType
            }

            return if (sources.find { it.formattingType != firstItemType } != null) {
                MixedFormattingType
            } else {
                firstItemType
            }
        }

    override val modifiedDate: Long
        get() = sources.fold(0L) { acc, source ->
            max(acc, source.modifiedDate)
        }

    override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap? {
        return sources.fold(null as? ResMap) { resMap, source ->
            resMap.merge(source.read(locales, extraParams))
        }
    }

    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        default.write(resMap, extraParams)
    }

    override fun close() {
        sources.forEach { it.close() }
    }

    override fun summaryForLocale(locale: String): FileSummary {
        var bytes: Long = 0
        val hash = StringBuilder()

        sources.forEach { source ->
            source.summaryForLocale(locale).let {
                bytes += it.bytes
                hash.append(it.hash)
            }
        }

        return FileSummary(bytes, hash.toString())
    }
}
