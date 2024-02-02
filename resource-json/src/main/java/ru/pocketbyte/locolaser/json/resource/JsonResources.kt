/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.json.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.json.KeyPluralizationRule
import ru.pocketbyte.locolaser.json.resource.file.JsonResourceFile
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType

import java.io.File

/**
 * @author Denis Shurygin
 */
class JsonResources(
    resourcesDir: File,
    fileName: String,
    resourceFileProvider: ResourceFileProvider,
    private val indent: Int,
    private val pluralKeyRule: KeyPluralizationRule.Postfix,
    filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, fileName, resourceFileProvider, filter) {

    override val formattingType: FormattingType = WebFormattingType
    override val fileExtension: String = "json"

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        val localesArray = locales?.toTypedArray() ?: return null
        return Array(locales.size) { i ->
            JsonResourceFile(
                getFileForLocale(localesArray[i]),
                localesArray[i], indent,
                pluralKeyRule
            )
        }
    }
}
