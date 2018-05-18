/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig;
import ru.pocketbyte.locolaser.config.source.SourceConfig;
import ru.pocketbyte.locolaser.config.source.SourceSetConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;
import ru.pocketbyte.locolaser.utils.TextUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public abstract class BaseTableSourceConfigParser<SourceConfigClass extends BaseTableSourceConfig> implements SourceConfigParser<SourceConfig> {

    public static final String TYPE = "type";
    public static final String COLUMN_KEY = "column_key";
    public static final String COLUMN_QUANTITY = "column_quantity";
    public static final String COLUMN_LOCALES = "column_locales";
    public static final String COLUMN_COMMENT = "column_comment";

    /**
     * Creates a new Source config object by it's name.
     *
     * @param type Source type.
     * @return Source object.
     * @throws InvalidConfigException if source type is unknown.
     */
    protected abstract SourceConfigClass sourceByType(String type) throws InvalidConfigException;

    /**
     * Parse Source from JSON object.
     *
     * @param sourceObject JSON object that contain source config properties.
     * @return Parsed source object.
     * @throws InvalidConfigException
     */
    public SourceConfig parse(Object sourceObject, boolean throwIfWrongType) throws InvalidConfigException {
        if (sourceObject instanceof JSONObject) {
            return parseFromJson((JSONObject) sourceObject);
        } else if (sourceObject instanceof JSONArray) {
            SourceConfig defaultConfig = null;
            Set<SourceConfig> configs = new HashSet<>(((JSONArray) sourceObject).size());
            for (Object object : (JSONArray) sourceObject) {
                if (object instanceof JSONObject) {
                    SourceConfig config = parseFromJson((JSONObject) object);
                    configs.add(config);

                    if (defaultConfig == null)
                        defaultConfig = config;
                }
                else
                    throw new InvalidConfigException("Source array must contain JSONObjects.");
            }
            return new SourceSetConfig(configs, defaultConfig);
        } else if (throwIfWrongType){
            throw new InvalidConfigException("Source must be a JSONObject or JSONArray.");
        }
        return null;
    }

    protected SourceConfigClass parseFromJson(JSONObject configJson) throws InvalidConfigException {
        String type = JsonParseUtils.getString(configJson, TYPE, ConfigParser.SOURCE, false);
        SourceConfigClass source = sourceByType(type);
        fillFromJSON(source, configJson);
        validate(source);
        return source;
    }

    /**
     * Fill source object from JSON.
     *
     * @param source      Source to fill.
     * @param configJson JSON object that contain source config properties.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     */
    protected void fillFromJSON(SourceConfigClass source, JSONObject configJson) throws InvalidConfigException {
        source.setKeyColumn(JsonParseUtils.getString(configJson, COLUMN_KEY, ConfigParser.SOURCE, true));
        source.setQuantityColumn(JsonParseUtils.getString(configJson, COLUMN_QUANTITY, ConfigParser.SOURCE, false));
        source.setCommentColumn(JsonParseUtils.getString(configJson, COLUMN_COMMENT, ConfigParser.SOURCE, false));

        List<String> locales = JsonParseUtils.getStrings(configJson, COLUMN_LOCALES, ConfigParser.SOURCE, true);
        if (locales != null)
            source.setLocaleColumns(new HashSet<>(locales));
    }

    protected void validate(SourceConfigClass source) throws InvalidConfigException {
        if (TextUtils.isEmpty(source.getKeyColumn()))
            throw new InvalidConfigException("\"" + ConfigParser.SOURCE + "." + COLUMN_KEY + "\" is not set.");
        if (source.getLocaleColumns() == null)
            throw new InvalidConfigException("\"" + ConfigParser.SOURCE + "." + COLUMN_KEY + "\" is not set.");
        if (source.getLocaleColumns().size() == 0)
            throw new InvalidConfigException("\"" + ConfigParser.SOURCE + "." + COLUMN_LOCALES + "\" must contain at least one item.");
    }
}