package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

class IosClassResourcesConfigBuilder(
    private val config: IosBaseClassResourcesConfig
): BaseResourcesConfigBuilder(config) {

    var tableName: String?
        get() = config.tableName
        set(value) { config.tableName = value }

}