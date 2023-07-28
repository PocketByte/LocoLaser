package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

import java.io.File

abstract class IosBaseClassResources(
    resourcesDir: File,
    name: String,
    resourceFileProvider: ResourceFileProvider,
    tableName: String?,
    filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, name, resourceFileProvider, filter) {

    override val formattingType: FormattingType = NoFormattingType

    var tableName: String = tableName ?: defaultTableName

    protected val defaultTableName: String
        get() = "Localizable"

}
