package ru.pocketbyte.locolaser.provider

import com.ibm.icu.text.PluralRules
import com.ibm.icu.util.ULocale
import ru.pocketbyte.locolaser.entity.Quantity
import java.util.*

private typealias FallbackHandler =
            (bundle: ResourceBundle, locale: Locale, key: String) -> String

/**
 * An [IndexFormattedStringProvider] that loads strings from a JVM [ResourceBundle],
 * using ICU4J for locale-aware plural form selection.
 *
 * Plural key resolution follows this order:
 * 1. `key.{cldrCategory}` (e.g. `key.one`, `key.few`)
 * 2. `key.other`
 * 3. `key` (bare key, as a last resort)
 *
 * @param bundle the resource bundle to load strings from.
 * @param locale the locale used for plural rule selection and string formatting.
 * Defaults to [Locale.getDefault].
 * @param fallbackHandler called when a key is not found in the bundle.
 * By default, throws [java.util.MissingResourceException].
 */
class JvmBundleStringProvider(
    private val bundle: ResourceBundle,
    private val locale: Locale = Locale.getDefault(),
    private val fallbackHandler: FallbackHandler = defaultFallbackHandler
) : IndexFormattedStringProvider {

    companion object {
        private val defaultFallbackHandler: FallbackHandler = { bundle, _, key ->
            throw MissingResourceException(
                "Can't find resource for bundle",
                bundle.javaClass.name,
                key
            )
        }
    }

    /**
     * Creates a provider by loading a [ResourceBundle] from the given class loader.
     *
     * @param resClassLoader the class loader used to locate the bundle.
     * @param bundleName the name of the bundle file (without extension). Defaults to `"strings"`.
     * @param bundlePath the path prefix for the bundle, relative to the class loader root.
     * Defaults to `"strings"`. The bundle is loaded from `"$bundlePath/$bundleName"`.
     * @param locale the locale used for plural rule selection and string formatting.
     * Defaults to [Locale.getDefault].
     * @param fallbackHandler called when a key is not found in the bundle.
     * By default, throws [java.util.MissingResourceException].
     */
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
