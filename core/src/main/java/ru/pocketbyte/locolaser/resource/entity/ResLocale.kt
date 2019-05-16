/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import java.util.LinkedHashMap

/**
 * Resources map for single locale.
 * Key is the key of resource, value is the resource item.
 *
 * @author Denis Shurygin
 */
class ResLocale() : LinkedHashMap<String, ResItem>() {

    constructor(map: Map<String, ResItem>?) : this() {
        map?.values?.forEach {
            put(ResItem(it))
        }
        if (map != null)
            for (item in map.values) {

            }
    }

    override fun put(key: String, value: ResItem): ResItem {
        throw UnsupportedOperationException("Please, use put(ResItem value).")
    }

    override fun putAll(from: Map<out String, ResItem>) {
        throw UnsupportedOperationException("Please, use put(ResItem value).")
    }

    override fun putIfAbsent(key: String, value: ResItem): ResItem {
        throw UnsupportedOperationException("Please, use put(ResItem value).")
    }

    fun put(value: ResItem) {
        super.put(value.key, value)
    }

    fun merge(resLocale: ResLocale?): ResLocale {
        if (resLocale != null) {
            for ((key, value) in resLocale) {
                val destinationItem = this[key]
                if (destinationItem != null)
                    this.put(destinationItem.merge(value))
                else
                    this.put(value)
            }
        }
        return this
    }

    fun remove(mapForRemove: ResLocale?): ResLocale {
        if (mapForRemove != null) {
            for ((key, value) in mapForRemove) {
                val resItem = this[key]
                if (resItem != null) {
                    for (removeValue in value.values) {
                        resItem.removeValueForQuantity(removeValue.quantity)
                    }
                    if (resItem.values.isEmpty())
                        this.remove(resItem.key)
                }
            }
        }
        return this
    }
}