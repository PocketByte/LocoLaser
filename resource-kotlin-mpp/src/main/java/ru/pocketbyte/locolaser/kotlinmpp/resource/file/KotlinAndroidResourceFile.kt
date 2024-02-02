package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.provider.StringProvider
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import java.io.File

class KotlinAndroidResourceFile(
        file: File,
        className: String,
        classPackage: String,
        interfaceName: String?,
        interfacePackage: String?
): KotlinAbsKeyValueResourceFile(
    file, className, classPackage, interfaceName, interfacePackage, JavaFormattingType
) {

    companion object {
        private val StringProviderImplClassName = ClassName(
            StringProvider::class.java.`package`.name,
            "AndroidStringProvider"
        )
        private val ContextClassName = ClassName("android.content", "Context")
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
                    .addParameter("context", ContextClassName)
                    .callThisConstructor("${StringProviderImplClassName.simpleName}(context)")
                    .build()
            )
    }
}