package ru.pocketbyte.locolaser.utils

import ru.pocketbyte.locolaser.resource.entity.Quantity

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

    fun quantityIndexForLocale(quantity: Quantity, locale: String): Int? {
        val quantities = quantitiesForLocale(locale) ?: return null
        for (index in 0 until quantities.size) {
            if (quantities[index] == quantity)
                return index
        }
        return null
    }

    fun quantityFromString(string: String?, locale: String): Quantity? {
        if (string == null)
            return null

        return quantityFromString(string)
                ?: quantityFromIndex(string.toIntOrNull(), locale)
    }

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


    fun quantityFromIndex(index: Int?, locale: String): Quantity? {
        if (index == null)
            return null

        val quantities = quantitiesForLocale(locale) ?: return null
        if (index < quantities.size)
            return quantities[index]

        return null
    }

    fun quantityIsSupported(quantity: Quantity, locale: String): Boolean {
        return quantitiesForLocale(locale)?.contains(quantity) ?: true
    }

}