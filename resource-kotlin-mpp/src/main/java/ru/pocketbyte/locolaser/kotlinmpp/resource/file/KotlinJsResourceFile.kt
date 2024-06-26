package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.provider.StringProvider
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import java.io.File

class KotlinJsResourceFile(
    file: File,
    className: String,
    classPackage: String,
    interfaceName: String?,
    interfacePackage: String?,
): KotlinAbsKeyValueResourceFile(
    file, className, classPackage, interfaceName, interfacePackage, WebFormattingType
) {

    companion object {
        private val StringProviderImplClassName = ClassName(
            StringProvider::class.java.`package`.name,
            "JsStringProvider"
        )
        private val I18nClassName = ClassName("i18next", "I18n")
    }

    override fun instantiateFileSpecBuilder(
        resMap: ResMap,
        extraParams: ExtraParams?
    ): FileSpec.Builder {
        return super.instantiateFileSpecBuilder(resMap, extraParams)
            .addImport(
                StringProviderImplClassName.packageName,
                StringProviderImplClassName.simpleName
            )
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        return super.instantiateClassSpecBuilder(resMap, extraParams)
            .addFunction(
                FunSpec.constructorBuilder()
                    .addParameter("i18n", I18nClassName)
                    .callThisConstructor("${StringProviderImplClassName.simpleName}(i18n)")
                    .build()
            )
    }
}