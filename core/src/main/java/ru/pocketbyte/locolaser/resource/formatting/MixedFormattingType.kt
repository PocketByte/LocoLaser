package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue

object MixedFormattingType: FormattingType {

    override val argumentsSubstitution = FormattingType.ArgumentsSubstitution.NO

    override fun argumentsFromValue(value: String): List<FormattingArgument>? {
        throw UnsupportedOperationException(
            "${MixedFormattingType::class.java.canonicalName} doesn't support value arguments parsing."
        )
    }

    override fun convert(value: ResValue): ResValue {
        throw UnsupportedOperationException(
            "${MixedFormattingType::class.java.canonicalName} doesn't support value conversion."
        )
    }

    override fun convertToJava(value: ResValue): ResValue {
        throw UnsupportedOperationException(
                "${MixedFormattingType::class.java.canonicalName} doesn't support value conversion."
        )
    }
}