package ru.pocketbyte.locolaser.mobile.resource

import ru.pocketbyte.locolaser.resource.AbsResources

import java.io.File

abstract class IosBaseClassResources(
        resourcesDir: File,
        name: String,
        tableName: String?,
        filter: ((key: String) -> Boolean)?
) : AbsResources(resourcesDir, name, filter) {

    var tableName: String? = null
        get() = field ?: defaultTableName

    protected val defaultTableName: String
        get() = "Localizable"

    init {
        this.tableName = tableName
    }
}
