/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.parser;

import ru.pocketbyte.locolaser.platform.mobile.GetTextPlatformConfig;
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
    public static final String TEMP_DIR = "temp_dir";

    public BasePlatformConfig parse(Object platformObject) throws InvalidConfigException {

        if (platformObject instanceof String) {
            checkType((String) platformObject);
            return new GetTextPlatformConfig();
        }
        else if (platformObject instanceof JSONObject) {
            JSONObject platformJSON = (JSONObject) platformObject;

            checkType(JsonParseUtils.getString(platformJSON, PLATFORM_TYPE, ConfigParser.PLATFORM, true));

            GetTextPlatformConfig platform = new GetTextPlatformConfig();

            platform.setResourceName(JsonParseUtils.getString(
                                        platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false));

            platform.setResourcesDir(JsonParseUtils.getFile(
                                        platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false));

            platform.setTempDir(JsonParseUtils.getFile(
                                        platformJSON, TEMP_DIR, ConfigParser.PLATFORM, false));

            return platform;
        }
        else
            throw new InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.");
    }

    private void checkType(String type) throws InvalidConfigException {
        if (!GetTextPlatformConfig.TYPE.equals(type))
            throw new InvalidConfigException("Source type is \"" + type + "\", but expected \"" + GetTextPlatformConfig.TYPE + "\".");

    }
}
