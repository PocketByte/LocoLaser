package ru.pocketbyte.locolaser.provider

/**
 * A [StringProvider] that supports positional format arguments (e.g. `%1$s`, `%2$d`)
 * and plural string selection.
 *
 * Extends [StringProvider] with overloads that accept format arguments and a count
 * for choosing the correct plural form according to the current locale.
 */
interface IndexFormattedStringProvider : StringProvider {

    /**
     * Returns the localized string for the given [key], formatted with positional [args],
     * or [key] itself if no string is found.
     *
     * @param key the string resource key.
     * @param args positional format arguments applied to the localized string.
     * @return the formatted localized string, or [key] if not found.
     */
    fun getString(key: String, vararg args: Any): String

    /**
     * Returns the localized plural string for the given [key] and [count], formatted
     * with positional [args], or [key] itself if no string is found.
     *
     * @param key the string resource key.
     * @param count the number that determines which plural form to use.
     * @param args positional format arguments applied to the localized string.
     * @return the formatted localized plural string, or [key] if not found.
     */
    fun getPluralString(key: String, count: Long, vararg args: Any): String
}
