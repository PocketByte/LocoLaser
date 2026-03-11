package ru.pocketbyte.locolaser.config.resources.filter

import java.io.Serializable

fun interface ResourcesFilter: Serializable {
    fun filter(key: String): Boolean
}