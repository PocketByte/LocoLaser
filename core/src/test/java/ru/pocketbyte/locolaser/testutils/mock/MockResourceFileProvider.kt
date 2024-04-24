package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import java.io.File

internal object MockResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        return File(File(directory, locale), "$name.$extension")
    }
}