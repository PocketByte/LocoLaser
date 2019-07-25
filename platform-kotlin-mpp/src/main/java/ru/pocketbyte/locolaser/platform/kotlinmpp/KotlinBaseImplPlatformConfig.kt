package ru.pocketbyte.locolaser.platform.kotlinmpp

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig

abstract class KotlinBaseImplPlatformConfig : BasePlatformConfig() {

    var interfaceName: String? = null
        get() = if (field != null) field else defaultInterfaceName

    abstract val defaultInterfaceName: String

    override val defaultTempDirPath = "./build/tmp/"

}
