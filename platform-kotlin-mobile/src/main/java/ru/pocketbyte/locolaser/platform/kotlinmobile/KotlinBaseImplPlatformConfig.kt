package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig

abstract class KotlinBaseImplPlatformConfig : BasePlatformConfig() {

    var interfaceName: String? = null

    override val defaultTempDirPath = "./build/tmp/"
    override val defaultResourcesPath = "./src/main/kotlin/"

}
