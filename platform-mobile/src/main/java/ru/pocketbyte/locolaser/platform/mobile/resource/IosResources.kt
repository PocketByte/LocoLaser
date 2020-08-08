/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource

import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosPluralResourceFile
import ru.pocketbyte.locolaser.platform.mobile.resource.file.IosResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary
import java.io.File

/**
 * Resource implementation for iOS platform.
 *
 * @author Denis Shurygin
 */
class IosResources(
        resourcesDir: File,
        name: String,
        filter: ((key: String) -> Boolean)?
) : AbsIosStringsResources(resourcesDir, name, filter) {

    companion object {
        const val PLURAL_RES_FILE_EXTENSION = ".stringsdict"
    }

    override val formattingType: FormattingType = JavaFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size * 2) { i->
            val index: Int = i / 2
            if (i % 2 == 0) {
                IosResourceFile(getFileForLocale(localesArray[index]), localesArray[index])
            }
            else {
                IosPluralResourceFile(getPluralFileForLocale(localesArray[index]), localesArray[index])
            }
        }
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(arrayOf<File?>(getFileForLocale(locale), getPluralFileForLocale(locale)))
    }

    private fun getPluralFileForLocale(locale: String): File {
        val localeFolder = File(directory, getLocaleDirName(locale))
        localeFolder.mkdirs()
        return File(localeFolder, name + PLURAL_RES_FILE_EXTENSION)
    }
}
