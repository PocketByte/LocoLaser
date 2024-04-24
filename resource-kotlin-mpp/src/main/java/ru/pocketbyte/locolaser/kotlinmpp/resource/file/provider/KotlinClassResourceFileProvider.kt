package ru.pocketbyte.locolaser.kotlinmpp.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

internal object KotlinClassResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        return File(directory, "${name.replace(".", "/")}.$extension")
    }
}