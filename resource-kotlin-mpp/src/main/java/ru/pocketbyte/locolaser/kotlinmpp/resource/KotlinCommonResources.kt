package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.kotlinmpp.resource.file.KotlinCommonResourceFile
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType

import java.io.File

class KotlinCommonResources(
        dir: File,
        name: String,
        filter: ((key: String) -> Boolean)?
) : AbsKotlinResources(dir, name, filter) {

    override val formattingType: FormattingType = JavaFormattingType

    override fun getResourceFiles(locales: Set<String>?): Array<ResourceFile>? {
        return arrayOf(KotlinCommonResourceFile(
                directory, this.className, this.classPackage, formattingType))
    }
}
