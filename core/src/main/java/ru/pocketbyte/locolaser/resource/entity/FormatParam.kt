package ru.pocketbyte.locolaser.resource.entity

import ru.pocketbyte.locolaser.config.FormattingType

data class FormatParam(
    val type: FormattingType,
    val name: String?,
    val index: Int?,
    val typeName: String?,
    val typeArguments: String?
)

fun FormatParam?.merge(param: FormatParam?): FormatParam? {
    if (this == null) return param
    if (param == null) return this

    if (
        (param.name != null || this.name == null) &&
        (param.index != null || this.index == null) &&
        (param.typeName != null || this.typeName == null) &&
        (param.typeArguments != null || this.typeArguments == null)
    ) {
        return this
    }

    if (
        (param.name == null || param.name == this.name) &&
        (param.index == null || param.index == this.index) &&
        (param.typeName == null || param.typeName == this.typeName) &&
        (param.typeArguments == null || param.typeArguments == this.typeArguments)
    ) {
        return param
    }

    return FormatParam(
        this.type,
        param.name ?: this.name,
        param.index ?: this.index,
        param.typeName ?: this.typeName,
        param.typeArguments ?: this.typeArguments
    )
}