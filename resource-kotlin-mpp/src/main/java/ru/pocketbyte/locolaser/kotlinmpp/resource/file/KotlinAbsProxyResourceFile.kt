package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.resource.formatting.anyName
import java.io.File

class KotlinAbsProxyResourceFile(
    file: File,
    className: String,
    classPackage: String,
    private val interfaceName: String,
    private val interfacePackage: String,
    formattingType: FormattingType = NoFormattingType
): BaseKotlinPoetClassResourceFile(file, className, classPackage, formattingType) {

    companion object {
        private const val PROPERTY_REPOSITORY = "stringRepository"
    }

    private val stringRepositoryClassName = ClassName(
        interfacePackage, interfaceName
    )

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        val builder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC, KModifier.ABSTRACT)
            .addProperty(
                PropertySpec
                    .builder(
                        PROPERTY_REPOSITORY,
                        stringRepositoryClassName,
                        KModifier.PROTECTED, KModifier.ABSTRACT)
                    .build()
            )

        builder.addSuperinterface(ClassName(interfacePackage, interfaceName))

        return builder
    }

    override fun instantiatePropertySpecBuilder(
        name: String, item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): PropertySpec.Builder {
        val builder = super
            .instantiatePropertySpecBuilder(name, item, resMap, extraParams)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return $PROPERTY_REPOSITORY.$name")
                    .build()
            )

        builder.addModifiers(KModifier.OVERRIDE)

        return builder
    }

    override fun instantiateFormattedPropertySpecBuilder(
        name: String, formattingArguments: List<FormattingArgument>,
        item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val builder = super.instantiateFormattedPropertySpecBuilder(
            name, formattingArguments, item, resMap, extraParams
        )

        builder.addModifiers(KModifier.OVERRIDE)

        if (formattingArguments.isEmpty()) {
            builder.addStatement("return $PROPERTY_REPOSITORY.$name()")
        } else {
            val argumentsString = formattingArguments.mapIndexed { index, argument ->
                argument.anyName(index)
            }.joinToString()
            builder.addStatement("return $PROPERTY_REPOSITORY.$name($argumentsString)")
        }

        return builder
    }

    override fun instantiatePluralSpecBuilder(
        name: String, formattingArguments: List<FormattingArgument>,
        item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        return instantiateFormattedPropertySpecBuilder(
            name, formattingArguments, item, resMap, extraParams
        )
    }
}