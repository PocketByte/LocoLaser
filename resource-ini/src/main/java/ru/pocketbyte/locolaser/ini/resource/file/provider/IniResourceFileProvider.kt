package ru.pocketbyte.locolaser.ini.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

internal class IniResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        return File(directory, "$name.$extension")
    }
}