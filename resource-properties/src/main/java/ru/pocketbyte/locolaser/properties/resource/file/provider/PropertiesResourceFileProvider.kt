package ru.pocketbyte.locolaser.properties.resource.file.provider

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.Resources
import java.io.File

internal object PropertiesResourceFileProvider : ResourceFileProvider {
    override fun get(locale: String, directory: File, name: String, extension: String): File {
        return if (locale == Resources.BASE_LOCALE) {
            File(directory, "$name.$extension")
        } else {
            File(directory, "${name}_$locale.$extension")
        }
    }
}
