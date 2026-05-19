package ru.pocketbyte.locolaser.provider

/**
 * A composite [StringProvider] that queries multiple providers in order and returns
 * the first result that differs from the key.
 *
 * If none of the providers finds a localized string, the key itself is returned.
 *
 * @param providers the providers to query, in priority order (left to right).
 */
class StringProviderSet(
    private vararg val providers: StringProvider
) : StringProvider {
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
