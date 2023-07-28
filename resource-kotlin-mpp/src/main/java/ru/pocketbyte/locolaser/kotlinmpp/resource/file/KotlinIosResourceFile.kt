package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.provider.StringProvider
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class KotlinIosResourceFile(
        file: File,
        className: String,
        classPackage: String,
        interfaceName: String?,
        interfacePackage: String?,
): AbsKeyValuePoetClassResourceFile(
        file, className, classPackage, interfaceName, interfacePackage, JavaFormattingType
) {

    companion object {
        private val StringProviderImplClassName = ClassName(
            StringProvider::class.java.`package`.name,
            "IosStringProvider"
        )
        private val BundleClassName = ClassName("platform.Foundation", "NSBundle")
    }

    override fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder {
        return super.instantiateClassSpecBuilder(resMap, extraParams)
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

        builder.addImport("platform.Foundation", "localizedStringWithFormat")
        builder.addImport("platform.Foundation", "stringWithFormat")
        builder.addImport(
            StringProviderImplClassName.packageName,
            StringProviderImplClassName.simpleName
        )

        return builder
    }
}