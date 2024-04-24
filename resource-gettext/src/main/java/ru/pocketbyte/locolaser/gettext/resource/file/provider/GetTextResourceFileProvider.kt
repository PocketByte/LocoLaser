package ru.pocketbyte.locolaser.gettext.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

internal object GetTextResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        return File(File(directory, "$locale/LC_MESSAGES/"), "$name.$extension")
    }
}