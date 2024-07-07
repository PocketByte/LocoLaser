package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import ru.pocketbyte.locolaser.config.ExtraParams
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
        private val BundleClassName = ClassName("platform.Foundation", "NSBundle")
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        val builder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC)
            .addProperty(
                PropertySpec
                    .builder("bundle", BundleClassName, KModifier.PRIVATE)
                    .initializer("bundle")
                    .build()
            )
            .addProperty(
                PropertySpec
                    .builder("tableName", String::class, KModifier.PRIVATE)
                    .initializer("tableName")
                    .build()
            )
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("bundle", BundleClassName)
                    .addParameter("tableName", String::class)
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

        if (interfaceName != null && interfacePackage != null) {
            builder.addSuperinterface(ClassName(interfacePackage, interfaceName))
        }

        return builder
    }

    override fun instantiateFileSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): FileSpec.Builder {
        val builder = super.instantiateFileSpecBuilder(resMap, extraParams)

        builder.addImport(
            "platform.Foundation",
            "NSString",
            "stringWithFormat",
            "localizedStringWithFormat"
        )
        return builder
    }

    override fun getStringStatement(
        key: String,
        formattingArguments: List<FormattingArgument>?
    ): String {
        val result = "bundle.localizedStringForKey(\"${key}\", \"\", tableName)"

        val argumentsString = formattingArguments?.mapIndexed { index, argument ->
            if (argument.parameterClass() == String::class) {
                argument.anyName(index) + " as NSString"
            } else {
                argument.anyName(index)
            }
        }?.joinToString()

        return if (argumentsString != null) {
            "NSString.stringWithFormat(${result}, ${argumentsString})"
        } else {
            result
        }
    }

    override fun getPluralStringStatement(
        key: String,
        formattingArguments: List<FormattingArgument>
    ): String {
        val result = "bundle.localizedStringForKey(\"${key}\", \"\", tableName)"

        val argumentsString = formattingArguments.mapIndexed { index, argument ->
            if (index == 0) {
                "count"
            } else if (argument.parameterClass() == String::class) {
                argument.anyName(index) + " as NSString"
            } else {
                argument.anyName(index)
            }
        }.joinToString()

        return if (argumentsString.isNotBlank()) {
            "NSString.localizedStringWithFormat(${result}, ${argumentsString})"
        } else {
            "NSString.localizedStringWithFormat(${result})"
        }
    }
}