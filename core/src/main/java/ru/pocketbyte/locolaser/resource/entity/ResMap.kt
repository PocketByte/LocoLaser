/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

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

    fun merge(map: ResMap?): ResMap {
        map?.forEach { locale, value ->
            this[locale] = this[locale]?.merge(value) ?: value
        }
        return this
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
