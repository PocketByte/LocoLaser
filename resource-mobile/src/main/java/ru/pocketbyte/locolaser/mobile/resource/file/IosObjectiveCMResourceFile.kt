/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource.file

import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.BaseClassResourceFile

import java.io.File
import java.io.IOException

/**
 * Objective-C .m file generator. Part of Objective-C class generation.
 * @author Denis Shurygin
 */
class IosObjectiveCMResourceFile(
        file: File,
        private val mClassName: String,
        private val mTableName: String
) : BaseClassResourceFile(file) {

    companion object {

        private const val CLASS_HEADER_TEMPLATE = ("#import <%s.h>\r\n"
                + "\r\n"
                + "@implementation %s\r\n")

        private const val CLASS_FOOTER_TEMPLATE = "@end"

        private const val PROPERTY_TEMPLATE = "+(NSString*)%s {\r\n" +
                "    return NSLocalizedStringFromTable(@\"%s\", @\"%s\", @\"%s\")\r\n" +
                "}"

        private fun escapeString(string: String?): String? {
            return string
                    ?.replace("\\", "\\\\")
                    ?.replace("\"", "\\\"")
                    ?.replace("\r", "\\r")
                    ?.replace("\n", "\\n")
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
        writeStringLn(String.format(CLASS_HEADER_TEMPLATE, mClassName, mClassName))
    }

    @Throws(IOException::class)
    override fun writeComment(extraParams: ExtraParams?, comment: String) {
        // Do not write comment for .m file
    }

    @Throws(IOException::class)
    override fun writeProperty(extraParams: ExtraParams?, propertyName: String, item: ResItem) {
        val valueOther = item.valueForQuantity(Quantity.OTHER)
        val comment = if (valueOther?.comment != null) valueOther.comment else ""

        writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName, item.key, mTableName, escapeString(comment)))
        writeln()
    }

    @Throws(IOException::class)
    override fun writeClassFooter(resMap: ResMap, extraParams: ExtraParams?) {
        writeString(CLASS_FOOTER_TEMPLATE)
    }

}