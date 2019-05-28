/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils

import ru.pocketbyte.locolaser.exception.InvalidConfigException
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

import java.io.File
import java.io.IOException

/**
 * Utils class to parse JSON.
 *
 * @author Denis Shurygin
 */
object JsonParseUtils {

    val JSON_PARSER = JSONParser()

    @Throws(InvalidConfigException::class)
    fun getString(json: JSONObject, key: String, parentKey: String?, throwIfNull: Boolean): String? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null && jsonObject !is String)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a String.")
        return jsonObject as String?
    }

    @Throws(InvalidConfigException::class)
    fun getBoolean(json: JSONObject, key: String, parentKey: String?, throwIfNull: Boolean): Boolean {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null && jsonObject !is Boolean)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a Boolean.")
        return (jsonObject as? Boolean) ?: false
    }

    @Throws(InvalidConfigException::class)
    fun getLong(json: JSONObject, key: String, parentKey: String?, throwIfNull: Boolean): Long {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null && jsonObject !is Long)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a Long.")
        return (jsonObject as? Long) ?: 0L
    }

    @Throws(InvalidConfigException::class)
    fun getJSONObject(json: JSONObject, key: String, parentKey: String?, throwIfNull: Boolean): JSONObject? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null && jsonObject !is JSONObject)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a JSONObject.")
        return jsonObject as? JSONObject
    }

    @Throws(InvalidConfigException::class)
    fun getStrings(json: JSONObject, key: String, parentKey: String?, throwIfNull: Boolean): List<String>? {
        val jsonObject = getObject(json, key, parentKey, throwIfNull)
        if (jsonObject != null) {
            if (jsonObject is JSONArray) {
                (jsonObject as JSONArray?)!!
                        .filter { it !is String }
                        .forEach { _ ->
                            throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a Strings array.")
                        }
                return jsonObject as? List<String>
            } else
                throw InvalidConfigException("Property ${keyName(key, parentKey)} must be a Strings array.")
        }
        return null
    }

    @Throws(InvalidConfigException::class)
    fun getFile(json: JSONObject, key: String, parentKey: String?, throwIfNull: Boolean): File? {
        val path = getString(json, key, parentKey, throwIfNull)
        if (path != null)
            try {
                return File(File(path).canonicalPath)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        return null
    }

    @Throws(InvalidConfigException::class)
    fun getObject(json: JSONObject, key: String, parentKey: String?, throwIfNull: Boolean): Any? {
        val jsonObject = json[key]
        if (throwIfNull && jsonObject == null)
            throw InvalidConfigException("Property ${keyName(key, parentKey)} is not set.")
        return jsonObject
    }

    private fun keyName(key: String, parent: String?): String {
        val builder = StringBuilder("\"")
        if (parent != null)
            builder.append(parent).append(".")
        return builder.append(key).append("\"").toString()
    }
}
