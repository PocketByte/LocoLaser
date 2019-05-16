package ru.pocketbyte.locolaser.platform.kotlinmobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig

abstract class KotlinBaseImplPlatformConfig : BasePlatformConfig() {

    var interfaceName: String? = null

    override fun getDefaultTempDirPath(): String {
        return "./build/tmp/"
    }

    override fun getDefaultResourcesPath(): String {
        return "./src/main/kotlin/"
    }
}
