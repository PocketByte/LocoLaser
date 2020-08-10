package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import java.io.File

class KotlinJsResourceFile(
        file: File,
        className: String,
        classPackage: String,
        interfaceName: String?,
        interfacePackage: String?,
        formattingType: FormattingType = WebFormattingType
): AbsKeyValuePoetClassResourceFile(
        file, className, classPackage, interfaceName, interfacePackage, formattingType
) {

    companion object {
        private val StringProviderImplClassName = ClassName("", "StringProviderImpl")
        private val I18nClassName = ClassName("i18next", "I18n")
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        return super.instantiateClassSpecBuilder(resMap, extraParams)
            .addType(instantiateStringProviderClassSpecBuilder().build())
            .addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("i18n", I18nClassName)
                    .callThisConstructor("${StringProviderImplClassName.simpleName()}(i18n)")
                    .build()
            )
    }

    private fun instantiateStringProviderClassSpecBuilder(): TypeSpec.Builder {
        return TypeSpec.classBuilder(StringProviderImplClassName)
            .addSuperinterface(StringProviderClassName)
            .addModifiers(KModifier.PRIVATE)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("private val i18n", I18nClassName)
                    .build()
            )
            .addFunction(
                instantiateStringProviderGetStringSpecBuilder()
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("return this.i18n.t(key, dynamic(*args))")
                    .build()
            )
            .addFunction(
                instantiateStringProviderGetPluralStringSpecBuilder()
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("return this.i18n.t(\"\${key}_plural\", dynamic(Pair(\"count\", count), *args))")
                    .build()
            )
            .addFunction(
                FunSpec.builder("dynamic")
                    .addModifiers(KModifier.PRIVATE)
                    .addParameter("vararg args", KeyValuePairClassName)
                    .addStatement("val d: dynamic = object{}")
                    .beginControlFlow("args.forEach")
                    .addStatement("d[it.first] = it.second")
                    .endControlFlow()
                    .addStatement("return d")
                    .returns(ClassName("", "dynamic"))
                    .build()
            )
    }
}