package ru.pocketbyte.locolaser.provider

import platform.Foundation.NSBundle

/**
 * A [StringProvider] for Apple platforms that loads localized strings from `.strings` files
 * via [NSBundle.localizedStringForKey].
 *
 * @param bundle the bundle containing the `.strings` files.
 * @param tableName the name of the `.strings` file to look up (without the `.strings` extension).
 */
class IosStringProvider(
    private val bundle: NSBundle,
    private val tableName: String
) : StringProvider {

    companion object {
        /** The default `.strings` table name used in Apple's localization ecosystem. */
        const val DEFAULT_TABLE_NAME = "Localizable"
    }

    /** Creates a provider using [bundle] and the [DEFAULT_TABLE_NAME] table. */
    constructor(bundle: NSBundle) : this(bundle, DEFAULT_TABLE_NAME)

    /** Creates a provider using the app's main bundle and the given [tableName]. */
    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)

    /** Creates a provider using the app's main bundle and the [DEFAULT_TABLE_NAME] table. */
    constructor() : this(NSBundle.mainBundle(), DEFAULT_TABLE_NAME)

    override fun getString(key: String): String {
        return bundle.localizedStringForKey(key, "", this.tableName)
    }
}
