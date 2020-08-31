package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig

abstract class IosBaseClassResourcesConfig : BaseResourcesConfig() {

    var tableName: String? = null

    override val defaultTempDirPath = "../DerivedData/LocoLaserTemp/"
    override val defaultResourcesPath = "./"
    override val defaultResourceName = "Str"

}
