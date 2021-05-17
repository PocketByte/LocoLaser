package ru.pocketbyte.locolaser.config.resources

import java.io.File

open class BaseResourcesConfigBuilder(
    private val config: BaseResourcesConfig
) {

    var resourceName: String?
        get() = config.resourceName
        set(value) { config.resourceName = value }

    var resourcesDir: File?
        get() = config.resourcesDir
        set(value) { config.resourcesDir = value }

    fun resourcesDir(path: String) {
        resourcesDir = File(path)
    }

    var filter: ((key: String) -> Boolean)?
        get() = config.filter
        set(value) { config.filter = value }

    fun filter(regExp: String) {
        filter = BaseResourcesConfig.regExFilter(regExp)
    }

}