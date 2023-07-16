package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue
import java.util.*
import kotlin.reflect.KClass

object JavaFormattingType: FormattingType {

    const val PARAM_TYPE_NAME      = "Java_TypeName"
    const val PARAM_TYPE_PARAMETERS = "Java_TypeParameters"

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

    public fun argumentToString(argument: FormattingArgument): String? {
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

}