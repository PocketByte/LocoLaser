package ru.pocketbyte.locolaser.utils.json

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue
import ru.pocketbyte.locolaser.resource.formatting.FormattingType

class CustomFormattingType: FormattingType {
    override val argumentsSubstitution: FormattingType.ArgumentsSubstitution
        get() = throw RuntimeException()

    override fun argumentsFromValue(value: String): List<FormattingArgument>? {
        throw RuntimeException()
    }

    override fun convert(value: ResValue): ResValue {
        throw RuntimeException()
    }

    override fun convertToJava(value: ResValue): ResValue {
        throw RuntimeException()
    }
}