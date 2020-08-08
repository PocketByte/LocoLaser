package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue

/**
 * Represents value without formatting.
 * Arguments for this format always null.
 * Conversion always returns same object.
 */
object NoFormattingType: FormattingType {

    override fun argumentsFromValue(value: String): List<FormattingArgument>? {
        return null
    }

    override fun convert(value: ResValue): ResValue {
        return value
    }

    override fun convertToJava(value: ResValue): ResValue {
        return if (value.formattingType == this) {
            value
        } else {
            value.formattingType.convertToJava(value)
        }
    }
}