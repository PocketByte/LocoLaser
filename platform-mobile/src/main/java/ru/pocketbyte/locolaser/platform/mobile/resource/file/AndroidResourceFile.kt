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
 * ResourceFile implementation for Android platform.
 *
 * @author Denis Shurygin
 */
class AndroidResourceFile(file: File, private val mLocale: String) : ResourceStreamFile(file) {

    companion object {

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

        private fun escapeComment(string: String): String {
            return string
                    .replace("<br>", "\n")
                    .replace("<", "&lt;")
        }

        fun toPlatformValue(string: String): String {
            var string = string
                    .replace("'", "\\'")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("<", "&lt;")
            if (string.startsWith("?"))
                string = "\\" + string
            return string
        }

        fun fromPlatformValue(string: String): String {
            var string = string
                    .replace("\\'", "'")
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("&lt;", "<")

            if (string.startsWith("\\?"))
                string = string.substring(1)

            return string
        }
    }

    override fun read(): ResMap? {
        if (file.exists()) {
            val handler = AndroidXmlFileParser()
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
        open()
        writeStringLn(TemplateStr.XML_DECLARATION)
        writeStringLn(TemplateStr.GENERATED_XML_COMMENT)
        writeln()
        writeStringLn("<resources>")

        val items = resMap[mLocale]

        items?.keys?.forEach { key ->
            val resItem = items[key]
            if (resItem != null) {

                if (!resItem.isHasQuantities) {
                    val comment = resItem.values[0].comment
                    val value = resItem.values[0].value
                    if (comment != null && (writingConfig == null || writingConfig.isDuplicateComments || comment != value)) {
                        writeString("    /* ")
                        writeString(escapeComment(comment))
                        writeStringLn(" */")
                    }

                    writeString("    <string name=\"")
                    writeString(resItem.key.trim { it <= ' ' })
                    writeString("\">")
                    writeString(toPlatformValue(value))
                    writeStringLn("</string>")
                } else {
                    writeString("    <plurals name=\"")
                    writeString(resItem.key.trim { it <= ' ' })
                    writeStringLn("\">")

                    for (resValue in resItem.values) {

                        writeString("        <item quantity=\"")
                        writeString(resValue.quantity.toString())
                        writeString("\">")
                        writeString(toPlatformValue(resValue.value))
                        writeStringLn("</item>")
                    }
                    writeStringLn("    </plurals>")
                }
            }
        }

        writeString("</resources>")
        close()
    }

    private class AndroidXmlFileParser : DefaultHandler() {
        var map: ResLocale? = null
            private set
        private var mItem: ResItem? = null
        private var isPlural: Boolean = false
        private var mQuantity: Quantity? = null
        private var mValue: StringBuilder? = null
        private var mComment: String? = null //TODO comments not work

        @Throws(SAXException::class)
        override fun startDocument() {
            map = ResLocale()
        }

        @Throws(SAXException::class)
        override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
            if ("string" == qName) {
                mItem = ResItem(attributes!!.getValue("name"))
                isPlural = false
                mQuantity = null
            } else if ("plurals" == qName) {
                mItem = ResItem(attributes!!.getValue("name"))
                isPlural = true
                mQuantity = null
            } else if (mItem != null && isPlural && "item" == qName) {
                mQuantity = Quantity.fromString(attributes!!.getValue("quantity"))
            } else {
                mItem = null
                isPlural = false
                mQuantity = null
            }
        }

        @Throws(SAXException::class)
        override fun characters(ch: CharArray?, start: Int, length: Int) {
            if (mItem != null && (!isPlural || mQuantity != null)) {
                if (mValue == null)
                    mValue = StringBuilder()
                mValue!!.append(fromPlatformValue(String(ch!!, start, length)))
            }
        }

        @Throws(SAXException::class)
        override fun endElement(uri: String?, localName: String?, qName: String?) {
            if (mItem != null && mValue != null && "string" == qName) {
                mItem!!.addValue(ResValue(mValue!!.toString(), mComment)) // TODO read plurals http://developer.android.com/intl/ru/guide/topics/resources/string-resource.html#Plurals
                map!!.put(mItem)
                mItem = null
            } else if (mItem != null && mValue != null && "item" == qName && isPlural) {
                mItem!!.addValue(ResValue(mValue!!.toString(), mComment, mQuantity))
            } else if (mItem != null && "plurals" == qName && isPlural) {
                map!!.put(mItem)
                mItem = null
            }

            mValue = null
            mComment = null
            mQuantity = null
        }
    }
}
