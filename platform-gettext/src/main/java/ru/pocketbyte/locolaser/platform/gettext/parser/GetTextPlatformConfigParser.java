/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.gettext.parser;

import ru.pocketbyte.locolaser.platform.gettext.GetTextPlatformConfig;
import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

/**
 * @author Denis Shurygin
 */
public class GetTextPlatformConfigParser implements PlatformConfigParser<BasePlatformConfig> {
    public static final String RESOURCE_NAME = "res_name";
    public static final String RESOURCES_DIR = "res_dir";

    public BasePlatformConfig parse(Object platformObject, boolean throwIfWrongType) throws InvalidConfigException {

        if (platformObject instanceof String) {
            if (checkType((String) platformObject, throwIfWrongType))
                return new GetTextPlatformConfig();
        }
        else if (platformObject instanceof JSONObject) {
            JSONObject platformJSON = (JSONObject) platformObject;

            if (checkType(JsonParseUtils.getString(platformJSON, PLATFORM_TYPE, ConfigParser.PLATFORM, true), throwIfWrongType)) {
                GetTextPlatformConfig platform = new GetTextPlatformConfig();

                platform.setResourceName(JsonParseUtils.getString(
                        platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false));

                platform.setResourcesDir(JsonParseUtils.getFile(
                        platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false));

                return platform;
            }
        }

        if (throwIfWrongType)
            throw new InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.");

        return null;
    }

    private boolean checkType(String type, boolean throwIfWrongType) throws InvalidConfigException {
        if (!GetTextPlatformConfig.TYPE.equals(type)) {
            if (throwIfWrongType)
                throw new InvalidConfigException("Source type is \"" + type + "\", but expected \"" + GetTextPlatformConfig.TYPE + "\".");

            return false;
        }

        return true;
    }
}
