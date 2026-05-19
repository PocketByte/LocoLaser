/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.resource.entity

import java.util.LinkedHashMap

/**
 * A map of resource items for a single locale, keyed by resource key.
 *
 * @author Denis Shurygin
 */
class ResLocale() : LinkedHashMap<String, ResItem>() {

    constructor(map: Map<String, ResItem>?) : this() {
        map?.values?.forEach {
            put(ResItem(it))
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

    /**
     * Adds or replaces the resource item in the map using its key.
     */
    fun put(value: ResItem) {
        super.put(value.key, value)
    }

    /**
     * Merges [resLocale] into this map, combining values for existing keys and adding new ones.
     * @return This map after merging.
     */
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

    /**
     * Removes from this map the quantities specified in [mapForRemove], deleting items that become empty.
     * @return This map after removal.
     */
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

/**
 * Returns a new [ResLocale] containing only the items whose keys satisfy [filter].
 */
inline fun ResLocale.filter(filter: ((key: String) -> Boolean)): ResLocale {
    val newResLocale = ResLocale()

    this.forEach { (key, resItem) ->
        if (filter(key)) {
            newResLocale.put(resItem)
        }
    }

    return newResLocale
}
