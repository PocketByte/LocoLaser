package ru.pocketbyte.locolaser.platform.ini.resource

import ru.pocketbyte.locolaser.platform.ini.resource.file.IniResourceFile
import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.summary.FileSummary

import java.io.File

class IniResources(resourcesDir: File, fileName: String) : AbsPlatformResources(resourcesDir, fileName) {

    private val resourceFile: File
        get() {
            val localeFolder = File(directory, "/")
            localeFolder.mkdirs()
            return File(localeFolder, "$name.ini")
        }

    override fun getResourceFiles(locales: Set<String>): Array<ResourceFile>? {
        return arrayOf(IniResourceFile(resourceFile, locales))
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(resourceFile)
    }
}
