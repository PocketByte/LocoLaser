package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.Resources

import java.io.File

abstract class AbsIosStringsResources(
        resourcesDir: File,
        name: String,
        filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, name, filter) {

    companion object {
        const val RES_FILE_EXTENSION = ".strings"
    }

    protected fun getFileForLocale(locale: String): File {
        val localeFolder = File(directory, getLocaleDirName(locale))
        localeFolder.mkdirs()
        return File(localeFolder, name + RES_FILE_EXTENSION)
    }

    protected fun getLocaleDirName(locale: String): String {
        val builder = StringBuilder()
        if (Resources.BASE_LOCALE == locale)
            builder.append("Base")
        else
            builder.append(locale)
        builder.append(".lproj")
        return builder.toString()
    }
}