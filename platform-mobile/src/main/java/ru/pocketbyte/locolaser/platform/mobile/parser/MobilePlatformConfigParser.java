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

    // Extra properties for iOS
    public static final String SOURCE_DIR = "source_dir";
    public static final String SWIFT_CLASS = "swift_class";
    public static final String OBJC_CLASS = "objc_class";

    public BasePlatformConfig parse(Object platformObject, boolean throwIfWrongType) throws InvalidConfigException {

        if (platformObject instanceof String) {
            String type = (String) platformObject;
            return platformByType(type, throwIfWrongType);
        }
        else if (platformObject instanceof JSONObject) {
            JSONObject platformJSON = (JSONObject) platformObject;
            String type = JsonParseUtils.getString(platformJSON, PLATFORM_TYPE, ConfigParser.PLATFORM, true);
            BasePlatformConfig platform = platformByType(type, throwIfWrongType);

            if (platform == null)
                return null;

            platform.setResourceName(JsonParseUtils.getString(
                                        platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false));

            platform.setResourcesDir(JsonParseUtils.getFile(
                                        platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false));

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

            return platform;
        }

        if (throwIfWrongType)
            throw new InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.");

        return null;
    }

    /**
     * Creates new platform object from it's type.
     * @param type Platform type.
     * @param throwIfWrongType Define that parser should trow exception if object type is not supported.
     * @return Platform object depends on type or null if type is not supported and throwIfWrongType equal false.
     * @throws InvalidConfigException if platform is unknown.
     */
    protected BasePlatformConfig platformByType(String type, boolean throwIfWrongType) throws InvalidConfigException {
        if (AndroidPlatformConfig.TYPE.equals(type))
            return new AndroidPlatformConfig();
        if (IosPlatformConfig.TYPE.equals(type))
            return new IosPlatformConfig();
        if (WpPlatformConfig.TYPE.equals(type))
            return new WpPlatformConfig();

        if (throwIfWrongType)
            throw new InvalidConfigException("Unknown platform: " + type);

        return null;
    }
}
