package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue
import java.util.*
import kotlin.reflect.KClass

/**
 * [FormattingType] implementation for Java-style format strings (e.g., `%1$s`, `%2$d`).
 */
object JavaFormattingType: FormattingType {

    /** Parameter key for the Java format type specifier character (e.g., `"s"`, `"d"`). */
    const val PARAM_TYPE_NAME      = "Java_TypeName"

    /** Parameter key for the Java format flags and width/precision modifiers (e.g., `"-10.2"`). */
    const val PARAM_TYPE_PARAMETERS = "Java_TypeParameters"

    /** Regex pattern that matches Java-style format specifiers in a string. */
    val pattern =
            "(^|[^\\\\])%(([0-9]+)\\\$)?([0-9|+|\\-|.|,|(|#|]*)([a|b|c|d|e|f|g|h|n|o|s|t|x][a-zA-Z]?)"
                    .toRegex(RegexOption.MULTILINE).toPattern()

    override val argumentsSubstitution = FormattingType.ArgumentsSubstitution.BY_INDEX

    override fun argumentsFromValue(value: String): List<FormattingArgument>? {
        val matcher = pattern.matcher(value)
        val list = mutableListOf<FormattingArgument>()

        if (matcher.find()) do {
            list.add(FormattingArgument(
                index = matcher.group(3)?.toIntOrNull(),
                parameters = mapOf(
                    Pair(PARAM_TYPE_NAME, matcher.group(5) ?: ""),
                    Pair(PARAM_TYPE_PARAMETERS, matcher.group(4) ?: "")
                )
            ))

        } while (matcher.find())

        return list
    }

    override fun convert(value: ResValue): ResValue {
        return convertToJava(value)
    }

    override fun convertToJava(value: ResValue): ResValue {
        if (value.formattingType == JavaFormattingType)
            return value

        return value.formattingType.convertToJava(value)
    }

    /**
     * Converts [argument] to its Java format specifier string (e.g., `%1$s`).
     * @return The format specifier string, or null if the argument has no type name.
     */
    fun argumentToString(argument: FormattingArgument): String? {
        val typeName = argument.parameters?.get(PARAM_TYPE_NAME) as? String

        if (typeName.isNullOrBlank()) return null

        val builder = StringBuilder("%")
        argument.index ?.let {
            builder.append(it).append("$")
        }
        (argument.parameters[PARAM_TYPE_PARAMETERS] as? String)?.let {
            builder.append(it)
        }
        builder.append(typeName)
        return builder.toString()
    }

    /**
     * Returns the Kotlin class corresponding to the Java format type specifier [name]
     * (e.g., `"s"` → [String], `"d"` → [Long]), or null if the specifier is unknown or unsupported.
     */
    fun classFromName(name: String?): KClass<*>? {
        return when (name?.getOrNull(0)) {
            'a' -> Double::class
            'b' -> Boolean::class
            'c' -> Char::class
            'd' -> Long::class
            'e' -> Double::class
            'f' -> Double::class
            'g' -> Double::class
            'h' -> Long::class
            'n' -> null
            'o' -> Long::class
            's' -> String::class
            't' -> Date::class
            'x' -> Long::class
            else -> null
        }
    }

    private fun readResolve(): Any = JavaFormattingType
}
