package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.platform.kotlinmpp.extension.hasPlurals
import ru.pocketbyte.locolaser.resource.entity.ResMap
import java.io.File

class KotlinIosResourceFile(
        file: File,
        className: String,
        classPackage: String,
        interfaceName: String?,
        interfacePackage: String?
): AbsKeyValuePoetClassResourceFile(file, className, classPackage, interfaceName, interfacePackage) {

    companion object {
        private val StringProviderImplClassName = ClassName("", "StringProviderImpl")
        private val BundleClassName = ClassName("platform.Foundation", "NSBundle")
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        return super.instantiateClassSpecBuilder(resMap, extraParams)
            .addType(instantiateStringProviderClassSpecBuilder().build())
            .addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("bundle", BundleClassName)
                    .addParameter("tableName", String::class)
                    .callThisConstructor("${StringProviderImplClassName.simpleName()}(bundle, tableName)")
                    .build()
            )
            .addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("bundle", BundleClassName)
                    .callThisConstructor("bundle", "\"Localizable\"")
                    .build()
            )
            .addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("tableName", String::class)
                    .callThisConstructor("NSBundle.mainBundle()", "tableName")
                    .build()
            )
            .addFunction(
                FunSpec.constructorBuilder()
                    .callThisConstructor("NSBundle.mainBundle()", "\"Localizable\"")
                    .build()
            )
    }

    override fun instantiateFileSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): FileSpec.Builder {
        val builder = super.instantiateFileSpecBuilder(resMap, extraParams)

        if (resMap.hasPlurals) {
            builder.addStaticImport("localizedPlural", "NSLocalizedPluralString")
        }

        return builder
    }

    private fun instantiateStringProviderClassSpecBuilder(): TypeSpec.Builder {
        return TypeSpec.classBuilder(StringProviderImplClassName)
            .addSuperinterface(StringProviderClassName)
            .addModifiers(KModifier.PRIVATE)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("private val bundle", BundleClassName)
                    .addParameter("private val tableName", String::class)
                    .build()
            ).addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("bundle", BundleClassName)
                    .callThisConstructor("bundle", "\"Localizable\"")
                    .build()
            ).addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("tableName", String::class)
                    .callThisConstructor("NSBundle.mainBundle()", "tableName")
                    .build()
            ).addFunction(
                FunSpec.constructorBuilder()
                    .callThisConstructor("NSBundle.mainBundle()", "\"Localizable\"")
                    .build()
            )
            .addFunction(
                FunSpec.builder("getString")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("key", String::class)
                    .addStatement("return this.bundle.localizedStringForKey(key, \"\", this.tableName)")
                    .returns(String::class)
                    .build()
            )
            .addFunction(
                FunSpec.builder("getPluralString")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("key", String::class)
                    .addParameter("count", Int::class)
                    .addStatement("return NSLocalizedPluralString(key, this.tableName, this.bundle, count) ?: key")
                    .returns(String::class)
                    .build()
            )
    }
}