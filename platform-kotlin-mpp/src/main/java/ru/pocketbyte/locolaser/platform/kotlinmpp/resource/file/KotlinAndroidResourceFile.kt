package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import java.io.File

class KotlinAndroidResourceFile(
        file: File,
        className: String,
        classPackage: String,
        private val interfaceName: String?,
        private val interfacePackage: String?
): BasePoetClassResourceFile(file, className, classPackage) {

    private val contextClass = ClassName("android.content", "Context")
    private val mutableMapClass = ClassName("kotlin.collections", "MutableMap")

    override fun instantiateClassSpecBuilder(resMap: ResMap, writingConfig: WritingConfig?): TypeSpec.Builder {
        val builder = TypeSpec.classBuilder(className)
                .addModifiers(KModifier.PUBLIC)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("private val context", contextClass)
                        .build()
                ).addProperty(
                    PropertySpec.builder("resIds",
                        ParameterizedTypeName.get(
                            mutableMapClass, String::class.asTypeName(),
                            ParameterizedTypeName.get(
                                mutableMapClass, String::class.asTypeName(), Int::class.asTypeName()
                            )
                        ), KModifier.PRIVATE
                    ).initializer("mutableMapOf<String, MutableMap<String, Int>>()").build()
                ).addFunction(
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
                                    .addStatement("resId = context.resources.getIdentifier(resName, defType, context.packageName)")
                                    .addStatement("resMap[resName] = resId")
                                .endControlFlow()
                                .addStatement("return resId")
                                .build()
                        ).build()
                )
        if (interfaceName != null && interfacePackage != null) {
            builder.addSuperinterface(ClassName(interfacePackage, interfaceName))
        }

        return builder
    }

    override fun instantiatePropertySpecBuilder(
            name: String, item: ResItem, resMap: ResMap, writingConfig: WritingConfig?
    ): PropertySpec.Builder {
        val builder = super
            .instantiatePropertySpecBuilder(name, item, resMap, writingConfig)
            .getter(
                FunSpec.getterBuilder()
                    .addStatement("return this.context.getString(getId(\"${item.androidId}\", \"string\"))")
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
            name: String, item: ResItem, resMap: ResMap, writingConfig: WritingConfig?
    ): FunSpec.Builder {
        val builder = super
            .instantiatePluralSpecBuilder(name, item, resMap, writingConfig)
            .addStatement("return this.context.resources.getQuantityString(getId(\"${item.androidId}\", \"plurals\"), count)")

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

    private val ResItem.androidId: String
        get() = this.key.trim { it <= ' ' }
}