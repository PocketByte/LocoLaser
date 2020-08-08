/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.IOException

/**
 * Represent resources for specified platform.
 *
 * @author Denis Shurygin
 */
interface PlatformResources {

    companion object {
        const val BASE_LOCALE = "base"
    }

    val formattingType: FormattingType

    /**
     * Read resources map from the resource files. Keys from the result map duplicate resource locale's.
     * @return Resources map. Keys from the map duplicate resource locale's.
     */
    fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap?

    @Throws(IOException::class)
    fun write(resMap: ResMap, extraParams: ExtraParams?)

    fun summaryForLocale(locale: String): FileSummary
}
