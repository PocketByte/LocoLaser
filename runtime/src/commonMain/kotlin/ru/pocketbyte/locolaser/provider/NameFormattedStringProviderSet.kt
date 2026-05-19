package ru.pocketbyte.locolaser.provider

/**
 * A composite [NameFormattedStringProvider] that queries multiple providers in order and returns
 * the first result that differs from the key.
 *
 * If none of the providers finds a localized string, the key itself is returned.
 *
 * @param providers the providers to query, in priority order (left to right).
 */
class NameFormattedStringProviderSet(
    private vararg val providers: NameFormattedStringProvider
) : NameFormattedStringProvider {
    override fun getPluralString(key: String, count: Long, vararg args: Pair<String, Any>): String {
        for (provider in providers) {
            val string = provider.getPluralString(key, count, *args)
            if (string != key) {
                return string
            }
        }
        return key
    }

    override fun getString(key: String, vararg args: Pair<String, Any>): String {
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
