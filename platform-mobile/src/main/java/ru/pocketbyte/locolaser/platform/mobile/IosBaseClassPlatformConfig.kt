package ru.pocketbyte.locolaser.platform.mobile

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig

abstract class IosBaseClassPlatformConfig : BasePlatformConfig() {

    var tableName: String? = null

    override val defaultTempDirPath = "../DerivedData/LocoLaserTemp/"
    override val defaultResourcesPath = "./"
    override val defaultResourceName = "Str"

}
