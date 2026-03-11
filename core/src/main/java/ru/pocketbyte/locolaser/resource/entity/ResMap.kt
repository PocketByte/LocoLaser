/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import java.util.HashMap

/**
 * @author Denis Shurygin
 */
class ResMap() : HashMap<String, ResLocale>() {

    constructor(map: Map<String, ResLocale>?) : this() {
        map?.forEach { key, value ->
            put(key, ResLocale(value))
        }
    }

    fun remove(map: ResMap?): ResMap {
        map?.forEach { locale, removeItem ->
            val destinationItems = this[locale]

            destinationItems?.remove(removeItem)
            if (destinationItems?.size == 0)
                this.remove(locale)
        }
        return this
    }
}

fun ResMap?.merge(map: ResMap?): ResMap? {
    if (this == null)
        return map

    map?.forEach { locale, value ->
        this[locale] = this[locale]?.merge(value) ?: value
    }
    return this
}

inline fun ResMap.filter(filter: ((key: String) -> Boolean)): ResMap {
    val newResMap = ResMap()

    this.forEach { (key, resLocale) ->
        newResMap[key] = resLocale.filter(filter)
    }

    return newResMap
}

fun ResMap.filter(filter: ResourcesFilter?): ResMap {
    if (filter == null) {
        return this
    }

    return filter { key ->
        filter.filter(key)
    }
}