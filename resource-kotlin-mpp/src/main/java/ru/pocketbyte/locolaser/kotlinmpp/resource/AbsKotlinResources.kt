package ru.pocketbyte.locolaser.kotlinmpp.resource


import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.AbsResources
import java.io.File

abstract class AbsKotlinResources(
    dir: File,
    name: String,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : AbsResources(dir, name, resourceFileProvider, filter) {

    companion object {
        const val KOTLIN_FILE_EXTENSION = "kt"
    }

    val className: String
    val classPackage: String

    override val fileExtension: String = KOTLIN_FILE_EXTENSION

    init {
        val lastSegmentIndex = name.lastIndexOf(".")
        if (lastSegmentIndex <= 0)
            throw IllegalArgumentException("Invalid resource name: $name")

        this.className = name.substring(lastSegmentIndex + 1)
        this.classPackage = name.substring(0, lastSegmentIndex)
    }
}
