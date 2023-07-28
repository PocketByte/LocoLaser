package ru.pocketbyte.locolaser.mobile.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

internal class IosResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        val localeDir = StringBuilder()
        if (Resources.BASE_LOCALE == locale)
            localeDir.append("Base")
        else
            localeDir.append(locale)
        localeDir.append(".lproj")

        return File(File(directory, localeDir.toString()), "$name.$extension")
    }
}