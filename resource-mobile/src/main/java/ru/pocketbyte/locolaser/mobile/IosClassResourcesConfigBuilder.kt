package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder
import java.io.File

open class IosClassResourcesConfigBuilder<T : IosBaseClassResourcesConfig>(
    config: T
): BaseResourcesConfigBuilder<T>(config) {

    /**
     * Name of the table in iOS bundle. Default value is "Localizable".
     */
    var tableName: String?
        get() = config.tableName
        set(value) { config.tableName = value }

    /**
     * Name of the class that will be generated.
     */
    override var resourceName: String
        get() = super.resourceName
        set(value) { super.resourceName = value }

    /**
     * Directory where class file should be placed.
     */
    override var resourcesDir: String?
        get() = super.resourcesDir
        set(value) { super.resourcesDir = value }
}