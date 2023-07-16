package ru.pocketbyte.locolaser.mobile.resource.file

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.duplicateComments
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.LogUtils
import java.io.File
import java.io.IOException
import java.io.LineNumberReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern

abstract class AbsIosStringsResourceFile(file: File, private val mLocale: String) : ResourceStreamFile(file) {

    companion object {
        private const val COMMENT_MULTILINE_START_1 = "/**"
        private const val COMMENT_MULTILINE_START_2 = "/*"
        private const val COMMENT_MULTILINE_END = "*/"
        private const val COMMENT_SINGLE_LINE = "//"

        fun toPlatformValue(string: String): String {
            return string
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("%s", "%@")
                    .replace("%([0-9]{1,})\\\$s".toRegex(), "%$1\\$@")
        }

        fun fromPlatformValue(string: String): String {
            return string
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("%@", "%s")
                    .replace("%([0-9]{1,})\\$@".toRegex(), "%$1\\\$s")
        }
    }

    protected abstract val keyValueLinePattern: String
    @Throws(IOException::class)
    protected abstract fun writeKeyValueString(key: String, value: String)

    override val formattingType: FormattingType = JavaFormattingType

    override fun read(extraParams: ExtraParams?): ResMap? {
        if (file.exists()) {
            val result = ResLocale()

            val keyValuePattern = Pattern.compile(keyValueLinePattern)
            val keyValueMatcher = keyValuePattern.matcher("")

            val path = Paths.get(file.toURI())
            try {
                val reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)
                val lineReader = LineNumberReader(reader)


                var lineNumber = 0
                var isComment = false
                var isMultilineComment = false
                var comment: StringBuilder? = null

                var line: String?  = lineReader.readLine()

                while (line != null) {
                    lineNumber++

                    if (!isComment) {
                        var commentSignLength = 0
                        isMultilineComment = false
                        when {
                            line.startsWith(COMMENT_MULTILINE_START_1) -> {
                                isMultilineComment = true
                                commentSignLength = COMMENT_MULTILINE_START_1.length
                            }
                            line.startsWith(COMMENT_MULTILINE_START_2) -> {
                                isMultilineComment = true
                                commentSignLength = COMMENT_MULTILINE_START_2.length
                            }
                            line.startsWith(COMMENT_SINGLE_LINE) -> commentSignLength = COMMENT_SINGLE_LINE.length
                        }
                        if (commentSignLength > 0) {
                            comment = StringBuilder(line.substring(commentSignLength))
                            isComment = true
                        }
                    } else if (isMultilineComment) {
                        comment?.append(line)
                    }

                    if (isMultilineComment && line.endsWith(COMMENT_MULTILINE_END)) {
                        comment?.delete(comment.length - COMMENT_MULTILINE_END.length - 1, comment.length)
                        isMultilineComment = false
                    }

                    if (!isComment) {
                        keyValueMatcher.reset(line)

                        // Workaround for following bug
                        // https://bugs.java.com/view_bug.do?bug_id=6882582
                        var isFind: Boolean
                        try {
                            isFind = keyValueMatcher.find()
                        } catch (e: StackOverflowError) {
                            isFind = false
                            LogUtils.err("Unable to parse line $lineNumber"
                                    + " in resource file: ${this.file.absolutePath}"
                                    + "\nThe line will be removed from this resource file")
                        }

                        if (isFind && keyValueMatcher.groupCount() == 2) {
                            val key = keyValueMatcher.group(1)
                            val value = keyValueMatcher.group(2)

                            val item = ResItem(key)
                            item.addValue(fromPlatformValue(value), comment?.toString()?.trim { it <= ' ' })
                            result.put(item)
                        }
                    }

                    if (!isMultilineComment) {
                        if (isComment)
                            isComment = false
                        else
                            comment = null
                    }

                    line = lineReader.readLine()
                }
            } catch (e: IOException) {
                // Do nothing
                e.printStackTrace()
            }


            val resMap = ResMap()
            resMap[mLocale] = result
            return resMap
        }
        return null
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        open()

        writeStringLn(TemplateStr.GENERATED_KEY_VALUE_PAIR_COMMENT)
        writeln()

        val items = resMap[mLocale]

        var isFirst = true
        items?.keys?.forEach { key ->
            val resItem = items[key]
            if (resItem != null) {
                if (!resItem.isHasQuantities) {

                    if (!isFirst) {
                        writeln()
                        writeln()
                    } else {
                        isFirst = false
                    }

                    val resValue = formattingType.convert(resItem.values[0])
                    val comment = resValue.comment
                    val value = resValue.value

                    if (comment != null && (extraParams == null || extraParams.duplicateComments || comment != value)) {
                        writeString(COMMENT_MULTILINE_START_2)
                        writeString(" ")
                        writeString(comment)
                        writeString(" ")
                        writeString(COMMENT_MULTILINE_END)
                        writeln()
                    }

                    writeKeyValueString(resItem.key, toPlatformValue(value))
                }
            }
        }

        close()
    }

    private fun ResItem.addValue(
            value: String,
            comment: String?
    ) {
        val formattingArguments = formattingType.argumentsFromValue(value)
        val formattingType = if (formattingArguments?.isEmpty() != false) NoFormattingType else formattingType
        this.addValue(ResValue(value, comment, Quantity.OTHER, formattingType, formattingArguments))
    }
}