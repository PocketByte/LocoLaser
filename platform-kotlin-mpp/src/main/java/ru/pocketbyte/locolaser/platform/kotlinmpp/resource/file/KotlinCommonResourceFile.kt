package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.apache.commons.lang3.text.WordUtils
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import java.io.File

class KotlinCommonResourceFile(
    file: File,
    className: String,
    classPackage: String
): BasePoetClassResourceFile(file, className, classPackage) {

    override fun instantiateClassSpecBuilder(resMap: ResMap, writingConfig: WritingConfig?): TypeSpec.Builder {
        return TypeSpec.interfaceBuilder(className)
    }

    override fun instantiatePropertySpecBuilder(
            name: String, item: ResItem, resMap: ResMap, writingConfig: WritingConfig?
    ): PropertySpec.Builder {
        val builder = super.instantiatePropertySpecBuilder(name, item, resMap, writingConfig)

        val valueOther = item.valueForQuantity(Quantity.OTHER)
        if (valueOther?.value != null) {
            builder.addKdoc("%L", wrapCommentString(valueOther.value))
        }

        return builder
    }

    override fun instantiatePluralSpecBuilder(
            name: String, item: ResItem, resMap: ResMap, writingConfig: WritingConfig?
    ): FunSpec.Builder {
        val builder = super
            .instantiatePluralSpecBuilder(name, item, resMap, writingConfig)
            .addModifiers(KModifier.ABSTRACT)

        val valueOther = item.valueForQuantity(Quantity.OTHER)
        if (valueOther?.value != null) {
            builder.addKdoc("%L", wrapCommentString(valueOther.value))
        }

        return builder
    }
}