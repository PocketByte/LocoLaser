package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

abstract class IosClassResourcesConfigBuilder<T : IosBaseClassResourcesConfig>
    : BaseResourcesConfigBuilder<T>() {

    /**
     * Name of the table in iOS bundle. Default value is "Localizable".
     */
    var tableName: String? = null

    /**
     * Name of the class that will be generated.
     */
    override var resourceName: String? = null

    /**
     * Directory where class file should be placed.
     */
    override var resourcesDir: String? = null
}