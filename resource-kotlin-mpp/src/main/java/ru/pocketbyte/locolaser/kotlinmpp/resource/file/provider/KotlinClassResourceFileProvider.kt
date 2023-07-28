package ru.pocketbyte.locolaser.kotlinmpp.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

class KotlinClassResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        val nameParts = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }

        return File(directory, "${nameParts.joinToString(" / ")}.$extension")
    }
}