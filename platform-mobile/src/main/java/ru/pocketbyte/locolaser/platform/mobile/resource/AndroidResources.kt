/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource

import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import ru.pocketbyte.locolaser.platform.mobile.resource.file.AndroidResourceFile
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

/**
 * @author Denis Shurygin
 */
class AndroidResources(
        resourcesDir: File,
        fileName: String,
        filter: ((key: String) -> Boolean)?
) : AbsPlatformResources(resourcesDir, fileName, filter) {

    override fun getResourceFiles(locales: Set<String>): Array<ResourceFile> {
        val localesArray = locales.toTypedArray()
        return Array(locales.size) { i ->
            AndroidResourceFile(getFileForLocale(localesArray[i]), localesArray[i])
        }
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(getFileForLocale(locale))
    }

    private fun getFileForLocale(locale: String): File {
        val localeFolder = File(directory, getLocaleDirName(locale))
        localeFolder.mkdirs()
        return File(localeFolder, "$name.xml")
    }

    private fun getLocaleDirName(locale: String): String {
        val builder = StringBuilder("values")
        if (PlatformResources.BASE_LOCALE == locale)
            builder.append("/")
        else
            builder.append("-").append(locale).append("/")
        return builder.toString()
    }
}
