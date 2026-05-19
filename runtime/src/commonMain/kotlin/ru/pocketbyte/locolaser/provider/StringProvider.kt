package ru.pocketbyte.locolaser.provider

/**
 * Provides localized strings by key.
 *
 * Implementations must return the key itself if no localized string is found for it.
 */
interface StringProvider {

    /**
     * Returns the localized string for the given [key], or [key] itself if no string is found.
     *
     * @param key the string resource key.
     * @return the localized string, or [key] if not found.
     */
    fun getString(key: String): String
}
