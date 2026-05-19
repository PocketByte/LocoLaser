package ru.pocketbyte.locolaser.utils

import ru.pocketbyte.locolaser.entity.Quantity

/**
 * Utility object for working with CLDR plural quantities and locale-specific plural rules.
 */
object PluralUtils {

    private val languageQuantitiesMap = mutableMapOf<String, List<Quantity>>()

    init {
        // Taken from https://www.unicode.org/cldr/charts/33/supplemental/language_plural_rules.html
        var quantities = listOf(Quantity.ONE, Quantity.OTHER)
        arrayOf("base", "af", "ak", "sq", "am", "hy", "as", "ast", "asa", "az", "bn", "eu", "bem", "bez", "brx", "bg",
                "ca", "tzm", "ckb", "ce", "chr", "cgg", "da", "dv", "nl", "en", "eo", "et", "ee", "fo", "fil",
                "tl", "fi", "fr", "fur", "ff", "gl", "lg", "ka", "de", "el", "gu", "guw", "ha", "haw", "hi", "hu",
                "is", "io", "it", "kaj", "kab", "kkj", "kl", "kn", "ks", "kk", "ku", "ky", "ln", "lb", "mk", "jmc",
                "mg", "ml", "mr", "mas", "mgo", "mn", "nah", "ne", "nnh", "jgo", "nd", "nso", "nb", "no", "nn",
                "ny", "nyn", "or", "om", "os", "pap", "ps", "fa", "pt", "pa", "rm", "rof", "rwk", "ssy", "saq",
                "seh", "ksb", "sn", "scn", "sd", "si", "xog", "so", "nr", "sdh", "st", "es", "sw", "ss", "sv",
                "gsw", "syr", "ta", "te", "teo", "tig", "ti", "ts", "tn", "tr", "tk", "kcg", "ur", "ug", "uz",
                "ve", "vo", "vun", "wa", "wae", "fy", "xh", "ji", "zu").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf(Quantity.ZERO, Quantity.ONE, Quantity.TWO, Quantity.FEW, Quantity.MANY, Quantity.OTHER)
        arrayOf("ar", "ars", "cy").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf(Quantity.OTHER)
        arrayOf("bm", "my", "yue", "zh", "dz", "ig", "id", "ja", "jv", "kea", "km", "ko", "ses", "lkt", "lo",
                "jbo", "kde", "ms", "nqo", "sah", "sg", "ii", "th", "bo", "to", "vi", "wo", "yo").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf(Quantity.ONE, Quantity.FEW, Quantity.MANY, Quantity.OTHER)
        arrayOf("be", "cs", "lt", "mt", "pl", "ru", "sk", "uk").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf(Quantity.ONE, Quantity.FEW, Quantity.OTHER)
        arrayOf("bs", "hr", "ro", "sr", "shi").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf( Quantity.ONE, Quantity.TWO, Quantity.FEW, Quantity.MANY, Quantity.OTHER)
        arrayOf("br", "ga", "gv").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf(Quantity.ZERO, Quantity.ONE, Quantity.OTHER)
        arrayOf("ksh", "lag", "lv", "prg").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf(Quantity.ONE, Quantity.TWO, Quantity.OTHER)
        arrayOf("kw", "smn", "iu", "smj", "naq", "se", "smi", "sms", "sma").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf(Quantity.ONE, Quantity.TWO, Quantity.MANY, Quantity.OTHER)
        arrayOf("he").forEach {
            languageQuantitiesMap[it] = quantities
        }

        quantities = listOf(Quantity.ONE, Quantity.TWO, Quantity.FEW, Quantity.OTHER)
        arrayOf("dsb", "gd", "sl", "hsb").forEach {
            languageQuantitiesMap[it] = quantities
        }
    }

    /**
     * Returns the list of plural quantities supported by [locale],
     * or null if the locale is unknown.
     * If an exact match is not found, strips the region suffix and retries (e.g., `"en_US"` → `"en"`).
     */
    fun quantitiesForLocale(locale: String): List<Quantity>? {
        val result = languageQuantitiesMap[locale]
        if (result != null)
            return result

        val sepIndex = locale.lastIndexOf('_')
        if (sepIndex > 0) {
            return quantitiesForLocale(locale.substring(0, sepIndex))
        }

        return null
    }

    /**
     * Returns the index of [quantity] in the plural quantity list for [locale],
     * or null if the locale is unknown or the quantity is not in the list.
     */
    fun quantityIndexForLocale(quantity: Quantity, locale: String): Int? {
        val quantities = quantitiesForLocale(locale) ?: return null
        for (index in quantities.indices) {
            if (quantities[index] == quantity)
                return index
        }
        return null
    }

    /**
     * Parses [string] to a [Quantity], first by CLDR name (e.g., `"one"`, `"few"`),
     * then by numeric index within the quantity list for [locale].
     * Returns null if [string] is null or cannot be resolved.
     */
    fun quantityFromString(string: String?, locale: String): Quantity? {
        if (string == null)
            return null

        return quantityFromString(string)
                ?: quantityFromIndex(string.toIntOrNull(), locale)
    }

    /**
     * Parses [string] to a [Quantity] by CLDR name (e.g., `"zero"`, `"one"`, `"other"`).
     * Returns null if [string] is null or does not match any known quantity name.
     */
    fun quantityFromString(string: String?): Quantity? {
        return when (string?.trim { it <= ' ' }) {
            "zero" -> Quantity.ZERO
            "one" -> Quantity.ONE
            "two" -> Quantity.TWO
            "few" -> Quantity.FEW
            "many" -> Quantity.MANY
            "other" -> Quantity.OTHER
            else -> null
        }
    }


    /**
     * Returns the [Quantity] at position [index] in the plural quantity list for [locale],
     * or null if [index] is null, the locale is unknown, or the index is out of range.
     */
    fun quantityFromIndex(index: Int?, locale: String): Quantity? {
        if (index == null)
            return null

        val quantities = quantitiesForLocale(locale) ?: return null
        if (index < quantities.size)
            return quantities[index]

        return null
    }

    /**
     * Returns true if [quantity] is supported by [locale] or the locale is unknown.
     */
    fun quantityIsSupported(quantity: Quantity, locale: String): Boolean {
        return quantitiesForLocale(locale)?.contains(quantity) ?: true
    }
}
