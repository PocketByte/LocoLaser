package ru.pocketbyte.locolaser.provider

interface NameFormattedStringProvider : StringProvider {
    fun getString(key: String, vararg args: Pair<String, Any>): String
    fun getPluralString(key: String, count: Long, vararg args: Pair<String, Any>): String
}
