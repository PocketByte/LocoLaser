package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import java.io.File

open class AbsKeyValuePoetClassResourceFile(
        file: File,
        className: String,
        classPackage: String,
        private val interfaceName: String?,
        private val interfacePackage: String?
): BasePoetClassResourceFile(file, className, classPackage) {

    companion object {
        val StringProviderClassName = ClassName("", "StringProvider")
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

    private fun instantiateStringProviderInterfaceSpecBuilder(): TypeSpec.Builder {
        return TypeSpec.interfaceBuilder(StringProviderClassName)
            .addFunction(
                FunSpec.builder("getString")
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter("key", String::class)
                    .returns(String::class)
                    .build()
            )
            .addFunction(
                FunSpec.builder("getPluralString")
                    .addModifiers(KModifier.ABSTRACT)
                    .addParameter("key", String::class)
                    .addParameter("count", Int::class)
                    .returns(String::class)
                    .build()
            )
    }
}