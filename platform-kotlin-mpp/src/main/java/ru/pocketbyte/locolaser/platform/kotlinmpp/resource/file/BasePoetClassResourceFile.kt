package ru.pocketbyte.locolaser.platform.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import org.apache.commons.lang3.text.WordUtils
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.platform.kotlinmpp.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.TextUtils
import java.io.File
import java.util.HashSet

abstract class BasePoetClassResourceFile(
        protected val directory: File,
        protected val className: String,
        protected val classPackage: String
) : ResourceFile {

    companion object {
        private const val MAX_LINE_SIZE = 120 - 6
    }

    abstract fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder

    override val formattingType: FormattingType = NoFormattingType

    override fun read(extraParams: ExtraParams?): ResMap? {
        return null
    }

    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        val locale = resMap[PlatformResources.BASE_LOCALE]
        if (locale != null) {
            val classSpec = instantiateClassSpecBuilder(resMap, extraParams)

            val keysSet = HashSet<String>()
            for (item in locale.values) {
                val propertyName = TextUtils.keyToProperty(item.key)
                if (!keysSet.contains(propertyName)) {

                    keysSet.add(propertyName)

                    if (item.isHasQuantities) {
                        classSpec.addFunction(
                            instantiatePluralSpecBuilder(propertyName, item, resMap, extraParams)
                                .build()
                        )
                    } else {
                        classSpec.addProperty(
                            instantiatePropertySpecBuilder(propertyName, item, resMap, extraParams)
                                .build()
                        )
                    }
                }
            }

            instantiateFileSpecBuilder(resMap, extraParams)
                .addType(classSpec.build())
                .build()
                .writeTo(directory)
        }
    }


    protected open fun instantiatePropertySpecBuilder(
            name: String, item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): PropertySpec.Builder {
        return PropertySpec.builder(name, String::class, KModifier.PUBLIC)
    }

    protected open fun instantiatePluralSpecBuilder(
            name: String, item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        return FunSpec.builder(name)
            .addParameter("count", Int::class)
            .returns(String::class)
            .addModifiers(KModifier.PUBLIC)
    }

    protected open fun instantiateFileSpecBuilder(
            resMap: ResMap, extraParams: ExtraParams?
    ): FileSpec.Builder {
        return FileSpec.builder(classPackage, className)
            .addComment(TemplateStr.GENERATED_CLASS_WARNING)
    }

    protected fun wrapCommentString(string: String): String {
        return WordUtils.wrap(string, MAX_LINE_SIZE, "\n", true)
    }
}