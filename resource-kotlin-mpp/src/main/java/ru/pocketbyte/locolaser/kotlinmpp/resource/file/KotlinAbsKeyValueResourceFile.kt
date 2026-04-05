package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.provider.IndexFormattedStringProvider
import ru.pocketbyte.locolaser.provider.NameFormattedStringProvider
import ru.pocketbyte.locolaser.provider.StringProvider
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.FormattingType.ArgumentsSubstitution.*
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.resource.formatting.anyName
import java.io.File

open class KotlinAbsKeyValueResourceFile(
    file: File,
    className: String,
    classPackage: String,
    protected val interfaceName: String?,
    protected val interfacePackage: String?,
    formattingType: FormattingType = NoFormattingType
): BaseKotlinPoetClassResourceFile(file, className, classPackage, formattingType) {

    protected open val stringProviderClassName by lazy {
        when (formattingType.argumentsSubstitution) {
            BY_INDEX -> IndexFormattedStringProvider::class.asTypeName()
            BY_NAME -> NameFormattedStringProvider::class.asTypeName()
            NO -> StringProvider::class.asTypeName()
        }
    }

    override fun instantiateFileSpecBuilder(
        resMap: ResMap,
        extraParams: ExtraParams?
    ): FileSpec.Builder {
        return super.instantiateFileSpecBuilder(resMap, extraParams).apply {
            if (formattingType.argumentsSubstitution == BY_NAME) {
                addImport("kotlin", "Pair")
            }
        }
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        val builder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC)
            .addProperty(
                PropertySpec
                    .builder("stringProvider", stringProviderClassName, KModifier.PRIVATE)
                    .initializer("stringProvider")
                    .build()
            )
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("stringProvider", stringProviderClassName)
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
                    .addReturnStringStatement(item.key)
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

        builder.addReturnStringStatement(item.key, formattingArguments)

        return builder
    }

    override fun instantiatePluralSpecBuilder(
            name: String, formattingArguments: List<FormattingArgument>,
            item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        item.valueForQuantity(Quantity.OTHER)
                ?: throw IllegalArgumentException("item must have OTHER quantity")
        val builder = super
            .instantiatePluralSpecBuilder(name, formattingArguments, item, resMap, extraParams)

        builder.addReturnPluralStringStatement(item.key, formattingArguments)

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

    protected open fun FunSpec.Builder.addReturnStringStatement(
        key: String,
        formattingArguments: List<FormattingArgument>? = null
    ): FunSpec.Builder {
        val argumentsString = when (formattingType.argumentsSubstitution) {
            BY_NAME -> {
                formattingArguments?.mapIndexed { index, argument ->
                    argument.anyName(index).let { "Pair(\"$it\", $it)" }
                }?.joinToString(",♢")
            }
            BY_INDEX -> {
                formattingArguments?.mapIndexed { index, argument ->
                    argument.anyName(index)
                }?.joinToString(",♢")
            }
            NO -> null
        }

        return if (argumentsString == null) {
            addStatement("return stringProvider.getString(\"${key}\")")
        } else {
            addStatement("return stringProvider.getString(\"${key}\",♢$argumentsString)")
        }
    }

    protected open fun FunSpec.Builder.addReturnPluralStringStatement(
        key: String,
        formattingArguments: List<FormattingArgument>
    ): FunSpec.Builder {
        val argumentsString = when (formattingType.argumentsSubstitution) {
            BY_NAME -> {
                formattingArguments.mapIndexed { index, argument ->
                    if (index == 0) {
                        "count"
                    } else {
                        argument.anyName(index).let { "Pair(\"$it\", $it)" }
                    }
                }.joinToString(",♢")
            }
            BY_INDEX -> {
                formattingArguments.mapIndexed { index, argument ->
                    if (index == 0) {
                        "count"
                    } else {
                        argument.anyName(index)
                    }
                }.joinToString(",♢")
            }
            NO -> null
        }

        return if (argumentsString == null) {
            addStatement("return stringProvider.getString(\"${key}\")")
        } else {
            addStatement("return stringProvider.getPluralString(\"${key}\",♢$argumentsString)")
        }
    }
}