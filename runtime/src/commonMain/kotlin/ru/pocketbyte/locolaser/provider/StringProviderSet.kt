package ru.pocketbyte.locolaser.provider

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
