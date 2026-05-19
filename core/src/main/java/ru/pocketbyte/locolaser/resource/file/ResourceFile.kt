/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.file

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType

import java.io.*

/**
 * Represents a single localization resource file.
 *
 * @author Denis Shurygin
 */
interface ResourceFile {

    /**
     * The formatting type used to parse and convert formatting arguments in this file's values.
     */
    val formattingType: FormattingType

    /**
     * Reads and returns the resource map from this file.
     * @param extraParams Additional parameters for the read operation, or null for defaults.
     * @return A [ResMap] containing the resources, or null if the file is empty or unreadable.
     */
    fun read(extraParams: ExtraParams?): ResMap?

    /**
     * Writes the given resource map to this file.
     * @param resMap The resource map to write.
     * @param extraParams Additional parameters for the write operation, or null for defaults.
     * @throws IOException if an I/O error occurs during writing.
     */
    @Throws(IOException::class)
    fun write(resMap: ResMap, extraParams: ExtraParams?)
}
