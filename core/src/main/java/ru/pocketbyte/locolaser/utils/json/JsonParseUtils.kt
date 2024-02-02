/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils.json

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.ContainerFactory
import org.json.simple.parser.JSONParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.resource.formatting.*
import java.lang.reflect.Field
import java.lang.reflect.Modifier

/**
 * Utils class to parse JSON.
 *
 * @author Denis Shurygin
 */
object JsonParseUtils {

    val JSON_PARSER = JSONParser()

    /**
     * Instance of ContainerFactory that returns LinkedJSONObject instead of JSONObject.
     */
    val JSON_LINKED_CONTAINER_FACTORY = object : ContainerFactory {
        override fun creatArrayContainer(): MutableList<Any?> {
            return JSONArray()
        }

        override fun createObjectContainer(): MutableMap<Any?, Any?> {
            return LinkedJSONObject()
        }
    }

    @Throws(InvalidConfigException::class)
    fun getString(json: JSONObject, key: String, parentKey: String? = null, throwIfNull: Boolean = false): String? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null && jsonObject !is String)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a String.")
        return jsonObject as? String?
    }

    @Throws(InvalidConfigException::class)
    fun getBoolean(json: JSONObject, key: String, parentKey: String? = null, throwIfNull: Boolean = false): Boolean? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null && jsonObject !is Boolean)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a Boolean.")
        return (jsonObject as? Boolean)
    }

    @Throws(InvalidConfigException::class)
    fun getLong(json: JSONObject, key: String, parentKey: String? = null, throwIfNull: Boolean = false): Long? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null && jsonObject !is Long)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a Long.")
        return (jsonObject as? Long)
    }

    @Throws(InvalidConfigException::class)
    fun getJSONObject(json: JSONObject, key: String, parentKey: String? = null, throwIfNull: Boolean = false): JSONObject? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null && jsonObject !is JSONObject)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a JSONObject.")
        return jsonObject as? JSONObject
    }

    @Throws(InvalidConfigException::class)
    fun getStrings(json: JSONObject, key: String, parentKey: String? = null, throwIfNull: Boolean = false): List<String>? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null) {
            if (jsonObject is JSONArray) {
                return jsonObject.map {
                    it as? String
                        ?: throw InvalidConfigException(
                            "Property ${keyName(key, parentKey)} must be a Strings array."
                        )
                }
            } else
                throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a Strings array.")
        }
        return null
    }

    @Throws(InvalidConfigException::class)
    fun getObject(json: JSONObject, key: String, parentKey: String? = null, throwIfNull: Boolean = false): Any? {
        val jsonObject = json[key]
        if (throwIfNull && jsonObject == null)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} is not set.")
        return jsonObject
    }

    fun getFormattingType(json: JSONObject, key: String, parentKey: String? = null, throwIfNull: Boolean = false): FormattingType? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject is String) {
            return when(jsonObject) {
                "java" -> JavaFormattingType
                "web" -> WebFormattingType
                "no" -> NoFormattingType
                else -> {
                    val clazz = Class.forName(jsonObject)
                    val field: Field? = try {
                        clazz.getDeclaredField("INSTANCE")
                    } catch (e: NoSuchFieldException) {
                        null
                    }
                    if (field != null && !Modifier.isPrivate(field.modifiers)) {
                        field.get(null) as FormattingType
                    } else {
                        clazz.getDeclaredConstructor().newInstance() as FormattingType
                    }
                }
            }
        } else if (jsonObject != null) {
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a String.")
        }
        return null
    }

    private fun keyName(key: String, parent: String?): String {
        val builder = StringBuilder("\"")
        if (parent != null)
            builder.append(parent).append(".")
        return builder.append(key).append("\"").toString()
    }
}
