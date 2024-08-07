/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.merge
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.MixedFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import java.io.File

import java.io.IOException

class ResourcesSet(
    private val resources: Set<Resources>,
    private val main: Resources? = null
) : Resources {

    override val formattingType: FormattingType
        get() {
            val firstItemType = if (resources.isEmpty()) {
                NoFormattingType
            } else {
                resources.elementAt(0).formattingType
            }

            return if (resources.find { it.formattingType != firstItemType } != null) {
                MixedFormattingType
            } else {
                firstItemType
            }
        }

    override fun read(locales: Set<String>?, extraParams: ExtraParams?): ResMap? {
        return resources.fold(null as? ResMap?) { resMap, source ->
            resMap.merge(source.read(locales, extraParams))
        }
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        if (main != null) {
            main.write(resMap, extraParams)
        } else {
            resources.forEach {
                it.write(resMap, extraParams)
            }
        }
    }

    override fun allFiles(locales: Set<String>): List<File> {
        return resources.fold(mutableListOf()) { list, resource ->
            list.apply {
                addAll(resource.allFiles(locales))
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ResourcesSet) {
            return false
        }
        return main == other.main
            && resources == other.resources
    }

    override fun hashCode(): Int {
        var result = resources.hashCode()
        result = 31 * result + (main?.hashCode() ?: 0)
        return result
    }
}
