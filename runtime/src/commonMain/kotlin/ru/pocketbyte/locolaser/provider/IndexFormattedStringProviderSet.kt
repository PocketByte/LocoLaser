package ru.pocketbyte.locolaser.provider

class IndexFormattedStringProviderSet(
    private vararg val providers: IndexFormattedStringProvider
) : IndexFormattedStringProvider {
    override fun getPluralString(key: String, count: Long, vararg args: Any): String {
        for (provider in providers) {
            val string = provider.getPluralString(key, count, *args)
            if (string != key) {
                return string
            }
        }
        return key
    }

    override fun getString(key: String, vararg args: Any): String {
        for (provider in providers) {
            val string = provider.getString(key, *args)
            if (string != key) {
                return string
            }
        }
        return key
    }

    override fun getString(key: String): String {
        for (provider in providers) {
            val string = provider.getString(key)
            if (string != key) {
                return string
            }
        }
        return key
    }
}
