/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.filter
import ru.pocketbyte.locolaser.resource.entity.merge
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import java.io.File
import java.io.IOException

/**
 * Abstract base implementation of [Resources] providing common read/write logic.
 *
 * @author Denis Shurygin
 */
abstract class AbsResources(
    /** The directory where resource files are located. */
    val directory: File,
    /** Resource name. */
    val name: String,
    /** Provides the resource [File] for a given locale, directory, and name. */
    private val resourceFileProvider: ResourceFileProvider,
    /** Filter for including or excluding resources by key. */
    private val filter: ResourcesFilter?
) : Resources {

    protected abstract fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>?

    protected abstract val fileExtension: String

    protected fun getFileForLocale(
        locale: String,
        directory: File = this.directory,
        name: String = this.name,
        extension: String = this.fileExtension
    ): File {
        return resourceFileProvider.get(locale, directory, name, extension).apply {
            val parent = this.parentFile
            parent.mkdirs()
            if (!parent.exists()) {
                throw RuntimeException("Failed to create folder ${parentFile.absolutePath}")
            }
        }
    }

    override fun allFiles(locales: Set<String>): List<File> {
        return locales.map { getFileForLocale(it) }
    }

    override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap {
        val resMap = ResMap()
        getResourceFiles(locales)?.forEach { resFile ->
            resMap.merge(resFile.read(extraParams))
        }
        return resMap.filter(filter)
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        val filteredMap = resMap.filter(filter)
        getResourceFiles(filteredMap.keys)?.forEach {
            it.write(filteredMap, extraParams)
        }
    }
}
