package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import kotlin.reflect.KClass

/**
 * Returns the name of this argument, falling back to a generated name if [FormattingArgument.name] is not set.
 *
 * @param position The 0-based position of this argument in the argument list.
 */
fun FormattingArgument.anyName(position: Int): String {
    this.name?.let {
        return it
    }

    val javaName = (this.parameters?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String) ?: "arg"
    val index = this.index ?: (position + 1)
    return javaName + index.toString()
}

/**
 * Returns the Kotlin class that represents the type of this argument.
 * Defaults to [String] if the type cannot be determined.
 */
fun FormattingArgument.parameterClass(): KClass<*> {
    (parameters?.get(FormattingType.PARAM_CLASS) as? KClass<*>)?.let {
        return it
    }
    (parameters?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String)?.let { paramName ->
        JavaFormattingType.classFromName(paramName)?.let {
            return it
        }
    }
    if (name?.lowercase() == "count") {
        return Long::class
    }
    return String::class
}
