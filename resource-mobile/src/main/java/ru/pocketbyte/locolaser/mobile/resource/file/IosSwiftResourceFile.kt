/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource.file

import org.apache.commons.lang3.text.WordUtils
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile

import java.io.File
import java.io.IOException

/**
 * Swift class file generator. For generation used Swift version 3.0.
 */
class IosSwiftResourceFile(
        file: File,
        private val mClassName: String,
        private val mTableName: String
) : BaseClassResourceFile(file) {

    companion object {

        private const val CLASS_HEADER_TEMPLATE = ("import Foundation\r\n"
                + "\r\n"
                + "public class %s {\r\n")

        private const val CLASS_FOOTER_TEMPLATE = "}"

        private const val PROPERTY_TEMPLATE = ("    public static var %s : String {\r\n"
                + "        get {\r\n"
                + "            return NSLocalizedString(\"%s\", tableName:\"%s\"," +
                " bundle:Bundle.main, value:\"%s\", comment: \"%s\")\r\n"
                + "        }\r\n"
                + "    }\r\n")

        private const val MAX_LINE_SIZE = 120

        private fun escapeComment(string: String?): String? {
            return string
                    ?.replace("\r", "\\r")
                    ?.replace("\n", "\\n")
                    ?.replace("\u0009", "  ")
        }

        private fun escapeString(string: String?): String? {
            return string
                    ?.replace("\\", "\\\\")
                    ?.replace("\"", "\\\"")
                    ?.replace("\r", "\\r")
                    ?.replace("\n", "\\n")
                    ?.replace("\u0009", "\\t")
        }
    }

    override fun read(extraParams: ExtraParams?): ResMap? {
        return null
    }

    @Throws(IOException::class)
    override fun writeHeaderComment(resMap: ResMap, extraParams: ExtraParams?) {
        writeStringLn(TemplateStr.GENERATED_CLASS_COMMENT)
    }

    @Throws(IOException::class)
    override fun writeClassHeader(resMap: ResMap, extraParams: ExtraParams?) {
        writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName))
    }

    @Throws(IOException::class)
    override fun writeComment(extraParams: ExtraParams?, comment: String) {
        val commentLinePrefix = "    /// "
        writeString(commentLinePrefix)
        writeStringLn(WordUtils.wrap(escapeComment(comment), MAX_LINE_SIZE - commentLinePrefix.length, "\r\n" + commentLinePrefix, true))
    }

    @Throws(IOException::class)
    override fun writeProperty(extraParams: ExtraParams?, propertyName: String, item: ResItem) {
        val valueOther = item.valueForQuantity(Quantity.OTHER)

        val comment = if (valueOther?.comment != null) valueOther.comment else ""

        writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName, item.key, mTableName,
                if (valueOther != null) escapeString(valueOther.value) else null, escapeString(comment)))
    }

    @Throws(IOException::class)
    override fun writeClassFooter(resMap: ResMap, extraParams: ExtraParams?) {
        writeString(CLASS_FOOTER_TEMPLATE)
    }
}
