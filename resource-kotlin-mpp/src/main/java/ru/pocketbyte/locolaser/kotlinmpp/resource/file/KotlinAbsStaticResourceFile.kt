package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.*
import java.io.File

open class KotlinAbsStaticResourceFile(
    file: File,
    className: String,
    classPackage: String,
    private val interfaceName: String?,
    private val interfacePackage: String?,
    formattingType: FormattingType = WebFormattingType
): BasePoetClassResourceFile(file, className, classPackage, formattingType) {

    override fun description(): String {
        return "KotlinAbsStatic(${directory.absolutePath}/${className})"
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        val builder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC)
            .addModifiers(KModifier.ABSTRACT)

        if (interfaceName != null && interfacePackage != null) {
            builder.addSuperinterface(ClassName(interfacePackage, interfaceName))
        }

        builder.addFunction(instantiateGetQuantitySpecBuilder().build())
        builder.addFunction(instantiateFormatStringSpecBuilder().build())

        return builder
    }

    override fun instantiatePropertySpecBuilder(
        name: String, item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): PropertySpec.Builder {
        val builder = super
            .instantiatePropertySpecBuilder(name, item, resMap, extraParams)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement(
                        "return %S",
                        item.valueForQuantity(Quantity.OTHER)?.value ?: item.key
                    )
                    .build()
            )

        if (interfaceName == null || interfacePackage == null) {
            val valueOther = item.valueForQuantity(Quantity.OTHER)
            if (valueOther?.value != null) {
                builder.addKdoc("%L", wrapCommentString(valueOther.value))
            }
        } else {
            builder.addModifiers(KModifier.OVERRIDE)
        }

        return builder
    }

    override fun instantiateFormattedPropertySpecBuilder(
        name: String, formattingArguments: List<FormattingArgument>,
        item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val builder = super.instantiateFormattedPropertySpecBuilder(name, formattingArguments, item, resMap, extraParams)

        if (interfaceName == null || interfacePackage == null) {
            val valueOther = item.valueForQuantity(Quantity.OTHER)
            if (valueOther?.value != null) {
                builder.addKdoc("%L", wrapCommentString(valueOther.value))
            }
        } else {
            builder.addModifiers(KModifier.OVERRIDE)
        }

        val otherValue = formattingType.convert(
            item.valueForQuantity(Quantity.OTHER)
                ?: throw IllegalArgumentException("item must have OTHER quantity")
        ).value

        builder.addStatement(
            StringBuilder("return %S")
                .appendFormatFunction(formattingArguments)
                .toString(),
            otherValue
        )

        return builder
    }

    override fun instantiatePluralSpecBuilder(
        name: String, formattingArguments: List<FormattingArgument>,
        item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val otherValue = item.valueForQuantity(Quantity.OTHER)
            ?: throw IllegalArgumentException("item must have OTHER quantity")
        val builder = super.instantiatePluralSpecBuilder(name, formattingArguments, item, resMap, extraParams)

        if (interfaceName == null || interfacePackage == null) {
            val valueOther = item.valueForQuantity(Quantity.OTHER)
            if (valueOther?.value != null) {
                builder.addKdoc("%L", wrapCommentString(valueOther.value))
            }
        } else {
            builder.addModifiers(KModifier.OVERRIDE)
        }

        val codeBlock = CodeBlock.builder()

        codeBlock.beginControlFlow("val stringValue = when (getQuantity(count))")

        item.values.forEach {
            if (it.quantity != Quantity.OTHER) {
                val formattedValue = formattingType.convert(it)
                codeBlock.addStatement(
                    "Quantity.${it.quantity.name} -> %S",
                    formattedValue.value
                )
            }
        }

        val formattedValue = formattingType.convert(otherValue)
        codeBlock.addStatement("else -> %S", formattedValue.value)
        codeBlock.endControlFlow()

        codeBlock.addStatement(
            StringBuilder("return stringValue")
                .appendFormatFunction(formattingArguments)
                .toString()
        )

        builder.addCode(codeBlock.build())

        return builder
    }

    private fun instantiateGetQuantitySpecBuilder(): FunSpec.Builder {
        return FunSpec.builder("getQuantity")
            .addModifiers(KModifier.PROTECTED)
            .addModifiers(KModifier.ABSTRACT)
            .addParameter("count", Long::class)
            .returns(Quantity::class.asTypeName())
    }

    private fun instantiateFormatStringSpecBuilder(): FunSpec.Builder {
        val builder = FunSpec.builder("formatString")
            .addModifiers(KModifier.PROTECTED)
            .addModifiers(KModifier.ABSTRACT)
            .addParameter("string", String::class)
            .returns(String::class)

        when (formattingType.argumentsSubstitution) {
            FormattingType.ArgumentsSubstitution.BY_INDEX -> {
                builder.addParameter("vararg args", Any::class)
            }
            FormattingType.ArgumentsSubstitution.BY_NAME -> {
                builder.addParameter("vararg args",
                    AbsKeyValuePoetClassResourceFile.KeyValuePairClassName
                )
            }
            FormattingType.ArgumentsSubstitution.NO -> {
                // Do nothing
            }
        }
        return builder
    }

    private fun StringBuilder.appendFormatFunction(formattingArguments: List<FormattingArgument>): StringBuilder {
        append(".let { formatString(it")
        formattingArguments.forEachIndexed { index, argument ->
            append(", ")
            val argumentName = argument.anyName(index)
            when (formattingType.argumentsSubstitution) {
                FormattingType.ArgumentsSubstitution.BY_NAME -> {
                    append("Pair(\"")
                    append(argumentName)
                    append("\", ")
                    append(argumentName)
                    append(")")
                }
                else -> {
                    append(argumentName)
                }
            }
        }
        append(") }")
        return this
    }

}
