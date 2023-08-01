package ru.pocketbyte.locolaser.provider

import platform.Foundation.NSBundle

class IosStringProvider(
    private val bundle: NSBundle,
    private val tableName: String
) : IndexFormattedStringProvider {

    companion object {
        const val DEFAULT_TABLE_NAME = "Localizable"
    }

    constructor(bundle: NSBundle) : this(bundle, DEFAULT_TABLE_NAME)

    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)

    constructor() : this(NSBundle.mainBundle(), DEFAULT_TABLE_NAME)

    override fun getString(key: String): String {
        return bundle.localizedStringForKey(key, "", this.tableName)
    }

    override fun getString(key: String, vararg args: Any): String {
        return bundle
            .localizedStringForKey(key, "", this.tableName)
            .stringWithFormat(*args)
    }

    override fun getPluralString(key: String, count: Long, vararg args: Any): String {
        return bundle
            .localizedStringForKey(key, "", this.tableName)
            .localizedStringWithFormat(count, *args)
    }
}
