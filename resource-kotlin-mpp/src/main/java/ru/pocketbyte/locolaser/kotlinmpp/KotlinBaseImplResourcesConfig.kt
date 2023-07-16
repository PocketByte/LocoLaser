package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig

abstract class KotlinBaseImplResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val DEFAULT_PACKAGE = "ru.pocketbyte.locolaser.kmpp"
        const val DEFAULT_INTERFACE_NAME = "StringRepository"
    }

    var implements: String? = null
        get() = if (field != null) field else defaultInterfaceName

    open val defaultInterfaceName = "$DEFAULT_PACKAGE.$DEFAULT_INTERFACE_NAME"

    override val defaultTempDirPath = "./build/tmp/"

}
