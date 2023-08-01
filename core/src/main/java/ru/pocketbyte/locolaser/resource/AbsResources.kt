/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.filter
import ru.pocketbyte.locolaser.resource.entity.merge
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File
import java.io.IOException

/**
 * @author Denis Shurygin
 */
abstract class AbsResources(
    /** Resource directory path. */
    val directory: File,
    /** Resource name. */
    val name: String,
    /** Provides resource File depending on locale, directory and name */
    private val resourceFileProvider: ResourceFileProvider,
    /** Resource name. */
    private val filter: ((key: String) -> Boolean)?
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

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(getFileForLocale(locale))
    }

    override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap {
        val resMap = ResMap()
        getResourceFiles(locales)?.forEach { resFile ->
            resMap.merge(resFile.read(extraParams))
        }
        return resMap
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        val filteredMap = resMap.filter(filter)
        getResourceFiles(filteredMap.keys)?.forEach {
            it.write(filteredMap, extraParams)
        }
    }
}
