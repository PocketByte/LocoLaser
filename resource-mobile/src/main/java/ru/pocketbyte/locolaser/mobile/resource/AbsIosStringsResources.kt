package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

abstract class AbsIosStringsResources(
    resourcesDir: File,
    name: String,
    resourceFileProvider: ResourceFileProvider,
    filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, name, resourceFileProvider, filter) {

    override val formattingType: FormattingType = JavaFormattingType
    override val fileExtension: String = "strings"

}
