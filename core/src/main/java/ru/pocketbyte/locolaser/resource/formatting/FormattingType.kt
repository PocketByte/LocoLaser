package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue
import java.io.Serializable

/**
 * Defines how formatting arguments are represented and converted in localization values.
 */
interface FormattingType: Serializable {

    /**
     * Describes how formatting arguments are substituted into a string.
     */
    enum class ArgumentsSubstitution {
        /**
         * No substitution. Any formatting functionality will be omitted.
         */
        NO,

        /**
         * Arguments substitution by index. Typical for formatting used in Java.
         * For example: "Hello %1$s. My name is %2$s"
         */
        BY_INDEX,

        /**
         * Arguments substitution by name. Typical for formatting used in Web.
         * For example: "Hello {{user_name}}. My name is {{admin_name}}"
         */
        BY_NAME
    }

    companion object {
        /** Parameter key for storing the Kotlin class of a formatting argument. */
        const val PARAM_CLASS = "class"
    }

    /** The substitution strategy used by this formatting type. */
    val argumentsSubstitution: ArgumentsSubstitution

    /**
     * Parses and returns the list of formatting arguments found in [value],
     * or null if the value contains no formatting arguments.
     *
     * @param value The raw localization string to parse.
     */
    fun argumentsFromValue(value: String): List<FormattingArgument>?

    /**
     * Converts the formatting arguments in [value] to this formatting type.
     *
     * @param value The value to convert.
     */
    fun convert(value: ResValue): ResValue

    /**
     * Converts the formatting arguments in [value] to [JavaFormattingType].
     *
     * @param value The value to convert.
     */
    fun convertToJava(value: ResValue): ResValue

}
