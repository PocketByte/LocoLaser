package ru.pocketbyte.locolaser.resource.entity

data class FormattingArgument(
    val name: String? = null,
    val index: Int? = null,
    val parameters: Map<String, Any>? = null
) {
    override fun equals(other: Any?): Boolean {
        return if (other is FormattingArgument) {
            name == other.name &&
            index == other.index &&
            parameters == other.parameters
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (index ?: 0)
        result = 31 * result + (parameters?.hashCode() ?: 0)
        return result
    }
}

fun FormattingArgument?.merge(argument: FormattingArgument?): FormattingArgument? {
    if (this == null) return argument
    if (argument == null) return this

    if (
        (argument.name != null || this.name == null) &&
        (argument.index != null || this.index == null) &&
        (this.parameters?.isEmpty() != false)
    ) {
        return argument
    }

    if (
        (argument.name == null || argument.name == this.name) &&
        (argument.index == null || argument.index == this.index) &&
        (argument.parameters?.isEmpty() != false || argument.parameters == this.parameters)
    ) {
        return this
    }

    return FormattingArgument(
        argument.name ?: this.name,
        argument.index ?: this.index,
        this.parameters?.let {
            if (argument.parameters != null) {
                it + argument.parameters
            } else {
                it
            }
        } ?: argument.parameters
    )
}