package ru.pocketbyte.locolaser.platform.mobile.parser;

import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

public abstract class BaseMobilePlatformConfigParser implements PlatformConfigParser<BasePlatformConfig> {

    public static final String RESOURCE_NAME = "res_name";
    public static final String RESOURCES_DIR = "res_dir";

    /**
     * Creates new platform object from it's type.
     * @param type Platform type.
     * @param throwIfWrongType Define that parser should trow exception if object type is not supported.
     * @return Platform object depends on type or null if type is not supported and throwIfWrongType equal false.
     * @throws InvalidConfigException if platform is unknown.
     */
    abstract protected BasePlatformConfig platformByType(String type, boolean throwIfWrongType) throws InvalidConfigException;

    public BasePlatformConfig parse(Object platformObject, boolean throwIfWrongType) throws InvalidConfigException {

        if (platformObject instanceof String) {
            return parseString((String) platformObject, throwIfWrongType);
        }
        else if (platformObject instanceof JSONObject) {
            return parseJSONObject((JSONObject) platformObject, throwIfWrongType);
        }

        if (throwIfWrongType)
            throw new InvalidConfigException("Property \"" + ConfigParser.PLATFORM + "\" must be a String or JSON object.");

        return null;
    }

    protected BasePlatformConfig parseString(String type, boolean throwIfWrongType) throws InvalidConfigException {
        return platformByType(type, throwIfWrongType);
    }

    protected BasePlatformConfig parseJSONObject(JSONObject platformJSON, boolean throwIfWrongType) throws InvalidConfigException {
        String type = JsonParseUtils.getString(platformJSON, PLATFORM_TYPE, ConfigParser.PLATFORM, true);
        BasePlatformConfig platform = platformByType(type, throwIfWrongType);

        if (platform == null)
            return null;

        platform.setResourceName(JsonParseUtils.getString(
                platformJSON, RESOURCE_NAME, ConfigParser.PLATFORM, false));

        platform.setResourcesDir(JsonParseUtils.getFile(
                platformJSON, RESOURCES_DIR, ConfigParser.PLATFORM, false));

        return platform;
    }

}
