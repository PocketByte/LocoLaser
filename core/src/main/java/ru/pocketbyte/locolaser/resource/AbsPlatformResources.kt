/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.filter
import ru.pocketbyte.locolaser.resource.entity.merge
import ru.pocketbyte.locolaser.resource.file.ResourceFile

import java.io.File
import java.io.IOException

/**
 * @author Denis Shurygin
 */
abstract class AbsPlatformResources(
        /** Resource directory path. */
        val directory: File,
        /** Resource name. */
        val name: String,
        /** Resource name. */
        private val filter: ((key: String) -> Boolean)?
) : PlatformResources {

    protected abstract fun getResourceFiles(locales: Set<String>): Array<ResourceFile>?

    override fun read(locales: Set<String>, extraParams: ExtraParams): ResMap {
        val resMap = ResMap()
        getResourceFiles(locales)?.forEach {
            resMap.merge(it.read(extraParams))
        }
        return resMap
    }

    @Throws(IOException::class)
    override fun write(map: ResMap, extraParams: ExtraParams?) {
        val filteredMap = map.filter(filter)
        getResourceFiles(filteredMap.keys)?.forEach {
            it.write(filteredMap, extraParams)
        }
    }
}
