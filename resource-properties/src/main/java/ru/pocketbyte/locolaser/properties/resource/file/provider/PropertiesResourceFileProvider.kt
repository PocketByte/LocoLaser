package ru.pocketbyte.locolaser.properties.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

internal class PropertiesResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        return File(File(directory, "$locale/"), "$name.$extension")
    }
}
