/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.SOURCE
import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig
import ru.pocketbyte.locolaser.config.source.SourceConfig
import ru.pocketbyte.locolaser.config.source.SourceSetConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

import java.util.HashSet

/**
 * @author Denis Shurygin
 */
abstract class BaseTableSourceConfigParser<SourceConfigClass : BaseTableSourceConfig> : SourceConfigParser<SourceConfig> {

    companion object {
        const val TYPE = "type"
        const val COLUMN_KEY = "column_key"
        const val COLUMN_QUANTITY = "column_quantity"
        const val COLUMN_LOCALES = "column_locales"
        const val COLUMN_COMMENT = "column_comment"
    }

    /**
     * Creates a new Source config object by it's name.
     *
     * @param type Source type.
     * @return Source object.
     * @throws InvalidConfigException if source type is unknown.
     */
    @Throws(InvalidConfigException::class)
    protected abstract fun sourceByType(type: String?): SourceConfigClass

    /**
     * Parse Source from JSON object.
     *
     * @param sourceObject JSON object that contain source config properties.
     * @return Parsed source object.
     * @throws InvalidConfigException
     */
    @Throws(InvalidConfigException::class)
    override fun parse(sourceObject: Any?, throwIfWrongType: Boolean): SourceConfig? {
        if (sourceObject is JSONObject) {
            return parseFromJson(sourceObject)
        } else if (sourceObject is JSONArray) {
            var defaultConfig: SourceConfig? = null
            val configs = HashSet<SourceConfig>(sourceObject.size)
            for (item in sourceObject) {
                if (item is JSONObject) {
                    val config = parseFromJson(item)
                    configs.add(config)

                    if (defaultConfig == null)
                        defaultConfig = config
                } else
                    throw InvalidConfigException("Source array must contain JSONObjects.")
            }
            return SourceSetConfig(configs, defaultConfig)
        } else if (throwIfWrongType) {
            throw InvalidConfigException("Source must be a JSONObject or JSONArray.")
        }
        return null
    }

    @Throws(InvalidConfigException::class)
    protected fun parseFromJson(configJson: JSONObject): SourceConfigClass {
        val type = JsonParseUtils.getString(configJson, TYPE, SOURCE, false)
        val source = sourceByType(type)
        fillFromJSON(source, configJson)
        validate(source)
        return source
    }

    /**
     * Fill source object from JSON.
     *
     * @param source      Source to fill.
     * @param configJson JSON object that contain source config properties.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     */
    @Throws(InvalidConfigException::class)
    protected open fun fillFromJSON(source: SourceConfigClass, configJson: JSONObject) {
        source.keyColumn = JsonParseUtils.getString(configJson, COLUMN_KEY, SOURCE, true)
        source.quantityColumn = JsonParseUtils.getString(configJson, COLUMN_QUANTITY, SOURCE, false)
        source.commentColumn = JsonParseUtils.getString(configJson, COLUMN_COMMENT, SOURCE, false)

        val locales = JsonParseUtils.getStrings(configJson, COLUMN_LOCALES, SOURCE, true)
        if (locales != null)
            source.localeColumns = HashSet(locales)
    }

    @Throws(InvalidConfigException::class)
    protected open fun validate(source: SourceConfigClass) {
        if (source.keyColumn?.isEmpty() != false)
            throw InvalidConfigException("\"$SOURCE.$COLUMN_KEY\" is not set.")
        if (source.localeColumns == null)
            throw InvalidConfigException("\"$SOURCE.$COLUMN_KEY\" is not set.")
        if (source.localeColumns?.size == 0)
            throw InvalidConfigException("\"$SOURCE.$COLUMN_LOCALES\" must contain at least one item.")
    }
}