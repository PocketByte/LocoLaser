package ru.pocketbyte.locolaser.provider

interface IndexFormattedStringProvider : StringProvider {
    fun getString(key: String, vararg args: Any): String
    fun getPluralString(key: String, count: Long, vararg args: Any): String
}
