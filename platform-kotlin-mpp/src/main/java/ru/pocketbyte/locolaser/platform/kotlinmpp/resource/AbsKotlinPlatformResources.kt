package ru.pocketbyte.locolaser.platform.kotlinmpp.resource

import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.summary.FileSummary


import java.io.File

abstract class AbsKotlinPlatformResources(
        dir: File,
        name: String,
        filter: ((key: String) -> Boolean)?
) : AbsPlatformResources(dir, name, filter) {

    companion object {
        const val KOTLIN_FILE_EXTENSION = ".kt"
    }

    val className: String
    val classPackage: String
    val classPackagePath: String

    protected val file: File
        get() {
            val sourceDir = directory
            sourceDir.mkdirs()
            return File(sourceDir, this.classPackagePath + "/" + this.className + KOTLIN_FILE_EXTENSION)
        }

    init {
        val nameParts = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (nameParts.size < 2)
            throw IllegalArgumentException("Invalid resource name")

        this.className = nameParts[nameParts.size - 1]

        val packageBuilder = StringBuilder()
        val packagePathBuilder = StringBuilder()

        for (i in 0 until nameParts.size - 1) {
            if (i != 0) {
                packageBuilder.append(".")
                packagePathBuilder.append("/")
            }

            packageBuilder.append(nameParts[i])
            packagePathBuilder.append(nameParts[i])
        }

        this.classPackage = packageBuilder.toString()
        this.classPackagePath = packagePathBuilder.toString()
    }

    override fun summaryForLocale(locale: String): FileSummary {
        return FileSummary(file)
    }
}
