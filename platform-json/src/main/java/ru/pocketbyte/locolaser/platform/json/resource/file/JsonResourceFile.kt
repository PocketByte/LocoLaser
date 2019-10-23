/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.json.resource.file

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.WritingConfig
import ru.pocketbyte.locolaser.resource.entity.*
import ru.pocketbyte.locolaser.resource.file.ResourceFile
import ru.pocketbyte.locolaser.utils.LogUtils
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.JSON_LINKED_CONTAINER_FACTORY
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils.JSON_PARSER
import ru.pocketbyte.locolaser.utils.json.LinkedJSONObject
import ru.pocketbyte.locolaser.utils.PluralUtils
import ru.pocketbyte.locolaser.utils.json.JsonToStringUtils
import java.io.*
import java.util.regex.Pattern
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.BufferedReader



/**
 * ResourceFile implementation for Android platform.
 *
 * @author Denis Shurygin
 */
class JsonResourceFile(
        private val file: File,
        private val mLocale: String,
        private val indent: Int
) : ResourceFile {

    companion object {

        private const val PLURAL_0_KEY_POSTFIX = "_plural"
        private const val PLURAL_KEY_POSTFIX = "_plural_"
        private const val PLURAL_KEY_PATTERN = "(.+)$PLURAL_KEY_POSTFIX([0-9]+)"

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

    override fun read(): ResMap? {
        if (!file.exists())
            return null

        val pluralMatcher = Pattern.compile(PLURAL_KEY_PATTERN).matcher("")
        val reader: Reader = BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8"))
        val json = JSON_PARSER.parse(reader, JSON_LINKED_CONTAINER_FACTORY)
        reader.close()

        if (json is LinkedJSONObject) {
            return ResMap().apply {
                val result = ResLocale()

                getResItemsFromObject("", json).forEach {
                    val key = it.key
                    val value = it.value

                    val pluralMatch = pluralMatcher.reset(key)
                    if (pluralMatch.find() && pluralMatch.groupCount() == 2) {
                        val quantityIndex = pluralMatch.group(2).toIntOrNull()
                        val quantity = PluralUtils.quantityFromIndex(quantityIndex, mLocale) ?: Quantity.OTHER
                        val pluralKey = pluralMatch.group(1)

                        val item = result[pluralKey] ?: ResItem(pluralKey)
                        item.addValue(ResValue(value, null, quantity))
                        result.put(item)
                    } else if (key.endsWith(PLURAL_0_KEY_POSTFIX)) {
                        val pluralKey = key.substring(0, key.length - PLURAL_0_KEY_POSTFIX.length)

                        val item = result[pluralKey] ?: ResItem(pluralKey)
                        item.addValue(ResValue(value, null, Quantity.OTHER))
                        result.put(item)
                    } else {
                        val item = result[key] ?: ResItem(key)
                        item.addValue(ResValue(value, null, Quantity.OTHER))
                        result.put(item)
                    }
                }

                this[mLocale] = result
            }
        }

        return null
    }

    @Throws(IOException::class)
    override fun write(resMap: ResMap, writingConfig: WritingConfig?) {
        val locale = resMap[mLocale]
        if (locale?.isNotEmpty() == true) {
            val json = LinkedJSONObject()

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
                        val index = PluralUtils.quantityIndexForLocale(it.quantity, mLocale)
                        if (index != null) {
                            rootJson[keyParts.last() + PLURAL_KEY_POSTFIX + index] = it.value
                            isHasAnyQuantity = true
                        }
                    }
                    if (!isHasAnyQuantity)
                        value.valueForQuantity(Quantity.OTHER)?.value?.let {
                            rootJson[keyParts.last() + PLURAL_KEY_POSTFIX + 0] = it
                        }
                } else {
                    rootJson[keyParts.last()] = value.values[0].value
                }
            }

            var writer: Writer? = null
            try {
                file.parentFile.mkdirs()



                writer = BufferedWriter(OutputStreamWriter(FileOutputStream(file), "UTF-8"))
                writer.write(JsonToStringUtils.toJSONString(json, indent))
                writer.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                writer?.close()
            }
        }
    }

    private fun getResItemsFromObject(keyPrefix: String, json: Map<Any?, Any?>): Map<String, String> {
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
