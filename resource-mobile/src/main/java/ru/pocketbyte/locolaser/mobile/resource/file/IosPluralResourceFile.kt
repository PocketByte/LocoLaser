/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource.file

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.PluralUtils
import java.io.File
import java.io.IOException
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

/**
 * @author Denis Shurygin
 */
class IosPluralResourceFile(file: File, private val mLocale: String) : ResourceStreamFile(file) {

    companion object {

        fun toPlatformValue(string: String): String {
            return string
                    .replace("'", "\\'")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("<", "&lt;")
                    .replace("%s", "%@")
        }

        fun fromPlatformValue(string: String): String {
            return string
                    .replace("\\'", "'")
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("%@", "%s")
                    .replace("&lt;", "<")
        }

        private fun convertToFileURL(file: File): String {
            var path = file.absolutePath
            if (File.separatorChar != '/') {
                path = path.replace(File.separatorChar, '/')
            }

            if (!path.startsWith("/")) {
                path = "/$path"
            }
            return "file:$path"
        }

        private const val LEVEL_NONE = 0
        private const val LEVEL_DOC = 1
        private const val LEVEL_ITEM_KEY = 2
        private const val LEVEL_ITEM_DICT = 3
        private const val LEVEL_ITEM_VALUE_DICT = 4
        private const val LEVEL_ITEM_VALUE_KEY = 5
        private const val LEVEL_ITEM_VALUE_STRING = 6

        private const val DEFAULT_PLURAL_FORMATTING = "d"
    }

    override val formattingType: FormattingType = JavaFormattingType

    override fun read(extraParams: ExtraParams?): ResMap? {
        if (file.exists()) {
            val handler = IosXmlFileParser()
            val spf = SAXParserFactory.newInstance()
            try {
                val saxParser = spf.newSAXParser()
                val xmlReader = saxParser.xmlReader
                xmlReader.contentHandler = handler
                xmlReader.parse(convertToFileURL(file))

            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val resMap = ResMap()
            resMap[mLocale] = handler.map
            return resMap
        }
        return null
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        val items = resMap[mLocale]

        items?.keys?.forEach { key ->
            val resItem = items[key]
            if (resItem != null) {
                if (resItem.isHasQuantities) {
                    if (!isOpen) {
                        // Open and write header
                        open()
                        writeStringLn(TemplateStr.XML_DECLARATION)
                        writeStringLn(TemplateStr.GENERATED_XML_COMMENT)
                        writeln()
                        writeStringLn("<plist version=\"1.0\">")
                        writeStringLn("<dict>")
                    }

                    writeString("    <key>")
                    writeString(resItem.key.trim { it <= ' ' })
                    writeStringLn("</key>")

                    writeStringLn("    <dict>")
                    writeStringLn("        <key>NSStringLocalizedFormatKey</key>")
                    writeStringLn("        <string>%#@value@</string>")
                    writeStringLn("        <key>value</key>")
                    writeStringLn("        <dict>")
                    writeStringLn("            <key>NSStringFormatSpecTypeKey</key>")
                    writeStringLn("            <string>NSStringPluralRuleType</string>")
                    writeStringLn("            <key>NSStringFormatValueTypeKey</key>")

                    // Searching format
                    var formattedResValue = resItem.valueForQuantity(Quantity.OTHER)
                    if (formattedResValue?.formatArgumentsIsNotEmpty() != true) {
                        formattedResValue = Quantity.values().find {
                            it != Quantity.OTHER && resItem.valueForQuantity(it)?.formatArgumentsIsNotEmpty() == true
                        }?.let {
                            resItem.valueForQuantity(it)
                        }
                    }

                    var format = formattedResValue?.let { formattingType.convert(it) }
                            ?.formattingArguments
                            ?.firstOrNull()
                            ?.parameters
                            ?.get(JavaFormattingType.PARAM_TYPE_NAME) as? String
                            ?: DEFAULT_PLURAL_FORMATTING

                    if (format == "s") format = "@"

                    writeString("            <string>")
                    writeString(format)
                    writeStringLn("</string>")

                    resItem.values.forEach {
                        val resValue = formattingType.convert(it)
                        writeString("            <key>")
                        writeString(resValue.quantity.toString())
                        writeStringLn("</key>")
                        writeString("            <string>")
                        writeString(toPlatformValue(resValue.value))
                        writeStringLn("</string>")
                    }
                    writeStringLn("        </dict>")
                    writeStringLn("    </dict>")
                }
            }
        }

        if (isOpen) {
            // Close and write footer
            writeStringLn("</dict>")
            writeString("</plist>")
            close()
        }
    }

    private fun ResItem.addValue(
        value: String,
        comment: String?,
        quantity: Quantity = Quantity.OTHER,
        meta: Map<String, String>? = null
    ) {
        val formattingArguments = formattingType.argumentsFromValue(value)
        val formattingType = if (formattingArguments?.isEmpty() != false) NoFormattingType else formattingType
        this.addValue(ResValue(value, comment, quantity, formattingType, formattingArguments, meta))
    }

    private inner class IosXmlFileParser : DefaultHandler() {

        val map = ResLocale()
        private var mItem: ResItem? = null

        private var mValue: StringBuilder? = null

        private var mDocLevel = LEVEL_NONE

        private var mQuantity: Quantity? = null
        private var mComment: String? = null //TODO comments not work

        @Throws(SAXException::class)
        override fun startDocument() { }

        @Throws(SAXException::class)
        override fun startElement(uri: String?, localName: String?, qName: String?,
                                  attributes: Attributes?) {
            when (mDocLevel) {
                LEVEL_NONE -> if ("dict" == qName) {
                    mDocLevel = LEVEL_DOC
                }
                LEVEL_DOC -> if ("key" == qName) {
                    mDocLevel = LEVEL_ITEM_KEY
                    mValue = StringBuilder()
                } else if ("dict" == qName) {
                    mDocLevel = LEVEL_ITEM_DICT
                }
                LEVEL_ITEM_DICT -> if ("dict" == qName) {
                    mDocLevel = LEVEL_ITEM_VALUE_DICT
                }
                LEVEL_ITEM_VALUE_DICT -> if ("key" == qName) {
                    mDocLevel = LEVEL_ITEM_VALUE_KEY
                    mQuantity = null
                    mValue = StringBuilder()
                } else if ("string" == qName) {
                    mDocLevel = LEVEL_ITEM_VALUE_STRING
                    if (mQuantity != null) {
                        mValue = StringBuilder()
                    }
                }
            }
        }

        @Throws(SAXException::class)
        override fun characters(ch: CharArray?, start: Int, length: Int) {
            if (ch == null) return
            when (mDocLevel) {
                LEVEL_ITEM_KEY -> mValue?.append(String(ch, start, length))
                LEVEL_ITEM_VALUE_KEY -> mValue?.append(String(ch, start, length))
                LEVEL_ITEM_VALUE_STRING -> if (mQuantity != null) {
                    mValue?.append(String(ch, start, length))
                }
            }
        }

        @Throws(SAXException::class)
        override fun endElement(uri: String?, localName: String?, qName: String?) {
            when (mDocLevel) {
                LEVEL_ITEM_KEY -> {
                    mItem = ResItem(mValue?.toString() ?: "")
                    mDocLevel = LEVEL_DOC
                }
                LEVEL_ITEM_VALUE_KEY -> {
                    val quantityString = mValue?.toString() ?: ""
                    if ("NSStringFormatSpecTypeKey" == quantityString || "NSStringFormatValueTypeKey" == quantityString) {
                        mQuantity = null
                    } else {
                        mQuantity = PluralUtils.quantityFromString(quantityString)
                    }
                    mDocLevel = LEVEL_ITEM_VALUE_DICT
                }
                LEVEL_ITEM_VALUE_STRING -> {
                    if (mQuantity != null && mItem != null) {
                        mItem?.addValue(fromPlatformValue(mValue?.toString()
                                ?: ""),
                            mComment, mQuantity ?: Quantity.OTHER)
                    }
                    mQuantity = null
                    mDocLevel = LEVEL_ITEM_VALUE_DICT
                }
                LEVEL_ITEM_VALUE_DICT -> mDocLevel = LEVEL_ITEM_DICT
                LEVEL_ITEM_DICT -> if ("dict" == qName) {
                    val item = mItem
                    if (item != null) {
                        map.put(item)
                    }
                    mItem = null
                    mDocLevel = LEVEL_DOC
                }
            }
            mValue = null
            mComment = null
        }
    }
}