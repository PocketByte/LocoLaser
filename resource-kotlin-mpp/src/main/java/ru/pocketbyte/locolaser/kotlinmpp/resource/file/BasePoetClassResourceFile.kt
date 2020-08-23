package ru.pocketbyte.locolaser.kotlinmpp.resource.file

import com.squareup.kotlinpoet.*
import org.apache.commons.lang3.text.WordUtils
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.exception.InvalidValueException
import ru.pocketbyte.locolaser.kotlinmpp.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.FormattingArgument
import ru.pocketbyte.locolaser.resource.entity.Quantity
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.*
import ru.pocketbyte.locolaser.resource.formatting.FormattingType.ArgumentsSubstitution
import ru.pocketbyte.locolaser.utils.TextUtils
import java.io.File
import java.util.HashSet

abstract class BasePoetClassResourceFile(
        protected val directory: File,
        protected val className: String,
        protected val classPackage: String,
        override val formattingType: FormattingType = NoFormattingType
) : ResourceFile {

    companion object {
        private const val MAX_LINE_SIZE = 120 - 6
    }

    abstract fun instantiateClassSpecBuilder(resMap: ResMap, extraParams: ExtraParams?): TypeSpec.Builder

    override fun read(extraParams: ExtraParams?): ResMap? {
        return null
    }

    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        val locale = resMap[Resources.BASE_LOCALE]
        if (locale != null) {
            val classSpec = instantiateClassSpecBuilder(resMap, extraParams)

            val keysSet = HashSet<String>()
            for (item in locale.values) {
                val propertyName = TextUtils.keyToProperty(item.key)
                if (!keysSet.contains(propertyName)) {

                    keysSet.add(propertyName)

                    if (item.isHasQuantities) {
                        val resItem = item.valueForQuantity(Quantity.OTHER)
                        val formattingArguments = sortFormattingArguments(resItem?.formattingArguments)
                        val countArgument = formattingArguments?.firstOrNull()

                        if (countArgument != null) {
                            val type = countArgument.parameterClass()
                            if (type == Int::class || type == Long::class) {
                                classSpec.addFunction(
                                    instantiatePluralSpecBuilder(
                                        propertyName, formattingArguments,
                                        item, resMap, extraParams
                                    ).build()
                                )
                            } else {
                                throw InvalidValueException(
                                    "Invalid plural string. " +
                                    "The first argument must be integer. " +
                                    "String= ${resItem?.value}")
                            }
                        } else {
                            classSpec.addFunction(
                                instantiatePluralSpecBuilder(
                                    propertyName,
                                    listOf(FormattingArgument(
                                        "count", 1,
                                        mapOf(Pair(FormattingType.PARAM_CLASS, Long::class))
                                    )),
                                    item, resMap, extraParams
                                ).build()
                            )
                        }
                    } else {
                        classSpec.addProperty(
                            instantiatePropertySpecBuilder(propertyName, item, resMap, extraParams)
                                .build()
                        )
                        if (formattingType.argumentsSubstitution != ArgumentsSubstitution.NO) {
                            val formattingArguments = sortFormattingArguments(
                                item.valueForQuantity(Quantity.OTHER)?.formattingArguments
                            )
                            if (formattingArguments?.isEmpty() == false) {
                                classSpec.addFunction(
                                    instantiateFormattedPropertySpecBuilder(
                                        propertyName, formattingArguments,
                                        item, resMap, extraParams
                                    ).build()
                                )
                            }
                        }
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

    protected open fun instantiateFormattedPropertySpecBuilder(
            name: String, formattingArguments: List<FormattingArgument>,
            item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val resValue = item.valueForQuantity(Quantity.OTHER)
                ?: throw IllegalArgumentException("item must have OTHER quantity")
        return FunSpec.builder(name)
            .returns(String::class)
            .addModifiers(KModifier.PUBLIC)
            .apply {
                formattingArguments.forEachIndexed { index, argument ->
                    addParameter(
                        argument.anyName(index),
                        argument.parameterClass()
                    )
                }
            }
    }

    protected open fun instantiatePluralSpecBuilder(
            name: String, formattingArguments: List<FormattingArgument>,
            item: ResItem, resMap: ResMap, extraParams: ExtraParams?
    ): FunSpec.Builder {
        val resValue = item.valueForQuantity(Quantity.OTHER)
                ?: throw IllegalArgumentException("item must have OTHER quantity")
        return FunSpec.builder(name)
            .returns(String::class)
            .addModifiers(KModifier.PUBLIC)
            .apply {
                formattingArguments.forEachIndexed { index, argument ->
                    addParameter(
                        if (index == 0) "count" else argument.anyName(index),
                        argument.parameterClass()
                    )
                }
            }
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

    private fun sortFormattingArguments(
            formattingArguments: List<FormattingArgument>?
    ): List<FormattingArgument>? = formattingArguments?.sortedWith(Comparator { o1, o2 ->
        if (o1.index != null || o2.index != null) {
            return@Comparator (o1.index ?: Int.MAX_VALUE) - (o2.index ?: Int.MAX_VALUE)
        }
        0
    })
}