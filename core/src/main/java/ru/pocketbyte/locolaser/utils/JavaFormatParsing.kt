package ru.pocketbyte.locolaser.utils

import ru.pocketbyte.locolaser.config.FormattingType
import ru.pocketbyte.locolaser.resource.entity.FormatParam
import java.util.*

class JavaFormatParsing {

    companion object {
        private const val FORMAT_PARAM_REGEX =
                "(^|[^\\\\])%(([0-9]+)\\\$)?([0-9|+|\\-|.|,|(|#|]*)([a|b|c|d|e|f|g|h|n|o|s|t|x][a-zA-Z]?)"
    }

    private val pattern = FORMAT_PARAM_REGEX.toRegex(RegexOption.MULTILINE).toPattern()

    fun toFormatParams(string: String): List<FormatParam>? {
        val matcher = pattern.matcher(string)
        val array = mutableListOf<FormatParam>()

        if (matcher.find()) do {
            array.add(FormatParam(
                name = null,
                index = matcher.group(3).toIntOrNull(),
                type = FormattingType.JAVA,
                typeName = matcher.group(5),
                typeArguments = matcher.group(4)
            ))

        } while (matcher.find())

        return array
    }

    private fun classFromName(name: String?): Class<*>? {
        return when (name?.getOrNull(0)) {
            'a' -> Double::class.java
            'b' -> Boolean::class.java
            'c' -> Char::class.java
            'd' -> Long::class.java
            'e' -> Double::class.java
            'f' -> Double::class.java
            'g' -> Double::class.java
            'h' -> Long::class.java
            'n' -> null
            'o' -> Long::class.java
            's' -> String::class.java
            't' -> Date::class.java
            'x' -> Long::class.java
            else -> null
        }
    }
}