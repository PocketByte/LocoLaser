package ru.pocketbyte.locolaser.resource.entity

/**
 * Represents a single formatting argument within a localization string value.
 */
data class FormattingArgument(
    /**
     * The argument name for named substitutions (e.g., `"%name$s"`), or null if unnamed.
     */
    val name: String? = null,

    /**
     * The argument index for positional substitutions (e.g., `"%1$s"`), or null if unindexed.
     */
    val index: Int? = null,

    /**
     * Additional formatting parameters (e.g., type name), or null if none.
     */
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

/**
 * Merges this [FormattingArgument] with [argument], combining their properties.
 * Properties from [argument] take precedence unless null.
 * @param argument The argument to merge with.
 * @return The merged [FormattingArgument], or null if both are null.
 */
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
