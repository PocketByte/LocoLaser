/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.resource.file

import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr.GENERATED_COMMENT_STRINGS
import ru.pocketbyte.locolaser.mobile.utils.TemplateStr.GENERATED_NEW_LINE
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceStreamFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.utils.PluralUtils
import ru.pocketbyte.locolaser.utils.commentShouldBeWritten
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import javax.xml.namespace.QName
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamConstants
import javax.xml.stream.XMLStreamWriter
import javax.xml.stream.events.Characters
import javax.xml.stream.events.Comment
import javax.xml.stream.events.EndElement
import javax.xml.stream.events.StartElement

/**
 * ResourceFile implementation for Android platform.
 *
 * @author Denis Shurygin
 */
class AndroidResourceFile(file: File, private val mLocale: String) : ResourceStreamFile(file) {

    companion object {

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

        public const val INDENT = "    "

        public const val META_CDATA = "xml-cdata"
        public const val META_CDATA_ON = "true"

        public const val META_FORMATTED = "formatted"
        public const val META_FORMATTED_ON = "true"
        public const val META_FORMATTED_OFF = "false"

        public const val ELEMENT_RESOURCES = "resources"
        public const val ELEMENT_STRING = "string"
        public const val ELEMENT_PLURALS_ITEM = "item"
        public const val ELEMENT_PLURALS = "plurals"

        public const val ATTRIBUTE_FORMATTED = "formatted"
        public const val ATTRIBUTE_NAME = "name"
        public const val ATTRIBUTE_QUANTITY = "quantity"
    }

    override val formattingType: FormattingType = JavaFormattingType

    override fun read(extraParams: ExtraParams?): ResMap? {
        if (file.exists().not()) {
            return null
        }

        val resBuilder = ResBuilder()
        val reader = XMLInputFactory.newInstance()
            .createXMLEventReader(FileInputStream(file))

        while (reader.hasNext()) {
            val event = reader.nextEvent()

            when (event.eventType) {
                XMLStreamConstants.START_DOCUMENT -> {
                    resBuilder.onStartDocument()
                }
                XMLStreamConstants.END_DOCUMENT -> {
                    resBuilder.onEndDocument()
                }
                XMLStreamConstants.START_ELEMENT -> {
                    resBuilder.onStartElement(event.asStartElement())
                }
                XMLStreamConstants.END_ELEMENT -> {
                    resBuilder.onEndElement(event.asEndElement())
                }
                XMLStreamConstants.CHARACTERS -> {
                    resBuilder.onCharacters(event.asCharacters())
                }
                XMLStreamConstants.COMMENT -> {
                    resBuilder.onComment((event as Comment).text.trim())
                }
                XMLStreamConstants.SPACE -> {
                    resBuilder.onSpace()
                }
                else -> {
                    // Do nothing
                }
            }
        }

        reader.close()

        return resBuilder.build()
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        file.parentFile.mkdirs()

        val writer = XMLOutputFactory.newInstance().createXMLStreamWriter(FileOutputStream(file))

        writer.writeStartDocument("utf-8", "1.0")
        writer.writeNewLine()

        GENERATED_COMMENT_STRINGS.forEach {
            writer.writeComment(" $it ")
            writer.writeNewLine()
        }

        writer.writeNewLine()
        writer.writeStartElement(ELEMENT_RESOURCES)

        val items = resMap[mLocale]

        var indentLevel = 1
        items?.keys?.forEach { key ->
            val resItem = items[key]
            if (resItem != null) {
                writer.writeNewLine(indentLevel)
                if (!resItem.isHasQuantities) {
                    val resValue = formattingType.convert(resItem.values[0])
                    val isCDATA = META_CDATA_ON == resValue.meta?.get(META_CDATA)
                    val value = resValue.value

                    if (commentShouldBeWritten(resValue, extraParams)) {
                        writer.writeComment(" ${resValue.comment ?: ""} ")
                        writer.writeNewLine(indentLevel)
                    }

                    writer.writeStartElement(ELEMENT_STRING)
                    writer.writeAttribute(ATTRIBUTE_NAME, resItem.key.trim { it <= ' ' })
                    writer.writeAttributes(resValue)

                    if (isCDATA) {
                        writer.writeCData(toPlatformValue(value))
                    } else {
                        writer.writeCharacters(toPlatformValue(value))
                    }

                    writer.writeEndElement()
                } else {
                    writer.writeStartElement(ELEMENT_PLURALS)
                    writer.writeAttribute(ATTRIBUTE_NAME, resItem.key.trim { it <= ' ' })

                    indentLevel += 1
                    resItem.values.forEach {
                        writer.writeNewLine(indentLevel)
                        val resValue = formattingType.convert(it)
                        val isCDATA = META_CDATA_ON == resValue.meta?.get(META_CDATA)
                        val value = resValue.value

                        if (commentShouldBeWritten(resValue, extraParams)) {
                            writer.writeComment(" ${resValue.comment ?: ""} ")
                            writer.writeNewLine(indentLevel)
                        }

                        writer.writeStartElement(ELEMENT_PLURALS_ITEM)
                        writer.writeAttribute(ATTRIBUTE_QUANTITY, resValue.quantity.toString())

                        if (isCDATA) {
                            writer.writeCData(toPlatformValue(value))
                        } else {
                            writer.writeCharacters(toPlatformValue(value))
                        }

                        writer.writeEndElement()
                    }
                    indentLevel -= 1
                    writer.writeNewLine(indentLevel)
                    writer.writeEndElement()
                }
            }
        }

        writer.writeNewLine()
        writer.writeEndElement()
        writer.writeEndDocument()

        writer.flush()
        writer.close()
    }

    private inner class ResBuilder {
        val map: ResLocale = ResLocale()

        private var activeItem: ResItem? = null
        private var isActivePlural: Boolean = false
        private var activeQuantity: Quantity? = null
        private var activeValue: StringBuilder? = null
        private var activeComment: String? = null
        private var activeMetaData: Map<String, String>? = null

        private var isDocumentStart = false
        private var isResourcesElement = false

        fun build(): ResMap {
            val resMap = ResMap()
            resMap[mLocale] = map
            return resMap
        }

        fun onStartDocument() {
            isDocumentStart = true
        }

        fun onEndDocument() {
            isDocumentStart = false
        }

        fun onStartElement(startElement: StartElement) {
            if (!isDocumentStart) {
                return
            }

            val name = startElement.name.localPart

            when {
                ELEMENT_RESOURCES == name -> {
                    isResourcesElement = true
                    activeItem = null
                    isActivePlural = false
                    activeQuantity = null
                    activeComment = null
                }
                !isResourcesElement -> {
                    return
                }
                ELEMENT_STRING == name -> {
                    activeItem = ResItem(
                        startElement.getAttributeValue(ATTRIBUTE_NAME) ?: return
                    )
                    isActivePlural = false
                    activeQuantity = null
                    activeMetaData = getMetaFromAttributes(startElement)
                }
                ELEMENT_PLURALS == name -> {
                    activeItem = ResItem(
                        startElement.getAttributeValue(ATTRIBUTE_NAME) ?: return
                    )
                    isActivePlural = true
                    activeQuantity = null
                }
                ELEMENT_PLURALS_ITEM == name && activeItem != null && isActivePlural -> {
                    activeQuantity = PluralUtils.quantityFromString(
                        startElement.getAttributeValue(ATTRIBUTE_QUANTITY)
                    )
                    activeMetaData = getMetaFromAttributes(startElement)
                }
                else -> {
                    activeItem = null
                    isActivePlural = false
                    activeQuantity = null
                    activeComment = null
                }
            }
        }

        fun onCharacters(characters: Characters) {
            if (activeItem != null && (!isActivePlural || activeQuantity != null)) {
                if (activeValue == null)
                    activeValue = StringBuilder()
                activeValue?.append(fromPlatformValue(characters.data))
            }
        }

        fun onEndElement(endElement: EndElement) {
            if (!isDocumentStart || !isResourcesElement) {
                return
            }

            val item = activeItem
            val value = activeValue

            val name = endElement.name.localPart

            when {
                ELEMENT_RESOURCES == name -> {
                    isResourcesElement = false
                }
                ELEMENT_STRING == name && item != null && value != null -> {
                    item.addValue(value.toString(), activeComment, meta = activeMetaData)
                    map.put(item)
                    activeItem = null
                    activeMetaData = null
                }
                ELEMENT_PLURALS_ITEM == name && item != null && value != null && isActivePlural -> {
                    item.addValue(value.toString(), activeComment, activeQuantity, activeMetaData)
                    activeMetaData = null
                }
                ELEMENT_PLURALS == name && item != null && isActivePlural -> {
                    map.put(item)
                    activeItem = null
                }
            }

            activeValue = null
            activeComment = null
            activeQuantity = null
        }

        fun onComment(comment: String) {
            activeComment = comment
        }

        fun onSpace() {
            activeComment = null
        }

        private fun getMetaFromAttributes(startElement: StartElement): Map<String, String>? {
            if (startElement.attributes.hasNext().not())
                return null

            val map = mutableMapOf<String, String>()

            if (startElement.name.localPart == ELEMENT_STRING) {
                val formatted = startElement.getAttributeValue(ATTRIBUTE_FORMATTED)

                if (!formatted.isNullOrBlank())
                    map[META_FORMATTED] = formatted
            }

            return map.ifEmpty { null }
        }

        private fun ResItem.addValue(
            value: String,
            comment: String?,
            quantity: Quantity? = null,
            meta: Map<String, String>? = null
        ) {
            val formattingArguments = formattingType.argumentsFromValue(value)
            val formattingType = if (formattingArguments?.isEmpty() != false) {
                NoFormattingType
            } else {
                formattingType
            }
            this.addValue(
                ResValue(
                    value = value,
                    comment = comment,
                    quantity = quantity ?: Quantity.OTHER,
                    formattingType = formattingType,
                    formattingArguments = formattingArguments,
                    meta = meta
                )
            )
        }
    }
}

private fun StartElement.getAttributeValue(name: String): String? {
    return getAttributeByName(QName.valueOf(name))?.value
}

private fun XMLStreamWriter.writeAttributes(resValue: ResValue) {
    resValue.meta?.get(AndroidResourceFile.META_FORMATTED)?.let {
        writeAttribute(AndroidResourceFile.ATTRIBUTE_FORMATTED, it)
    }
}

private fun XMLStreamWriter.writeIndents(count: Int) {
    for (i in 0 until count) {
        writeCharacters(AndroidResourceFile.INDENT)
    }
}

private fun XMLStreamWriter.writeNewLine(indent: Int = 0) {
    writeCharacters(GENERATED_NEW_LINE)
    writeIndents(indent)
}
