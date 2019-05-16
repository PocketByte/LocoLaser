package ru.pocketbyte.locolaser.platform.mobile.resource

import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import ru.pocketbyte.locolaser.resource.PlatformResources

import java.io.File

abstract class AbsIosStringsResources(
        resourcesDir: File,
        name: String
) : AbsPlatformResources(resourcesDir, name) {

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
        if (PlatformResources.BASE_LOCALE == locale)
            builder.append("Base")
        else
            builder.append(locale)
        builder.append(".lproj")
        return builder.toString()
    }
}