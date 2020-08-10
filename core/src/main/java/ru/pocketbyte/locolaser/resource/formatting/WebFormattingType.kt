package ru.pocketbyte.locolaser.resource.formatting

import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResValue
import ru.pocketbyte.locolaser.resource.entity.merge

object WebFormattingType: FormattingType {

    const val PARAM_TYPE_FORMAT = "Web_TypeFormat"

    private val pattern = "\\{\\{(\\w+)(,\\s*(\\S+))?}}"
            .toRegex(RegexOption.MULTILINE).toPattern()

    override val argumentsSubstitution = FormattingType.ArgumentsSubstitution.BY_NAME

    override fun argumentsFromValue(value: String): List<FormattingArgument>? {
        val matcher = pattern.matcher(value)
        val list = mutableListOf<FormattingArgument>()

        if (matcher.find()) do {
            list.add(FormattingArgument(
                name = matcher.group(1),
                parameters = mapOf(
                    Pair(PARAM_TYPE_FORMAT, matcher.group(3) ?: "")
                )
            ))

        } while (matcher.find())

        return list
    }

    override fun convert(value: ResValue): ResValue {
        val javaValue = when(value.formattingType) {
            JavaFormattingType -> value
            this, NoFormattingType -> return value // Do return function value
            else -> value.formattingType.convertToJava(value)
        }

        val matcher = JavaFormattingType.pattern.matcher(javaValue.value)

        if (matcher.find()) {
            var lastIndex = 0
            val newValueBuilder = StringBuilder()
            val newArguments = mutableListOf<FormattingArgument>()
            var index = 0
            do {
                newValueBuilder.append(value.value.substring(lastIndex, matcher.start() + 1))
                value.formattingArguments?.getOrNull(index)?.let { argument ->
                    val argumentName =  value.nameForFormattingArgument(index)
                    val argumentFormat = formatForArgument(argument)
                    newValueBuilder.append("{{")
                    newValueBuilder.append(argumentName)
                    if (!argumentFormat.isNullOrBlank()) {
                        newValueBuilder.append(", ")
                        newValueBuilder.append(argumentFormat)
                    }
                    newValueBuilder.append("}}")
                    lastIndex = matcher.end()

                    newArguments.add(FormattingArgument(argumentName, argument.index, argument.parameters))
                }
                index++
            } while (matcher.find())

            newValueBuilder.append(value.value.substring(lastIndex, value.value.length))

            return ResValue(
                newValueBuilder.toString(),
                value.comment,
                value.quantity,
                WebFormattingType,
                newArguments,
                value.meta
            )
        } else {
            return ResValue(
                value.value,
                value.comment,
                value.quantity,
                WebFormattingType,
                value.formattingArguments,
                value.meta
            )
        }
    }

    override fun convertToJava(value: ResValue): ResValue {
        return if(value.formattingType == this) {
            val matcher = pattern.matcher(value.value)

            if (matcher.find()) {
                var lastIndex = 0
                val newValueBuilder = StringBuilder()
                val newArguments = mutableListOf<FormattingArgument>()
                val arguments = value.formattingArguments?.iterator()
                do {
                    newValueBuilder.append(value.value.substring(lastIndex, matcher.start()))
                    arguments?.next()?.let { argument ->
                        var javaParameters: Map<String, Any>? = null
                        val javaString = JavaFormattingType.argumentToString(argument) ?: {
                            javaParameters = mapOf(
                                Pair(JavaFormattingType.PARAM_TYPE_NAME, "s")
                            )
                            "%s" // TODO use Web TypeFormat to define Java type
                        }()
                        newValueBuilder.append(javaString)

                        newArguments.add(FormattingArgument(
                                argument.name,
                                argument.index,
                                argument.parameters?.let {
                                    if (javaParameters != null) {
                                        it + javaParameters!!
                                    } else {
                                        it
                                    }
                                } ?: javaParameters))

                        lastIndex = matcher.end()
                    }
                } while (matcher.find())

                newValueBuilder.append(value.value.substring(lastIndex, value.value.length))

                ResValue(
                    newValueBuilder.toString(),
                    value.comment,
                    value.quantity,
                    JavaFormattingType,
                    newArguments,
                    value.meta
                )
            } else {
                ResValue(
                    value.value,
                    value.comment,
                    value.quantity,
                    JavaFormattingType,
                    value.formattingArguments,
                    value.meta
                )
            }
        }
        else  {
            value.formattingType.convertToJava(value)
        }
    }

    private fun formatForArgument(argument: FormattingArgument): String? {
        return argument.parameters?.get(PARAM_TYPE_FORMAT) as? String
    }
}