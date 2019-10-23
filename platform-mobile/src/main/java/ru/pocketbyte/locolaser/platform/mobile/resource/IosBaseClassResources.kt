package ru.pocketbyte.locolaser.platform.mobile.resource

import ru.pocketbyte.locolaser.resource.AbsPlatformResources

import java.io.File

abstract class IosBaseClassResources(
        resourcesDir: File,
        name: String,
        tableName: String?,
        filter: ((key: String) -> Boolean)?
) : AbsPlatformResources(resourcesDir, name, filter) {

    var tableName: String? = null
        get() = field ?: defaultTableName

    protected val defaultTableName: String
        get() = "Localizable"

    init {
        this.tableName = tableName
    }
}
