package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue

/**
 * A [FormattingType] that represents a value without formatting.
 * Arguments are always null. Conversion always returns the same object.
 */
object NoFormattingType: FormattingType {

    override val argumentsSubstitution = FormattingType.ArgumentsSubstitution.NO

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

    private fun readResolve(): Any = NoFormattingType
}
