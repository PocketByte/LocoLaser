/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import java.io.File

import java.io.IOException
import java.io.Serializable

/**
 * Represents a set of localization resources that can be read from and written to.
 *
 * @author Denis Shurygin
 */
interface Resources: Serializable {

    companion object {
        /** The locale identifier used to represent the base (default) locale. Value: `"base"`. */
        const val BASE_LOCALE = "base"
    }

    /**
     * The formatting type used to parse and convert formatting arguments in resource values.
     */
    val formattingType: FormattingType

    /**
     * Reads and merges resources from all resource files for the given locales.
     * @param locales The set of locale identifiers to read, or null to read all available locales.
     * @param extraParams Additional parameters passed to the read operation, or null for defaults.
     * @return A [ResMap] containing the merged resources, or null if no resources were found.
     */
    fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap?

    /**
     * Writes the given resource map to resource files.
     * @param resMap The resource map to write.
     * @param extraParams Additional parameters passed to the write operation, or null for defaults.
     */
    @Throws(IOException::class)
    fun write(resMap: ResMap, extraParams: ExtraParams?)

    /**
     * Returns the list of resource files for the given locales.
     * @param locales The set of locale identifiers to get files for.
     * @return A list of [File] instances corresponding to the given locales.
     */
    fun allFiles(locales: Set<String>): List<File>
}
