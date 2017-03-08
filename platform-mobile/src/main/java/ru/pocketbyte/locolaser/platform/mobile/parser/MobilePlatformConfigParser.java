/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.parser;

import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.mobile.AndroidPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.IosPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.WpPlatformConfig;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

/**
 * @author Denis Shurygin
 */
public class MobilePlatformConfigParser implements PlatformConfigParser<BasePlatformConfig> {
    public static final String RESOURCE_NAME = "res_name";
    public static final String RESOURCES_DIR = "res_dir";
    public static final String TEMP_DIR = "temp_dir";

    // Extra properties for iOS
    public static final String SOURCE_DIR = "source_dir";
    public static final String SWIFT_CLASS = "swift_class";
    public static final String OBJC_CLASS = "objc_class";

    public BasePlatformConfig parse(Object platformObject) throws InvalidConfigException {
        BasePlatformConfig platform;

        if (platformObject instanceof String) {
            String type = (String) platformObject;
            platform = platformByType(type);
        }
        else if (platformObject instanceof JSONObject) {
            JSONObject platformJSON = (JSONObject) platformObject;
            String type = JsonParseUtils.getString(platformJSON, PLATFORM_TYPE, ConfigParser.PLATFORM, true);
            platform = platformByType(type);

            platform.setResourceName(JsonParseUtils.getString(
                                        platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false));

            platform.setResourcesDir(JsonParseUtils.getFile(
                                        platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false));

            platform.setTempDir(JsonParseUtils.getFile(
                                        platformJSON, TEMP_DIR, ConfigParser.PLATFORM, false));

            // Extra properties for iOS
            if (IosPlatformConfig.TYPE.equals(platform.getType())) {
                IosPlatformConfig iosPlatform = (IosPlatformConfig) platform;
                iosPlatform.setSourceDir(JsonParseUtils.getFile(
                                platformJSON, SOURCE_DIR, ConfigParser.PLATFORM, false));
                iosPlatform.setSwiftClassName(JsonParseUtils.getString(
                                platformJSON, SWIFT_CLASS, ConfigParser.PLATFORM, false));
                iosPlatform.setObjcClassName(JsonParseUtils.getString(
                                platformJSON, OBJC_CLASS, ConfigParser.PLATFORM, false));
            }
        }
        else
            throw new InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.");

        return platform;
    }

    /**
     * Creates new platform object from it's type.
     * @param type Platform type.
     * @return Platform object depends on type.
     * @throws InvalidConfigException if platform is unknown.
     */
    protected BasePlatformConfig platformByType(String type) throws InvalidConfigException {
        if (AndroidPlatformConfig.TYPE.equals(type))
            return new AndroidPlatformConfig();
        if (IosPlatformConfig.TYPE.equals(type))
            return new IosPlatformConfig();
        if (WpPlatformConfig.TYPE.equals(type))
            return new WpPlatformConfig();
        throw new InvalidConfigException("Unknown platform: " + type);
    }
}
