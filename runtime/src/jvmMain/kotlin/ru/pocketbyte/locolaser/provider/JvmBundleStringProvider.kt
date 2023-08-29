package ru.pocketbyte.locolaser.provider

import com.ibm.icu.text.PluralRules
import com.ibm.icu.util.ULocale
import ru.pocketbyte.locolaser.entity.Quantity
import java.util.*

private typealias FallbackHandler =
            (bundle: ResourceBundle, locale: Locale, key: String) -> String

class JvmBundleStringProvider(
    private val bundle: ResourceBundle,
    private val locale: Locale = Locale.getDefault(),
    private val fallbackHandler: FallbackHandler = defaultFallbackHandler
) : IndexFormattedStringProvider {

    companion object {
        private val defaultFallbackHandler: FallbackHandler = { bundle, locale, key ->
            throw MissingResourceException(
                "Can't find resource for bundle",
                bundle.javaClass.name,
                key
            )
        }
    }

    constructor(
        resClassLoader: ClassLoader,
        bundleName: String = "strings",
        bundlePath: String = "strings",
        locale: Locale = Locale.getDefault(),
        fallbackHandler: FallbackHandler = defaultFallbackHandler
    ) : this (
        ResourceBundle.getBundle(
            "$bundlePath/$bundleName", locale, resClassLoader,
            ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT)
        ),
        locale, fallbackHandler
    )

    override fun getPluralString(key: String, count: Long, vararg args: Any): String {
        val quantityName = PluralRules
            .forLocale(ULocale.forLocale(locale))
            .select(count.toDouble())

        val pluralKey = "$key.$quantityName"
        if (bundle.containsKey(pluralKey)) {
            return getString(pluralKey, count, *args)
        }

        val otherKey = "$key.${Quantity.OTHER.name.lowercase()}"
        if (bundle.containsKey(otherKey)) {
            return getString(otherKey, count, *args)
        }

        return getString(key, count, *args)
    }

    override fun getString(key: String, vararg args: Any): String {
        return String.format(locale = locale, getString(key), *args)
    }

    override fun getString(key: String): String {
        if (bundle.containsKey(key)) {
            return bundle.getString(key)
        }
        return fallbackHandler(bundle, locale, key)
    }

}