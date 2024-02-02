/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.json.resource.file

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.entity.Quantity
import ru.pocketbyte.locolaser.json.KeyPluralizationRule
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import ru.pocketbyte.locolaser.resource.formatting.WebFormattingType
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.JSON_LINKED_CONTAINER_FACTORY
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.JSON_PARSER
import ru.pocketbyte.locolaser.utils.json.JsonToStringUtils
import ru.pocketbyte.locolaser.utils.json.LinkedJSONObject
import java.io.*

/**
 * ResourceFile implementation for Android platform.
 *
 * @author Denis Shurygin
 */
class JsonResourceFile(
    private val file: File,
    private val mLocale: String,
    private val indent: Int,
    private val pluralKeyRule: KeyPluralizationRule.Postfix
) : ResourceFile {

    override val formattingType: FormattingType = WebFormattingType

    override fun read(extraParams: ExtraParams?): ResMap? {
        if (!file.exists())
            return null

        val reader: Reader = BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8"))
        val json = JSON_PARSER.parse(reader, JSON_LINKED_CONTAINER_FACTORY)
        reader.close()

        if (json is LinkedJSONObject) {
            return readFromJson(json)
        }

        return null
    }

    private fun readFromJson(json: LinkedJSONObject): ResMap {
        val result = ResLocale()

        getResItemsFromObject("", json).forEach {
            val key = it.key
            val value = it.value

            val pluralPair = pluralKeyRule.decodeKey(key, mLocale)
            if (pluralPair != null) {
                val item = result[pluralPair.first] ?: ResItem(pluralPair.first)
                item.addValue(value, pluralPair.second)
                result.put(item)
            } else {
                val item = result[key] ?: ResItem(key)
                item.addValue(value, Quantity.OTHER)
                result.put(item)
            }
        }

        return ResMap().apply {
            this[mLocale] = result
        }
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, extraParams: ExtraParams?) {
        val locale = resMap[mLocale]
        if (locale?.isNotEmpty() == true) {
            val json = LinkedJSONObject()
            writeToJson(locale, json)

            var writer: Writer? = null
            try {
                file.parentFile.mkdirs()

                writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file), "UTF-8"))
                writer.write(JsonToStringUtils.toJSONString(json, indent) ?: "{}")
                writer.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                writer?.close()
            }
        }
    }

    @Throws(IOException::class)
    private fun writeToJson(locale: ResLocale, json: LinkedJSONObject) {
        for (key in locale.keys) {
            val value = locale[key] ?: continue
            var rootJson = json

            val keyParts = key.split(".")

            for (i in 0..(keyParts.size - 2)) {
                val partKey = keyParts[i]
                val partJson = rootJson[partKey] as? LinkedJSONObject
                    ?: LinkedJSONObject()

                rootJson[partKey] = partJson
                rootJson = partJson
            }

            if (value.isHasQuantities) {
                var isHasAnyQuantity = false
                value.values.forEach {
                    val resValue = formattingType.convert(it)
                    val resKey = pluralKeyRule.encodeKey(keyParts.last(), resValue.quantity, mLocale)

                    if (resKey != null) {
                        rootJson[resKey] = resValue.value
                        isHasAnyQuantity = true
                    }
                }
                if (!isHasAnyQuantity) {
                    pluralKeyRule.encodeKey(keyParts.last(), Quantity.OTHER, mLocale)
                        ?.let { resKey ->
                            value.valueForQuantity(Quantity.OTHER)?.let {
                                rootJson[resKey] = formattingType.convert(it).value
                            }
                        }
                }
            } else {
                rootJson[keyParts.last()] = formattingType.convert(value.values[0]).value
            }
        }
    }

    private fun ResItem.addValue(value: String, quantity: Quantity) {
        val formattingArguments = formattingType.argumentsFromValue(value)
        val formattingType = if (formattingArguments?.isEmpty() != false) {
            NoFormattingType
        } else {
            formattingType
        }
        this.addValue(ResValue(value, null, quantity, formattingType, formattingArguments))
    }

    private fun getResItemsFromObject(
        keyPrefix: String, json: Map<Any?, Any?>
    ): Map<String, String> {
        val result = mutableMapOf<String, String>()

        for (itemKey in json.keys) {
            val value = json.getValue(itemKey)
            if (value is JSONObject) {
                val nextKeyPrefix = if (keyPrefix.isEmpty()) {
                    itemKey as String
                } else {
                    "$keyPrefix.$itemKey"
                }
                result.putAll(getResItemsFromObject(nextKeyPrefix, value))
            } else {
                result[keyPrefix + itemKey] = value.toString()
            }
        }

        return result
    }
}
