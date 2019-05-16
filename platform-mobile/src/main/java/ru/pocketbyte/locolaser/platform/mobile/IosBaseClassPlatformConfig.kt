package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig

abstract class IosBaseClassPlatformConfig : BasePlatformConfig() {

    var tableName: String? = null

    override fun getDefaultTempDirPath(): String {
        return "../DerivedData/LocoLaserTemp/"
    }

    override fun getDefaultResourcesPath(): String {
        return "./"
    }

    override fun getDefaultResourceName(): String {
        return "Str"
    }
}
