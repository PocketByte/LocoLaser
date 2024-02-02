package ru.pocketbyte.locolaser.provider

import i18next.I18n

class JsStringProvider(
    private val i18n: I18n,
    private val pluralKeyPostfix: String = "_plural"
) : NameFormattedStringProvider {

    override fun getString(key: String): String {
        return i18n.t(key)
    }

    override fun getString(key: String, vararg args: Pair<String, Any>): String {
        return i18n.t(key, dynamic(*args))
    }

    override fun getPluralString(key: String, count: Long, vararg args: Pair<String, Any>): String {
        return i18n.t(key + pluralKeyPostfix, dynamic(Pair("count", count), *args))
    }

    private fun dynamic(vararg args: Pair<String, Any>): Any {
        val d: dynamic = object{}
        args.forEach {
            d[it.first] = it.second
        }
        return d as Any
    }
}
