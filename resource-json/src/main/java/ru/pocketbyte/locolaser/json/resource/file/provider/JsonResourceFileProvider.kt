package ru.pocketbyte.locolaser.json.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

internal object JsonResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        return File(
            File(directory, if (Resources.BASE_LOCALE == locale) "en/" else "$locale/"),
            "$name.$extension"
        )
    }
}