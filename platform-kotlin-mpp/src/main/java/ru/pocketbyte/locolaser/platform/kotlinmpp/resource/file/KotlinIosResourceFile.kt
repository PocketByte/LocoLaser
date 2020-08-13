package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class KotlinIosResourceFile(
        file: File,
        className: String,
        classPackage: String,
        interfaceName: String?,
        interfacePackage: String?,
        formattingType: FormattingType = JavaFormattingType
): AbsKeyValuePoetClassResourceFile(
        file, className, classPackage, interfaceName, interfacePackage, formattingType
) {

    companion object {
        private val StringProviderImplClassName = ClassName("", "StringProviderImpl")
        private val BundleClassName = ClassName("platform.Foundation", "NSBundle")
        private val StringClassName = ClassName("platform.Foundation", "NSString")
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

        builder.addStaticImport("platform.Foundation", "localizedStringWithFormat")
        builder.addStaticImport("platform.Foundation", "stringWithFormat")

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
            )
            .addFunction(
                instantiateStringProviderGetStringSpecBuilder()
                    .addModifiers(KModifier.OVERRIDE).apply {
                        addStatement("val string = this.bundle.localizedStringForKey(key, \"\", this.tableName)")
                        beginControlFlow("return when(args.size)")
                        addStatement("0 -> string")
                        val switchSize = 20
                        for (i in 1..switchSize) {
                            val builder = StringBuilder(
                                (if (i == switchSize) "else" else i.toString()) +
                                        " -> %T.stringWithFormat(string"
                            )
                            for (j in 0 until i) builder.append(", args[$j]")
                            builder.append(")")
                            addStatement(builder.toString(), StringClassName)
                        }
                        endControlFlow()
                    }
                    .build()
            )
            .addFunction(
                instantiateStringProviderGetPluralStringSpecBuilder()
                    .addModifiers(KModifier.OVERRIDE)
                    .apply {
                        addStatement("val string = this.bundle.localizedStringForKey(key, \"\", this.tableName)")
                        beginControlFlow("return when(args.size)")
                        val switchSize = 20
                        for (i in 0..switchSize) {
                            val builder = StringBuilder(
                                (if (i == switchSize) "else" else i.toString()) +
                                    " -> %T.localizedStringWithFormat(string, count"
                            )
                            for (j in 0 until i) builder.append(", args[$j]")
                            builder.append(")")
                            addStatement(builder.toString(), StringClassName)
                        }
                        endControlFlow()
                    }
                    .build()
            )
    }
}