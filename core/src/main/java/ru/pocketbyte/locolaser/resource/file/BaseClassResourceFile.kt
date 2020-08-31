package ru.pocketbyte.locolaser.resource.file

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.TextUtils

import java.io.File
import java.io.IOException
import java.util.HashSet
import java.util.regex.Pattern

abstract class BaseClassResourceFile(file: File) : ResourceStreamFile(file) {

    companion object {
        private val FORMATTING_PATTERN = Pattern.compile("%([0-9]*)([s|d])")
    }

    class FormatArgument(
        val index: Int?,
        val type: String
    )

    override val formattingType: FormattingType = NoFormattingType

    @Throws(IOException::class)
    protected abstract fun writeHeaderComment(resMap: ResMap, extraParams: ExtraParams?)

    @Throws(IOException::class)
    protected abstract fun writeClassHeader(resMap: ResMap, extraParams: ExtraParams?)

    @Throws(IOException::class)
    protected abstract fun writeComment(extraParams: ExtraParams?, comment: String)

    @Throws(IOException::class)
    protected abstract fun writeProperty(extraParams: ExtraParams?, propertyName: String, item: ResItem)

    @Throws(IOException::class)
    protected abstract fun writeClassFooter(resMap: ResMap, extraParams: ExtraParams?)

    override fun read(extraParams: ExtraParams?): ResMap? {
        return null
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        val locale = resMap[Resources.BASE_LOCALE]
        if (locale != null) {
            open()
            writeHeaderComment(resMap, extraParams)
            writeln()
            writeClassHeader(resMap, extraParams)

            val keysSet = HashSet<String>()
            for (item in locale.values) {
                val propertyName = TextUtils.keyToProperty(item.key)
                if (!keysSet.contains(propertyName)) {

                    keysSet.add(propertyName)

                    val valueOther = item.valueForQuantity(Quantity.OTHER)
                    if (valueOther?.value != null) {
                        writeComment(extraParams, valueOther.value)
                    }

                    writeProperty(extraParams, propertyName, item)

                    var arguments = mutableListOf<Pair<Int, String>>()
                }
            }

            writeClassFooter(resMap, extraParams)
            close()
        }
    }
}
