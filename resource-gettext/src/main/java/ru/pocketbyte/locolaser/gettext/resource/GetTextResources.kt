/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.gettext.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.gettext.resource.file.GetTextResourceFile
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import java.io.File

class GetTextResources(
    resourcesDir: File,
    fileName: String,
    resourceFileProvider: ResourceFileProvider,
    filter: ResourcesFilter?
) : AbsResources(resourcesDir, fileName, resourceFileProvider, filter) {

    override val formattingType: FormattingType = NoFormattingType
    override val fileExtension = "po"

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            GetTextResourceFile(getFileForLocale(localesArray[i]), localesArray[i])
        }
    }
}
