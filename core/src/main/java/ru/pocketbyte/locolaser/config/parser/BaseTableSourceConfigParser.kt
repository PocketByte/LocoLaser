/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser

import org.json.simple.JSONObject
import ru.pocketbyte.locolaser.config.parser.ConfigParser.Companion.SOURCE
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfigBuilder
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

/**
 * @author Denis Shurygin
 */
@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
abstract class BaseTableSourceConfigParser<ConfigBuilder : BaseTableResourcesConfigBuilder<*>>
    : ResourcesConfigParser<ResourcesConfig> {

    companion object {
        const val TYPE = "type"
        const val COLUMN_KEY = "column_key"
        const val COLUMN_QUANTITY = "column_quantity"
        const val COLUMN_COMMENT = "column_comment"
        const val COLUMN_METADATA = "column_metadata"
    }

    /**
     * Creates a new config builder object by it's name.
     *
     * @param type Source type.
     * @return builder object.
     * @throws InvalidConfigException if type is unknown.
     */
    @Throws(InvalidConfigException::class)
    protected abstract fun builderByType(type: String?, throwIfWrongType: Boolean): ConfigBuilder?

    /**
     * Parse Source from JSON object.
     *
     * @param resourceObject JSON object that contain source config properties.
     * @return Parsed source object.
     * @throws InvalidConfigException
     */
    @Throws(InvalidConfigException::class)
    override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): ResourcesConfig? {
        if (resourceObject is JSONObject) {
            return parseFromJson(resourceObject, throwIfWrongType)
        } else if (throwIfWrongType) {
            throw InvalidConfigException("Source must be a JSONObject or JSONArray.")
        }
        return null
    }

    @Throws(InvalidConfigException::class)
    protected fun parseFromJson(configJson: JSONObject, throwIfWrongType: Boolean): ResourcesConfig? {
        val type = JsonParseUtils.getString(configJson, TYPE, SOURCE, false)
        val builder = builderByType(type, throwIfWrongType) ?: return null
        fillFromJSON(builder, configJson)
        validate(builder)
        return builder.build()
    }

    /**
     * Fill source object from JSON.
     *
     * @param builder      Source to fill.
     * @param configJson JSON object that contain source config properties.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     */
    @Throws(InvalidConfigException::class)
    protected open fun fillFromJSON(builder: ConfigBuilder, configJson: JSONObject) {
        builder.keyColumn = JsonParseUtils.getString(configJson, COLUMN_KEY, SOURCE, true)
            ?: throw InvalidConfigException("\"$SOURCE.$COLUMN_KEY\" is not set.")
        builder.quantityColumn = JsonParseUtils.getString(configJson, COLUMN_QUANTITY, SOURCE, false)
        builder.commentColumn = JsonParseUtils.getString(configJson, COLUMN_COMMENT, SOURCE, false)
        builder.metadataColumn = JsonParseUtils.getString(configJson, COLUMN_METADATA, SOURCE, false)
    }

    @Throws(InvalidConfigException::class)
    protected open fun validate(builder: ConfigBuilder) {
        if (builder.keyColumn.isEmpty())
            throw InvalidConfigException("\"$SOURCE.$COLUMN_KEY\" is not set.")
    }
}