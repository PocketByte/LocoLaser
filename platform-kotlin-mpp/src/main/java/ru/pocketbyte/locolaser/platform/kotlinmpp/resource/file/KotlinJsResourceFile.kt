package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import java.io.File

class KotlinJsResourceFile(
        file: File,
        className: String,
        classPackage: String,
        private val interfaceName: String?,
        private val interfacePackage: String?
): AbsKeyValuePoetClassResourceFile(file, className, classPackage, interfaceName, interfacePackage) {

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
            .addType(
                TypeSpec.classBuilder("Plural")
                    .addModifiers(KModifier.DATA)
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter("val count", Int::class)
                            .build()
                    ).build()
            )
            .addFunction(
                FunSpec.builder("getString")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("key", String::class)
                    .addStatement("return this.i18n.t(key)")
                    .returns(String::class)
                    .build()
            )
            .addFunction(
                FunSpec.builder("getPluralString")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("key", String::class)
                    .addParameter("count", Int::class)
                    .addStatement("return this.i18n.t(\"\${key}_plural\", Plural(count))")
                    .returns(String::class)
                    .build()
            )
    }
}