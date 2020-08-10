package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue

interface FormattingType {

    enum class ArgumentsSubstitution {
        NO,
        BY_INDEX,
        BY_NAME
    }

    val argumentsSubstitution: ArgumentsSubstitution

    fun argumentsFromValue(value: String): List<FormattingArgument>?

    /**
     * Converts provided value to current Formatting type.
     *
     * @param value Value that should be converted
     */
    fun convert(value: ResValue): ResValue

    /**
     * Converts provided value to Java Formatting type.
     *
     * @param value Value that should be converted
     */
    fun convertToJava(value: ResValue): ResValue

}