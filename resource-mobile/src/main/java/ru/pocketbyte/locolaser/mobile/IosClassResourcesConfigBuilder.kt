package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfigBuilder

/**
 * Abstract builder for iOS class-based code generator configurations (Objective-C and Swift).
 */
abstract class IosClassResourcesConfigBuilder<T : IosBaseClassResourcesConfig>
    : BaseResourcesConfigBuilder<T>() {

    /**
     * Name of the iOS `.strings` table used for localization lookups.
     * If `null`, defaults to `"Localizable"`.
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