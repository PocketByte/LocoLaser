package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.platform.kotlinmpp.extension.hasPlurals
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import java.io.File

class KotlinIosResourceFile(
        file: File,
        className: String,
        classPackage: String,
        private val interfaceName: String?,
        private val interfacePackage: String?
): BasePoetClassResourceFile(file, className, classPackage) {

    private val bundleClass = ClassName("platform.Foundation", "NSBundle")

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        val builder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.PUBLIC)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("private val bundle", bundleClass)
                    .addParameter("private val tableName", String::class)
                    .build()
            ).addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("bundle", bundleClass)
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
                    .addStatement("return this.bundle.localizedStringForKey(\"${item.key}\", \"\", this.tableName)")
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
            .addStatement("return NSLocalizedPluralString(\"${item.key}\", this.tableName, this.bundle, count)!!")

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

    override fun instantiateFileSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): FileSpec.Builder {
        val builder = super.instantiateFileSpecBuilder(resMap, extraParams)

        if (resMap.hasPlurals) {
            builder.addStaticImport("localizedPlural", "NSLocalizedPluralString")
        }

        return builder
    }
}