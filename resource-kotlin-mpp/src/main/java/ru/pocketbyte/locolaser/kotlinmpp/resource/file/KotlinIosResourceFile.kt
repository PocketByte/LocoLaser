package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.provider.StringProvider
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.anyName
import ru.pocketbyte.locolaser.resource.formatting.parameterClass
import java.io.File

class KotlinIosResourceFile(
        file: File,
        className: String,
        classPackage: String,
        interfaceName: String?,
        interfacePackage: String?,
): KotlinAbsKeyValueResourceFile(
        file, className, classPackage, interfaceName, interfacePackage, JavaFormattingType
) {

    companion object {
        private val StringProviderImplClassName = ClassName(
            StringProvider::class.java.`package`.name,
            "IosStringProvider"
        )
        private val BundleClassName = ClassName("platform.Foundation", "NSBundle")
    }

    override val stringProviderClassName by lazy {
        StringProvider::class.asTypeName()
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        return super.instantiateClassSpecBuilder(resMap, extraParams)
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("\"CAST_NEVER_SUCCEEDS\"")
                    .build()
            )
            .addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("bundle", BundleClassName)
                    .addParameter("tableName", String::class)
                    .callThisConstructor("${StringProviderImplClassName.simpleName}(bundle, tableName)")
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

        builder.addImport(
            StringProviderImplClassName.packageName,
            StringProviderImplClassName.simpleName
        )

        builder.addImport(
            "platform.Foundation",
            "NSString",
            "stringWithFormat",
            "localizedStringWithFormat"
        )
        return builder
    }

    override fun FunSpec.Builder.addReturnStringStatement(
        key: String,
        formattingArguments: List<FormattingArgument>?
    ): FunSpec.Builder {
        val getStringStatement = "stringProvider.getString(\"$key\")"

        val stringWithFormatStatement = formattingArguments?.mapIndexed { index, argument ->
            if (argument.parameterClass() == String::class) {
                argument.anyName(index) + " as NSString"
            } else {
                argument.anyName(index)
            }
        }?.joinToString()?.let {
            "NSString.stringWithFormat($getStringStatement, $it)"
        } ?: getStringStatement


        return addStatement("return $stringWithFormatStatement")
    }

    override fun FunSpec.Builder.addReturnPluralStringStatement(
        key: String,
        formattingArguments: List<FormattingArgument>
    ): FunSpec.Builder {
        val getStringStatement = "stringProvider.getString(\"$key\")"

        val stringWithFormatStatement = formattingArguments.mapIndexed { index, argument ->
            if (index == 0) {
                "count"
            } else if (argument.parameterClass() == String::class) {
                argument.anyName(index) + " as NSString"
            } else {
                argument.anyName(index)
            }
        }.joinToString().let {
            if (it.isNotBlank()) {
                "NSString.localizedStringWithFormat($getStringStatement, ${it})"
            } else {
                "NSString.localizedStringWithFormat($getStringStatement)"
            }
        }

        return addStatement("return $stringWithFormatStatement")
    }
}