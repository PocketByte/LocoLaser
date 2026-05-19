package ru.pocketbyte.locolaser.json

import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.utils.PluralUtils
import java.io.Serializable
import java.util.regex.Matcher
import java.util.regex.Pattern

/** Defines how plural string keys are structured in JSON resource files. */
sealed class KeyPluralizationRule: Serializable {

    /** A [KeyPluralizationRule] that encodes plural forms by appending a postfix to the base key. */
    sealed class Postfix : KeyPluralizationRule() {

        companion object {
            private const val DEFAULT_POSTFIX = "_plural"
        }

        /** The string appended between the base key and the plural postfix. */
        abstract val keySeparator: String

        /**
         * Decodes a raw JSON key into a base key and its plural [Quantity].
         * Returns `null` if the key does not match this rule.
         */
        abstract fun decodeKey(key: String, locale: String): Pair<String, Quantity>?

        /**
         * Encodes a base key and plural [Quantity] into a JSON key.
         * Returns `null` if the quantity is not applicable for the given locale.
         */
        abstract fun encodeKey(key: String, quantity: Quantity, locale: String): String?

        /**
         * A [Postfix] rule that appends a numeric index (e.g. `key_plural_0`, `key_plural_1`).
         * @param keySeparator The string appended between the base key and the plural postfix.
         */
        class Numeric(
            override val keySeparator: String = DEFAULT_POSTFIX
        ) : Postfix() {

            @Transient
            private var matcher: Matcher? = null

            override fun decodeKey(key: String, locale: String): Pair<String, Quantity>? {
                val pluralMatch = getOrCreateMatcher().reset(key)
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

            private fun getOrCreateMatcher(): Matcher {
                matcher?.let { return it }
                return Pattern
                    .compile("(.+)${keySeparator}_(\\d+)")
                    .matcher("")
                    .apply { matcher = this }
            }
        }

        /**
         * A [Postfix] rule that appends a quantity name (e.g. `key_plural_one`, `key_plural_other`).
         * @param keySeparator The string appended between the base key and the plural postfix.
         */
        class Named(
            override val keySeparator: String = DEFAULT_POSTFIX
        ) : Postfix() {

            @Transient
            private var matcher: Matcher? = null

            override fun decodeKey(key: String, locale: String): Pair<String, Quantity>? {
                val pluralMatch = getOrCreateMatcher().reset(key)
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

            private fun getOrCreateMatcher(): Matcher {
                matcher?.let { return it }
                return Pattern
                    .compile("(.+)${keySeparator}_(${Quantity.values().joinToString("|")})")
                    .matcher("").apply { matcher = this }
            }
        }
    }
}
