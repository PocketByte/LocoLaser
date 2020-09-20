/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource.file

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.config.duplicateComments
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
            var result = string
                    .replace("'", "\\'")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("@", "\\@")
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
            if (result.startsWith("?"))
                result = "\\" + result
            return result
        }

        fun fromPlatformValue(string: String): String {
            var result = string
                    .replace("\\'", "'")
                    .replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("\\@", "@")
                    .replace("&amp;", "&")
                    .replace("&lt;", "<")

            if (result.startsWith("\\?"))
                result = result.substring(1)

            return result
        }

        public const val META_CDATA = "xml-cdata"
        public const val META_CDATA_ON = "true"

        public const val META_FORMATTED = "formatted"
        public const val META_FORMATTED_ON = "true"
        public const val META_FORMATTED_OFF = "false"

        public const val XML_CDATA_PREFIX = "<![CDATA["
        public const val XML_CDATA_POSTFIX = "]]>"
    }

    override val formattingType: FormattingType = JavaFormattingType

    override fun read(extraParams: ExtraParams?): ResMap? {
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
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
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
                    val resValue = formattingType.convert(resItem.values[0])
                    val isCDATA = META_CDATA_ON == resValue.meta?.get(META_CDATA)
                    val comment = resValue.comment
                    val value = resValue.value
                    if (comment != null && (extraParams == null || extraParams.duplicateComments || comment != value)) {
                        writeString("    /* ")
                        writeString(escapeComment(comment))
                        writeStringLn(" */")
                    }

                    writeString("    <string name=\"")
                    writeString(resItem.key.trim { it <= ' ' })
                    writeString("\"")

                    when(resValue.meta?.get(META_FORMATTED)) {
                        META_FORMATTED_ON -> writeString(" formatted=\"true\"")
                        META_FORMATTED_OFF -> writeString(" formatted=\"false\"")
                        else -> { /* Do nothing */ }
                    }

                    writeString(">")

                    if (isCDATA) writeString(XML_CDATA_PREFIX)
                    writeString(toPlatformValue(value))
                    if (isCDATA) writeString(XML_CDATA_POSTFIX)
                    writeStringLn("</string>")
                } else {
                    writeString("    <plurals name=\"")
                    writeString(resItem.key.trim { it <= ' ' })
                    writeStringLn("\">")

                    resItem.values.forEach {
                        val resValue = formattingType.convert(it)
                        val isCDATA = META_CDATA_ON == resValue.meta?.get(META_CDATA)

                        writeString("        <item quantity=\"")
                        writeString(resValue.quantity.toString())
                        writeString("\">")
                        if (isCDATA) writeString(XML_CDATA_PREFIX)
                        writeString(toPlatformValue(resValue.value))
                        if (isCDATA) writeString(XML_CDATA_POSTFIX)
                        writeStringLn("</item>")
                    }
                    writeStringLn("    </plurals>")
                }
            }
        }

        writeString("</resources>")
        close()
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

    private inner class AndroidXmlFileParser : DefaultHandler() {
        val map: ResLocale = ResLocale()

        private var mItem: ResItem? = null
        private var isPlural: Boolean = false
        private var mQuantity: Quantity? = null
        private var mValue: StringBuilder? = null
        private var mComment: String? = null //TODO comments not work
        private var mMetaData: Map<String, String>? = null

        @Throws(SAXException::class)
        override fun startDocument() { }

        @Throws(SAXException::class)
        override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
            if ("string" == qName) {
                mItem = ResItem(attributes!!.getValue("name"))
                isPlural = false
                mQuantity = null
                mMetaData = getMetaFromAttributes(qName, attributes)
            } else if ("plurals" == qName) {
                mItem = ResItem(attributes!!.getValue("name"))
                isPlural = true
                mQuantity = null
            } else if (mItem != null && isPlural && "item" == qName) {
                mQuantity = PluralUtils.quantityFromString(attributes!!.getValue("quantity"))
                mMetaData = getMetaFromAttributes(qName, attributes)
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
            val item = mItem
            val value = mValue

            if (item != null && value != null && "string" == qName) {
                item.addValue(mValue!!.toString(), mComment, meta = mMetaData)
                map.put(item)
                mItem = null
                mMetaData = null
            } else if (item != null && value != null && "item" == qName && isPlural) {
                item.addValue(value.toString(), mComment, mQuantity ?: Quantity.OTHER, meta = mMetaData)
                mMetaData = null
            } else if (item != null && "plurals" == qName && isPlural) {
                map.put(item)
                mItem = null
            }

            mValue = null
            mComment = null
            mQuantity = null
        }

        private fun getMetaFromAttributes(qName: String?, attributes: Attributes?): Map<String, String>? {
            if (attributes == null)
                return null

            val map = mutableMapOf<String, String>()

            if (qName == "string") {
                val formatted = when (attributes.getValue("formatted")) {
                    "true" -> META_FORMATTED_ON
                    "false" -> META_FORMATTED_OFF
                    else -> null
                }

                if (formatted != null)
                    map[META_FORMATTED] = formatted
            }

            return if (map.isNotEmpty()) map else null
        }
    }
}
