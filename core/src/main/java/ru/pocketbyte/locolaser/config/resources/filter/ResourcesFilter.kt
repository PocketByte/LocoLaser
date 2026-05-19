package ru.pocketbyte.locolaser.config.resources.filter

import java.io.Serializable

/**
 * Functional interface for filtering resources by key.
 */
fun interface ResourcesFilter: Serializable {

    /**
     * Returns true if the resource with the given [key] should be included, false if it should be excluded.
     */
    fun filter(key: String): Boolean
}
