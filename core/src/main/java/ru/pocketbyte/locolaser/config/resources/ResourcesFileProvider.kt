package ru.pocketbyte.locolaser.config.resources

import java.io.File

fun interface ResourceFileProvider {
    fun get(locale: String, directory: File, name: String, extension: String): File
}
