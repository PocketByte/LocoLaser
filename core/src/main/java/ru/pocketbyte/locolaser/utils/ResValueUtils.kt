package ru.pocketbyte.locolaser.utils

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.duplicateComments
import ru.pocketbyte.locolaser.resource.entity.ResValue

fun commentShouldBeWritten(value: ResValue, extraParams: ExtraParams?):Boolean {
    return !value.comment.isNullOrBlank() &&
            (extraParams?.duplicateComments != false || value.comment != value.value)
}