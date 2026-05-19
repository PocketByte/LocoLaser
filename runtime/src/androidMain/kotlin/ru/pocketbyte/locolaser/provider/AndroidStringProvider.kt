package ru.pocketbyte.locolaser.provider

import android.content.Context

/**
 * An [IndexFormattedStringProvider] that loads strings from Android application resources by key name.
 *
 * Resource IDs are resolved via [android.content.res.Resources.getIdentifier] and cached
 * internally to avoid repeated lookups.
 *
 * @param context the application context used to access resources.
 */
class AndroidStringProvider(
    private val context: Context
) : IndexFormattedStringProvider {

    private val resIds = mutableMapOf<String, MutableMap<String, Int>>()

    override fun getString(key: String): String {
        return context.getString(getId(key, "string"))
    }

    override fun getString(key: String, vararg args: Any): String {
        return context.getString(getId(key, "string"), *args)
    }

    override fun getPluralString(key: String, count: Long, vararg args: Any): String {
        return context.resources.getQuantityString(
            getId(key, "plurals"),
            count.toInt(), count, *args
        )
    }

    private fun getId(resName: String, defType: String): Int {
        var resMap = resIds[defType]
        if (resMap == null) {
            resMap = mutableMapOf()
            resIds[defType] = resMap
        }
        var resId = resMap[resName]
        if (resId == null) {
            resId = context.resources.getIdentifier(
                resName.trim { it <= ' ' }, defType, context.packageName
            )
            resMap[resName] = resId
        }
        return resId
    }
}
