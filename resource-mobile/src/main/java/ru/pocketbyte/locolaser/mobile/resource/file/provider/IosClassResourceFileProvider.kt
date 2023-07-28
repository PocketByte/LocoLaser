package ru.pocketbyte.locolaser.mobile.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

internal class IosClassResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        return File(directory, "$name.$extension")
    }
}