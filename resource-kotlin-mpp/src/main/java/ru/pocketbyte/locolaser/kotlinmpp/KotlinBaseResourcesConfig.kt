package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.resource.file.provider.KotlinClassResourceFileProvider

abstract class KotlinBaseResourcesConfig(
    resourceName: String?,
    resourcesDirPath: String?,
    val implements: String?,
    filter: ((key: String) -> Boolean)?
) : BaseResourcesConfig(
    resourceName,
    resourcesDirPath,
    KotlinClassResourceFileProvider(),
    filter
) {

    companion object {
        const val DEFAULT_PACKAGE = "ru.pocketbyte.locolaser.kmpp"
        const val DEFAULT_INTERFACE_NAME = "StringRepository"
    }

    override val defaultTempDirPath = "./build/tmp/"
}
