package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.resource.formatting.nameForFormattingArgument
import java.io.File

open class AbsKeyValuePoetClassResourceFile(
        file: File,
        className: String,
        classPackage: String,
        private val interfaceName: String?,
        private val interfacePackage: String?,
        formattingType: FormattingType = NoFormattingType
): BasePoetClassResourceFile(file, className, classPackage, formattingType) {

    companion object {
        val StringProviderClassName = ClassName("", "StringProvider")
        val KeyValuePairClassName = ParameterizedTypeName.get(Pair::class, String::class, Any::class)
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        val builder = TypeSpec.classBuilder(className)
                .addModifiers(KModifier.PUBLIC)
                .addType(instantiateStringProviderInterfaceSpecBuilder().build())
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter(
                            "private val stringProvider", StringProviderClassName)
                        .build()
                )
        if (interfaceName != null && interfacePackage != null) {
            builder.addSuperinterface(ClassName(interfacePackage, interfaceName))
        }

        return builder
    }

    override fun instantiatePropertySpecBuilder(
            name: String, item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): PropertySpec.Builder {
        val builder = super
            .instantiatePropertySpecBuilder(name, item, resMap, extraParams)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return this.stringProvider.getString(\"${item.key}\")")
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
            name: String, item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val builder = super.instantiateFormattedPropertySpecBuilder(name, item, resMap, extraParams)

        if (interfaceName == null || interfacePackage == null) {
            val valueOther = item.valueForQuantity(Quantity.OTHER)
            if (valueOther?.value != null) {
                builder.addKdoc("%L", wrapCommentString(valueOther.value))
            }
        } else {
            builder.addModifiers(KModifier.OVERRIDE)
        }

        item.valueForQuantity(Quantity.OTHER)?.let { resValue ->
            resValue.formattingArguments
                ?.mapIndexed { index, _ ->
                    val argumentName = resValue.nameForFormattingArgument(index)
                    when (formattingType.argumentsSubstitution) {
                        FormattingType.ArgumentsSubstitution.BY_NAME -> "Pair(\"$argumentName\", $argumentName)"
                        else -> argumentName
                    }
                }
                ?.joinToString()
        }?.let { parametersString ->
            builder.addStatement("return this.stringProvider.getString(\"${item.key}\", $parametersString)")
        }

        return builder
    }

    override fun instantiatePluralSpecBuilder(
            name: String, item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val builder = super
            .instantiatePluralSpecBuilder(name, item, resMap, extraParams)
            .addStatement("return this.stringProvider.getPluralString(\"${item.key}\", count)")

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

    protected fun instantiateStringProviderInterfaceSpecBuilder(): TypeSpec.Builder {
        return TypeSpec.interfaceBuilder(StringProviderClassName)
            .addFunction(
                instantiateStringProviderGetStringSpecBuilder()
                    .addModifiers(KModifier.ABSTRACT)
                    .build()
            )
            .addFunction(
                instantiateStringProviderGetPluralStringSpecBuilder()
                    .addModifiers(KModifier.ABSTRACT)
                    .build()
            )
    }

    protected fun instantiateStringProviderGetStringSpecBuilder(): FunSpec.Builder {
        val builder = FunSpec.builder("getString")
                .addParameter("key", String::class)
                .returns(String::class)

        when (formattingType.argumentsSubstitution) {
            FormattingType.ArgumentsSubstitution.BY_INDEX -> {
                builder.addParameter("vararg args", Any::class)
            }
            FormattingType.ArgumentsSubstitution.BY_NAME -> {
                builder.addParameter("vararg args", KeyValuePairClassName)
            }
            FormattingType.ArgumentsSubstitution.NO -> {
                // Do nothing
            }
        }
        return builder
    }

    protected fun instantiateStringProviderGetPluralStringSpecBuilder(): FunSpec.Builder {
        val builder = FunSpec.builder("getPluralString")
                .addParameter("key", String::class)
                .addParameter("count", Int::class)
                .returns(String::class)

        when (formattingType.argumentsSubstitution) {
            FormattingType.ArgumentsSubstitution.BY_INDEX -> {
                builder.addParameter("vararg args", Any::class)
            }
            FormattingType.ArgumentsSubstitution.BY_NAME -> {
                builder.addParameter("vararg args", KeyValuePairClassName)
            }
            FormattingType.ArgumentsSubstitution.NO -> {
                // Do nothing
            }
        }
        return builder
    }
}