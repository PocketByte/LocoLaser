package ru.pocketbyte.locolaser.provider

import platform.Foundation.NSBundle

class IosStringProvider(
    private val bundle: NSBundle,
    private val tableName: String
) : StringProvider {

    companion object {
        const val DEFAULT_TABLE_NAME = "Localizable"
    }

    constructor(bundle: NSBundle) : this(bundle, DEFAULT_TABLE_NAME)

    constructor(tableName: String) : this(NSBundle.mainBundle(), tableName)

    constructor() : this(NSBundle.mainBundle(), DEFAULT_TABLE_NAME)

    override fun getString(key: String): String {
        return bundle.localizedStringForKey(key, "", this.tableName)
    }
}
