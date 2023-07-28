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
 * Objective-C .h file generator. Part of Objective-C class generation.
 * @author Denis Shurygin
 */
class IosObjectiveCHResourceFile(file: File, private val mClassName: String) : BaseClassResourceFile(file) {

    companion object {

        private const val CLASS_HEADER_TEMPLATE = ("#import <Foundation/Foundation.h>\r\n"
                + "\r\n"
                + "@interface %s : NSObject\r\n")

        private const val CLASS_FOOTER_TEMPLATE = "@end"

        private const val PROPERTY_TEMPLATE = "@property (class, readonly) NSString* %s;\r\n"

        private const val MAX_LINE_SIZE = 120

        private fun escapeComment(string: String): String {
            return string
                    .replace("\r", "\\r")
                    .replace("\n", "\\n")
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
        val commentLinePrefix = "/// "
        writeString(commentLinePrefix)
        writeStringLn(WordUtils.wrap(escapeComment(comment), MAX_LINE_SIZE - commentLinePrefix.length, "\r\n" + commentLinePrefix, true))
    }

    @Throws(IOException::class)
    override fun writeProperty(extraParams: ExtraParams?, propertyName: String, item: ResItem) {
        writeStringLn(String.format(PROPERTY_TEMPLATE, propertyName))
    }

    @Throws(IOException::class)
    override fun writeClassFooter(resMap: ResMap, extraParams: ExtraParams?) {
        writeString(CLASS_FOOTER_TEMPLATE)
    }

}