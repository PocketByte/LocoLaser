package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.utils.PluralUtils
import java.util.regex.Matcher
import java.util.regex.Pattern

sealed class KeyPluralizationRule {

    sealed class Postfix : KeyPluralizationRule() {

        companion object {
            private const val DEFAULT_POSTFIX = "_plural"
        }

        abstract val keySeparator: String

        abstract fun decodeKey(key: String, locale: String): Pair<String, Quantity>?
        abstract fun encodeKey(key: String, quantity: Quantity, locale: String): String?

        class Numeric(override val keySeparator: String = DEFAULT_POSTFIX) : Postfix() {
            private val matcher: Matcher by lazy {
                Pattern.compile(
                    "(.+)${keySeparator}_(\\d+)"
                ).matcher("")
            }

            override fun decodeKey(key: String, locale: String): Pair<String, Quantity>? {
                val pluralMatch = matcher.reset(key)
                if (pluralMatch.find() && pluralMatch.groupCount() == 2) {
                    return Pair(
                        pluralMatch.group(1),
                        PluralUtils.quantityFromIndex(
                            pluralMatch.group(2).toIntOrNull(), locale
                        ) ?: Quantity.OTHER
                    )
                }
                if (key.endsWith(keySeparator)) {
                    return Pair(
                        key.substring(0, key.length - keySeparator.length),
                        Quantity.OTHER
                    )
                }
                return null
            }

            override fun encodeKey(key: String, quantity: Quantity, locale: String): String? {
                val index = PluralUtils.quantityIndexForLocale(quantity, locale) ?: return null
                return key + keySeparator + "_" + index
            }
        }

        class Named(override val keySeparator: String = DEFAULT_POSTFIX) : Postfix() {
            private val matcher: Matcher by lazy {
                Pattern.compile(
                    "(.+)${keySeparator}_(${Quantity.values().joinToString("|")})"
                ).matcher("")
            }

            override fun decodeKey(key: String, locale: String): Pair<String, Quantity>? {
                val pluralMatch = matcher.reset(key)
                if (pluralMatch.find() && pluralMatch.groupCount() == 2) {
                    return Pair(
                        pluralMatch.group(1),
                        PluralUtils.quantityFromString(
                            pluralMatch.group(2)
                        ) ?: return null
                    )
                }
                if (key.endsWith(keySeparator)) {
                    return Pair(
                        key.substring(0, key.length - keySeparator.length),
                        Quantity.OTHER
                    )
                }
                return null
            }

            override fun encodeKey(key: String, quantity: Quantity, locale: String): String {
                return key + keySeparator + "_" + quantity.toString()
            }
        }
    }
}