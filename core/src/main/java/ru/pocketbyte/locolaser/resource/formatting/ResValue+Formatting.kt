package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.ResValue
import kotlin.reflect.KClass

fun ResValue.nameForFormattingArgument(position: Int): String {
    val argument = formattingArguments?.get(position) ?: throw NullPointerException("formattingArguments is null")
    argument.name?.let {
        return it
    }

    val javaName = (argument.parameters?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String) ?: "arg"

    if(formattingArguments.size == 1 && argument.index == null) {
        return javaName
    }

    val index = argument.index ?: (position + 1)

    return javaName + (index.toString() ?: "")
}

fun ResValue.typeForFormattingArgument(position: Int): KClass<*> {
    val argument = formattingArguments?.get(position) ?: throw NullPointerException("formattingArguments is null")

    return (argument.parameters?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String)?.let {
        JavaFormattingType.classFromName(it)
    } ?: String::class
}