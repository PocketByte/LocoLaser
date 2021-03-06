/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.json.resource

import ru.pocketbyte.locolaser.json.resource.file.JsonResourceFile
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

/**
 * @author Denis Shurygin
 */
class JsonResources(
        resourcesDir: File,
        fileName: String,
        private val indent: Int,
        filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, fileName, filter) {

    override val formattingType: FormattingType = WebFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            JsonResourceFile(getFileForLocale(localesArray[i]), localesArray[i], indent)
        }
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(getFileForLocale(locale))
    }

    private fun getFileForLocale(locale: String): File {
        val localeFolder = File(directory, getLocaleDirName(locale))
        localeFolder.mkdirs()
        return File(localeFolder, "$name.json")
    }

    private fun getLocaleDirName(locale: String): String {
        if (Resources.BASE_LOCALE == locale)
            return "en/"

        return "$locale/"
    }
}
