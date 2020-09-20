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
    return (parameters?.get(FormattingType.PARAM_CLASS) as? KClass<*>) ?:
           (parameters?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String)?.let {
                JavaFormattingType.classFromName(it)
            } ?: String::class
}