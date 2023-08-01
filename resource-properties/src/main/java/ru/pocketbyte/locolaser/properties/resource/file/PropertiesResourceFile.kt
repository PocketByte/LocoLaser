package ru.pocketbyte.locolaser.properties.resource.file

import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.duplicateComments
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.PluralUtils
import java.io.File
import java.io.IOException
import java.io.LineNumberReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern

class PropertiesResourceFile(
    file:File,
    private val locale: String
):ResourceStreamFile(file) {

    companion object {

        const val GENERATED_COMMENT = "# AUTO-GENERATED FILE. DO NOT MODIFY.\r\n" +
                "#\r\n" +
                "# This file was automatically generated by the LocoLaser tool.\r\n" +
                "# It should not be modified by hand."

        private const val COMMENT_SINGLE_LINE = "#"
        private const val KEY_VALUE_PATTERN = "((?:[^\"]|\\\\\")+)=((?:[^\"]|\\\\\")*)"

        internal fun toPlatformValue(string:String):String {
            return string
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
        }

        internal fun fromPlatformValue(string:String):String {
            return string
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
        }
    }

    override val formattingType: FormattingType = JavaFormattingType

    override fun read(extraParams: ExtraParams?):ResMap? {
        if (file.exists()) {
            val currentLocaleMap = ResLocale()

            val keyValueMatcher = Pattern.compile(KEY_VALUE_PATTERN).matcher("")

            val path = Paths.get(file.toURI())
            try {
                Files.newBufferedReader(path, StandardCharsets.UTF_8).use { reader-> LineNumberReader(reader).use { lineReader->

                    var line:String?
                    var comment:StringBuilder? = null

                    do {
                        line = lineReader.readLine()
                        if (line == null || GENERATED_COMMENT.contains(line)) {
                            comment = null
                            continue
                        }

                        if (line.startsWith(COMMENT_SINGLE_LINE)) {
                            val commentString = line.substring(COMMENT_SINGLE_LINE.length).trim { it <= ' ' }
                            if (comment == null)
                                comment = StringBuilder(commentString)
                            else
                                comment.append("\n").append(commentString)
                            continue
                        }

                        keyValueMatcher.reset(line)
                        if (keyValueMatcher.find() && keyValueMatcher.groupCount() == 2) {
                            val commentString = comment?.toString()
                            val keyString = keyValueMatcher.group(1)
                            val valueString = keyValueMatcher.group(2)
                            val quantitySeparatorIndex = keyString.lastIndexOf(".")
                            val quantity = if (quantitySeparatorIndex > 0) {
                                PluralUtils.quantityFromString(
                                    keyString.substring(quantitySeparatorIndex + 1)
                                )
                            } else { null }

                            if (quantity != null) {
                                val key = keyString.substring(0, quantitySeparatorIndex)
                                val item = currentLocaleMap[key] ?: ResItem(key)
                                item.addValue(valueString, commentString, quantity)
                                currentLocaleMap.put(item)
                            } else {
                                val item = ResItem(keyString)
                                item.addValue(valueString, commentString, Quantity.OTHER)
                                currentLocaleMap.put(item)
                            }

                            comment = null
                            continue
                        }

                        // Nothing found
                        comment = null
                    } while (line != null) }
                }
            }
            catch (e:IOException) {
                // Do nothing
                e.printStackTrace()
            }

            return ResMap().apply {
                put(locale, currentLocaleMap)
            }
        }
        return null
    }

    @Throws(IOException::class)
    override fun write(resMap:ResMap, extraParams: ExtraParams?) {
        open()

        writeStringLn(GENERATED_COMMENT)
        writeln()

        val items = resMap[locale] ?: return

        for (key in items.keys) {
            val resItem = items[key]
            if (resItem != null) {
                if (resItem.isHasQuantities) {
                    for (value in resItem.values) {
                        if (isCommentShouldBeWritten(value, extraParams)) {
                            writeComment(value.comment)
                        }
                        writeString(resItem.key)
                        writeString(".")
                        writeString(value.quantity.toString())
                        writeString("=")
                        writeStringLn(toPlatformValue(value.value))
                    }
                } else {
                    val value = resItem.valueForQuantity(Quantity.OTHER)
                    if (value != null) {
                        if (isCommentShouldBeWritten(value, extraParams)) {
                            writeComment(value.comment)
                        }
                        writeString(resItem.key)
                        writeString("=")
                        writeStringLn(toPlatformValue(value.value))
                    }
                }
            }
        }

        close()
    }

    private fun isCommentShouldBeWritten(value:ResValue, extraParams: ExtraParams?):Boolean {
        return value.comment != null && (extraParams?.duplicateComments != false || value.comment != value.value)
    }

    private fun ResItem.addValue(value: String, comment: String?, quantity: Quantity) {
        val formattingArguments = formattingType.argumentsFromValue(value)
        val formattingType = if (formattingArguments?.isEmpty() != false) NoFormattingType else formattingType
        this.addValue(ResValue(value, comment, quantity, formattingType, formattingArguments))
    }

    private fun writeComment(comment: String?) {
        writeString(COMMENT_SINGLE_LINE)
        writeString(" ")
        writeStringLn(comment?.replace("\n", "\n$COMMENT_SINGLE_LINE ") ?: "")
    }
}
