package ru.pocketbyte.locolaser.provider

/**
 * A [StringProvider] that supports named format arguments and plural string selection.
 *
 * Arguments are passed as `name → value` pairs, suitable for template engines that
 * reference placeholders by name (e.g. `{{name}}`).
 *
 * Extends [StringProvider] with overloads that accept named arguments and a count
 * for choosing the correct plural form according to the current locale.
 */
interface NameFormattedStringProvider : StringProvider {

    /**
     * Returns the localized string for the given [key], formatted with named [args],
     * or [key] itself if no string is found.
     *
     * @param key the string resource key.
     * @param args named format arguments as `name → value` pairs.
     * @return the formatted localized string, or [key] if not found.
     */
    fun getString(key: String, vararg args: Pair<String, Any>): String

    /**
     * Returns the localized plural string for the given [key] and [count], formatted
     * with named [args], or [key] itself if no string is found.
     *
     * @param key the string resource key.
     * @param count the number that determines which plural form to use.
     * @param args named format arguments as `name → value` pairs.
     * @return the formatted localized plural string, or [key] if not found.
     */
    fun getPluralString(key: String, count: Long, vararg args: Pair<String, Any>): String
}
