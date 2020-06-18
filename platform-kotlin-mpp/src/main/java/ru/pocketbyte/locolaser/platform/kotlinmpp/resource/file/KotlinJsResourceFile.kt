package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.platform.kotlinmpp.extension.hasPlurals
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import java.io.File

class KotlinJsResourceFile(
        file: File,
        className: String,
        classPackage: String,
        private val interfaceName: String?,
        private val interfacePackage: String?
): BasePoetClassResourceFile(file, className, classPackage) {

    override fun instantiateClassSpecBuilder(resMap: ResMap, writingConfig: WritingConfig?): TypeSpec.Builder {
        val builder = TypeSpec.classBuilder(className)
                .addModifiers(KModifier.PUBLIC)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("private val i18n", ClassName("i18next", "I18n"))
                        .build()
                )
        if (interfaceName != null && interfacePackage != null) {
            builder.addSuperinterface(ClassName(interfacePackage, interfaceName))
        }

        if (resMap.hasPlurals) {
            builder.addType(
                TypeSpec.classBuilder("Plural")
                    .addModifiers(KModifier.DATA)
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter("val count", Int::class)
                            .build()
                    ).build()
            )
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
                    .addStatement("return this.i18n.t(\"${item.key}\")")
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
            .addStatement("return this.i18n.t(\"${item.key}_plural\", Plural(count))")

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
}