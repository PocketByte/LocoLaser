package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue

interface FormattingType {

    enum class ArgumentsSubstitution {
        /**
         * No substitution. Any formatting functionality will be omitted.
         */
        NO,

        /**
         * Arguments substitution by index. Typical for formatting used in Java.
         * For example: "Hello %1$s. My name is %1$s"
         */
        BY_INDEX,

        /**
         * Arguments substitution by name. Typical for formatting used in Web.
         * For example: "Hello {{user_name}}. My name is {{admin_name}}"
         */
        BY_NAME
    }

    companion object {
        const val PARAM_CLASS = "class"
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