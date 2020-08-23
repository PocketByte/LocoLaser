package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class KotlinCommonResourceFile(
    file: File,
    className: String,
    classPackage: String,
    formattingType: FormattingType = JavaFormattingType
): BasePoetClassResourceFile(file, className, classPackage, formattingType) {

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        return TypeSpec.interfaceBuilder(className)
    }

    override fun instantiatePropertySpecBuilder(
            name: String, item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): PropertySpec.Builder {
        val builder = super.instantiatePropertySpecBuilder(name, item, resMap, extraParams)

        val valueOther = item.valueForQuantity(Quantity.OTHER)
        if (valueOther?.value != null) {
            builder.addKdoc("%L", wrapCommentString(valueOther.value))
        }

        return builder
    }

    override fun instantiateFormattedPropertySpecBuilder(
            name: String, formattingArguments: List<FormattingArgument>,
            item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val builder = super
            .instantiateFormattedPropertySpecBuilder(name, formattingArguments, item, resMap, extraParams)
            .addModifiers(KModifier.ABSTRACT)

        val valueOther = item.valueForQuantity(Quantity.OTHER)
        if (valueOther?.value != null) {
            builder.addKdoc("%L", wrapCommentString(valueOther.value))
        }

        return builder
    }

    override fun instantiatePluralSpecBuilder(
            name: String, formattingArguments: List<FormattingArgument>,
            item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val builder = super
            .instantiatePluralSpecBuilder(name, formattingArguments, item, resMap, extraParams)
            .addModifiers(KModifier.ABSTRACT)

        val valueOther = item.valueForQuantity(Quantity.OTHER)
        if (valueOther?.value != null) {
            builder.addKdoc("%L", wrapCommentString(valueOther.value))
        }

        return builder
    }
}