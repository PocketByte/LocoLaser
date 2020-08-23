package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig

abstract class KotlinBaseImplResourcesConfig : BaseResourcesConfig() {

    var interfaceName: String? = null
        get() = if (field != null) field else defaultInterfaceName

    abstract val defaultInterfaceName: String

    override val defaultTempDirPath = "./build/tmp/"

}
