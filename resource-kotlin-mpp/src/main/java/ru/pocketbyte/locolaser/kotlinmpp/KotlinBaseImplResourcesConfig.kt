package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig

abstract class KotlinBaseImplResourcesConfig : BaseResourcesConfig() {

    companion object {
        const val DEFAULT_PACKAGE = "ru.pocketbyte.locolaser.kmpp"
        const val DEFAULT_INTERFACE_NAME = "StringRepository"
    }

    private var _implements: String? = null
    var implements: String
        get() = _implements ?: defaultInterfaceName
    set(value) { _implements = value }

    open val defaultInterfaceName = "$DEFAULT_PACKAGE.$DEFAULT_INTERFACE_NAME"

    override val defaultTempDirPath = "./build/tmp/"

}
