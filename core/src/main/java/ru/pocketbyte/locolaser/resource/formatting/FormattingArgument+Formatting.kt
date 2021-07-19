package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import kotlin.reflect.KClass

fun FormattingArgument.anyName(position: Int): String {
    this.name?.let {
        return it
    }

    val javaName = (this.parameters?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String) ?: "arg"
    val index = this.index ?: (position + 1)
    return javaName + index.toString()
}

fun FormattingArgument.parameterClass(): KClass<*> {
    (parameters?.get(FormattingType.PARAM_CLASS) as? KClass<*>)?.let {
        return it
    }
    (parameters?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String)?.let { paramName ->
        JavaFormattingType.classFromName(paramName)?.let {
            return it
        }
    }
    if (name?.toLowerCase() == "count") {
        return Long::class
    }
    return String::class
}