/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.file

import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.entity.ResMap

import java.io.*

/**
 * Represent resource file.
 *
 * @author Denis Shurygin
 */
interface ResourceFile {

    /**
     * Read resources map from the resource file.
     * @return Map with resources.
     */
    fun read(): ResMap?

    /**
     * Write resources map into resource files.
     * @param resMap Map with resources.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun write(resMap: ResMap, writingConfig: WritingConfig?)

}