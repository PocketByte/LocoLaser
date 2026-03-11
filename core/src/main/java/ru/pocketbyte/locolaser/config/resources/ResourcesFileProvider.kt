package ru.pocketbyte.locolaser.config.resources

import java.io.File
import java.io.Serializable

fun interface ResourceFileProvider: Serializable {
    fun get(locale: String, directory: File, name: String, extension: String): File
}
