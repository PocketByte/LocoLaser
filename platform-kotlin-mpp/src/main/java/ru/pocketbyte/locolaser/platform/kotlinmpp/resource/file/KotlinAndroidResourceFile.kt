package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class KotlinAndroidResourceFile(
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
        private val ContextClassName = ClassName("android.content", "Context")
        private val MutableMapClassName = ClassName("kotlin.collections", "MutableMap")
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        return super.instantiateClassSpecBuilder(resMap, extraParams)
            .addType(instantiateStringProviderClassSpecBuilder().build())
            .addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("context", ContextClassName)
                    .callThisConstructor("${StringProviderImplClassName.simpleName()}(context)")
                    .build()
            )
    }

    private fun instantiateStringProviderClassSpecBuilder(): TypeSpec.Builder {
        return TypeSpec.classBuilder(StringProviderImplClassName)
            .addSuperinterface(StringProviderClassName)
            .addModifiers(KModifier.PRIVATE)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("private val context", ContextClassName)
                    .build()
            )
            .addProperty(
                PropertySpec.builder("resIds",
                    ParameterizedTypeName.get(
                        MutableMapClassName, String::class.asTypeName(),
                        ParameterizedTypeName.get(
                            MutableMapClassName, String::class.asTypeName(), Int::class.asTypeName()
                        )
                    ), KModifier.PRIVATE
                ).initializer("mutableMapOf<String, MutableMap<String, Int>>()").build()
            )
            .addFunction(
                instantiateStringProviderGetStringSpecBuilder()
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("return this.context.getString(getId(key, \"string\"), *args)")
                    .build()
            )
            .addFunction(
                instantiateStringProviderGetPluralStringSpecBuilder()
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("return this.context.resources.getQuantityString(getId(key, \"plurals\"), count.toInt(), count, *args)")
                    .build()
            )
            .addFunction(
                FunSpec.builder("getId")
                    .addModifiers(KModifier.PRIVATE)
                    .addParameter("resName", String::class)
                    .addParameter("defType", String::class)
                    .returns(Int::class)
                    .addCode(
                        CodeBlock.builder()
                            .addStatement("var resMap = resIds[defType]")
                            .beginControlFlow("if (resMap == null)")
                                .addStatement("resMap = mutableMapOf()")
                                .addStatement("resIds[defType] = resMap")
                            .endControlFlow()
                            .addStatement("var resId = resMap[resName]")
                            .beginControlFlow("if (resId == null)")
                                .addStatement("resId = context.resources.getIdentifier(")
                                .addStatement("    resName.trim { it <= ' ' }, defType, context.packageName")
                                .addStatement(")")
                                .addStatement("resMap[resName] = resId")
                            .endControlFlow()
                            .addStatement("return resId")
                            .build()
                    ).build()
            )
    }
}