/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.resource.file

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.platform.mobile.utils.TemplateStr
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile
import java.io.File
import java.io.IOException
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

/**
 * @author Denis Shurygin
 */
class IosPluralResourceFile(file: File, private val mLocale: String) : ResourceStreamFile(file) {

    companion object {

        private val FORMAT_VARIANTS = arrayOf(
                "%d", "%D", "%u", "%U",
                "%x", "%X", "%o", "%O",
                "%f", "%F", "%e", "%E",
                "%g", "%G", "%a", "%A")

        fun toPlatformValue(string: String): String {
            return string
                    .replace("'", "\\'")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("<", "&lt;")
        }

        fun fromPlatformValue(string: String): String {
            return string
                    .replace("\\'", "'")
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
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
    }

    override fun read(): ResMap? {
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
    override fun write(resMap: ResMap, writingConfig: WritingConfig?) {
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
                    var format = "f"
                    for (formatString in FORMAT_VARIANTS) {
                        if (resItem.valueForQuantity(Quantity.OTHER)!!.value.contains(formatString)) {
                            format = formatString.substring(1)
                            break
                        }
                    }
                    writeString("            <string>")
                    writeString(format)
                    writeStringLn("</string>")

                    for (resValue in resItem.values) {

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
        }
    }

    private class IosXmlFileParser : DefaultHandler() {

        companion object {
            private const val LEVEL_NONE = 0
            private const val LEVEL_DOC = 1
            private const val LEVEL_ITEM_KEY = 2
            private const val LEVEL_ITEM_DICT = 3
            private const val LEVEL_ITEM_VALUE_DICT = 4
            private const val LEVEL_ITEM_VALUE_KEY = 5
            private const val LEVEL_ITEM_VALUE_STRING = 6
        }

        var map: ResLocale? = null
            private set
        private var mItem: ResItem? = null

        private var mValue: StringBuilder? = null

        private var mDocLevel = LEVEL_NONE

        private var mQuantity: Quantity? = null
        private var mComment: String? = null //TODO comments not work

        @Throws(SAXException::class)
        override fun startDocument() {
            map = ResLocale()
        }

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
            when (mDocLevel) {
                LEVEL_ITEM_KEY -> mValue!!.append(String(ch!!, start, length))
                LEVEL_ITEM_VALUE_KEY -> mValue!!.append(String(ch!!, start, length))
                LEVEL_ITEM_VALUE_STRING -> if (mQuantity != null) {
                    mValue!!.append(String(ch!!, start, length))
                }
            }
        }

        @Throws(SAXException::class)
        override fun endElement(uri: String?, localName: String?, qName: String?) {
            when (mDocLevel) {
                LEVEL_ITEM_KEY -> {
                    mItem = ResItem(mValue!!.toString())
                    mDocLevel = LEVEL_DOC
                }
                LEVEL_ITEM_VALUE_KEY -> {
                    val quantityString = mValue!!.toString()
                    if ("NSStringFormatSpecTypeKey" == quantityString || "NSStringFormatValueTypeKey" == quantityString) {
                        mQuantity = null
                    } else {
                        mQuantity = Quantity.fromString(quantityString, null)
                    }
                    mDocLevel = LEVEL_ITEM_VALUE_DICT
                }
                LEVEL_ITEM_VALUE_STRING -> {
                    if (mQuantity != null && mItem != null) {
                        mItem!!.addValue(ResValue(fromPlatformValue(mValue!!.toString()), mComment, mQuantity))
                    }
                    mQuantity = null
                    mDocLevel = LEVEL_ITEM_VALUE_DICT
                }
                LEVEL_ITEM_VALUE_DICT -> mDocLevel = LEVEL_ITEM_DICT
                LEVEL_ITEM_DICT -> if ("dict" == qName) {
                    if (mItem != null) {
                        map!!.put(mItem)
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