/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.gettext.resource.file

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResLocale
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.ResValue
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.commentShouldBeWritten
import java.io.File
import java.io.IOException
import java.io.LineNumberReader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * ResourceFile implementation for Android platform.
 *
 * @author Denis Shurygin
 */
class GetTextResourceFile(file: File, private val mLocale: String) : ResourceStreamFile(file) {

    companion object {

        const val GENERATED_GETTEXT_COMMENT = "# AUTO-GENERATED FILE. DO NOT MODIFY.\r\n" +
                "#\r\n" +
                "# This file was automatically generated by the LocoLaser tool.\r\n" +
                "# It should not be modified by hand.\r\n\r\n" +
                "msgid \"\"\r\n" +
                "msgstr \"\"\r\n" +
                "\"MIME-Version: 1.0\\n\"\r\n" +
                "\"Content-Type: text/plain; charset=utf-8\\n\"\r\n" +
                "\"Content-Transfer-Encoding: 8bit\\n\"\r\n" +
                "\"Generated-By: LocoLaser\\n\""

        private const val COMMENT_SINGLE_LINE = "#"
        private const val KEY_LINE_PATTERN = "msgid \"((?:[^\"]|\\\\\")*)\"\\s*"
        private const val VALUE_LINE_PATTERN = "msgstr \"((?:[^\"]|\\\\\")*)\"\\s*"
        private const val VALUE_SECOND_LINE_PATTERN = "^[^\\S]*\"((?:[^\"]|\\\\\")*)\"\\s*"

        internal fun toPlatformValue(string: String): String {
            return string
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
        }

        internal fun fromPlatformValue(string: String): String {
            return string
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
        }
    }

    override val formattingType = NoFormattingType

    override fun read(extraParams: ExtraParams?): ResMap? {
        if (file.exists()) {
            val result = ResLocale()

            val keyMatcher = Pattern.compile(KEY_LINE_PATTERN).matcher("")
            val valueMatcher = Pattern.compile(VALUE_LINE_PATTERN).matcher("")
            val valueSecondMatcher = Pattern.compile(VALUE_SECOND_LINE_PATTERN).matcher("")

            val path = Paths.get(file.toURI())
            try {
                val reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)
                val lineReader = LineNumberReader(reader)

                var line: String?
                var comment: StringBuilder? = null
                var key: String? = null
                var value: StringBuilder? = null

                do {
                    line = lineReader.readLine()

                    if (key != null) {
                        if (value == null) {
                            if (line != null) {
                                valueMatcher.reset(line)
                                if (valueMatcher.find() && valueMatcher.groupCount() == 1) {
                                    value = StringBuilder(valueMatcher.group(1))
                                }
                            }
                        } else {
                            if (line != null)
                                valueSecondMatcher.reset(line)

                            if (line != null && valueSecondMatcher.find() && valueSecondMatcher.groupCount() == 1) {
                                value.append(valueSecondMatcher.group(1))
                            } else {
                                val item = ResItem(key)
                                item.addValue(ResValue(
                                        fromPlatformValue(value.toString()),
                                        comment?.toString()?.trim { it <= ' ' }))
                                result.put(item)

                                comment = null
                                key = null
                                value = null
                            }
                        }
                    }

                    if (key == null) {
                        if (line?.startsWith(COMMENT_SINGLE_LINE) == true) {
                            if (comment == null)
                                comment = StringBuilder(line.substring(COMMENT_SINGLE_LINE.length))
                            else {
                                comment.append("\n")
                                comment.append(line.substring(COMMENT_SINGLE_LINE.length))
                            }
                        } else if (line != null) {
                            keyMatcher.reset(line)
                            if (keyMatcher.find() && keyMatcher.groupCount() == 1) {
                                key = keyMatcher.group(1)
                                if (key?.isNotEmpty() == false) {
                                    comment = null
                                    key = null
                                }
                                value = null
                            } else {
                                comment = null
                            }
                        }
                    }
                } while (line != null)
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

        writeStringLn(GENERATED_GETTEXT_COMMENT)
        writeln()

        val items = resMap[mLocale]

        var isFirst = true
        items?.keys?.forEach { key ->
            val resItem = items[key]
            if (resItem != null) {
                if (!isFirst) {
                    writeln()
                    writeln()
                } else {
                    isFirst = false
                }

                val resValue = formattingType.convert(resItem.values[0])
                val value = resValue.value

                if (commentShouldBeWritten(resValue, extraParams)) {
                    writeString(COMMENT_SINGLE_LINE)
                    writeString(" ")
                    writeString(resValue.comment ?: "")
                    writeln()
                }
                writeString("msgid \"")
                writeString(resItem.key)
                writeString("\"")
                writeln()

                writeString("msgstr \"")
                writeString(toPlatformValue(value))
                writeString("\"")
            }
        }

        close()
    }
}
