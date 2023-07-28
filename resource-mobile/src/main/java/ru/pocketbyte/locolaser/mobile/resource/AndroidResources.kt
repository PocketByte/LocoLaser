/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.mobile.resource.file.AndroidResourceFile
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

/**
 * @author Denis Shurygin
 */
class AndroidResources(
    resourcesDir: File,
    fileName: String,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, fileName, resourceFileProvider, filter) {

    override val formattingType: FormattingType = JavaFormattingType
    override val fileExtension: String = "xml"

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            AndroidResourceFile(getFileForLocale(localesArray[i]), localesArray[i])
        }
    }
}
