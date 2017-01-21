/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils;

import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Utils class to parse JSON.
 *
 * @author Denis Shurygin
 */
public class JsonParseUtils {

    public static final JSONParser JSON_PARSER = new JSONParser();

    public static String getString(JSONObject json, String key, String parentKey, boolean throwIfNull) throws InvalidConfigException {
        Object object = getObject(json, key, parentKey, throwIfNull);
        if (object != null && !(object instanceof String))
            throw new InvalidConfigException("Property " + keyName(key, parentKey) + " must be a String.");
        return (String) object;
    }

    public static boolean getBoolean(JSONObject json, String key, String parentKey, boolean throwIfNull) throws InvalidConfigException {
        Object object = getObject(json, key, parentKey, throwIfNull);
        if (object != null && !(object instanceof Boolean))
            throw new InvalidConfigException("Property " + keyName(key, parentKey) + " must be a Boolean.");
        return object != null ? (Boolean) object : false;
    }

    public static long getLong(JSONObject json, String key, String parentKey, boolean throwIfNull) throws InvalidConfigException {
        Object object = getObject(json, key, parentKey, throwIfNull);
        if (object != null && !(object instanceof Long))
            throw new InvalidConfigException("Property " + keyName(key, parentKey) + " must be a Long.");
        return object != null ? (Long) object : 0;
    }

    public static JSONObject getJSONObject(JSONObject json, String key, String parentKey, boolean throwIfNull) throws InvalidConfigException {
        Object object = getObject(json, key, parentKey, throwIfNull);
        if (object != null && !(object instanceof JSONObject))
            throw new InvalidConfigException("Property " + keyName(key, parentKey) + " must be a JSONObject.");
        if (object != null)
            return (JSONObject) object;
        return null;
    }

    public static List<String> getStrings(JSONObject json, String key, String parentKey, boolean throwIfNull) throws InvalidConfigException {
        Object object = getObject(json, key, parentKey, throwIfNull);
        if (object != null) {
            if(object instanceof JSONArray) {
                for (Object item: (JSONArray) object)
                    if (!(item instanceof String))
                        throw new InvalidConfigException("Property " + keyName(key, parentKey) + " must be a Strings array.");
                return (List<String>) object;
            }
            else
                throw new InvalidConfigException("Property " + keyName(key, parentKey) + " must be a Strings array.");
        }
        return null;
    }

    public static File getFile(JSONObject json, String key, String parentKey, boolean throwIfNull) throws InvalidConfigException {
        String path = getString(json, key, parentKey, throwIfNull);
        if (path != null)
            try {
                return new File(new File(path).getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

    public static Object getObject(JSONObject json, String key, String parentKey, boolean throwIfNull) throws InvalidConfigException {
        Object object = json.get(key);
        if (throwIfNull && object == null)
            throw new InvalidConfigException("Property " + keyName(key, parentKey) + " is not set.");
        return object;
    }

    public static String keyName(String key, String parent) {
        StringBuilder builder = new StringBuilder("\"");
        if (parent != null)
            builder.append(parent).append(".");
        return builder.append(key).append("\"").toString();
    }
}
