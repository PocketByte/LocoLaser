package ru.pocketbyte.locolaser.platform.mobile.parser;

import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.mobile.IosBaseClassPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.IosObjectiveCPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.IosSwiftPlatformConfig;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

public class IosClassPlatformConfigParser extends BaseMobilePlatformConfigParser {

    public static final String TABLE_NAME = "table_name";

    @Override
    protected BasePlatformConfig parseJSONObject(JSONObject platformJSON, boolean throwIfWrongType) throws InvalidConfigException {
        IosBaseClassPlatformConfig config = (IosBaseClassPlatformConfig) super.parseJSONObject(platformJSON, throwIfWrongType);

        if (config != null) {

            config.setTableName(JsonParseUtils.getString(
                    platformJSON, TABLE_NAME, ConfigParser.PLATFORM, false));
        }

        return config;
    }

    @Override
    protected BasePlatformConfig platformByType(String type, boolean throwIfWrongType) throws InvalidConfigException {
        if (IosSwiftPlatformConfig.TYPE.equals(type))
            return new IosSwiftPlatformConfig();
        if (IosObjectiveCPlatformConfig.TYPE.equals(type))
            return new IosObjectiveCPlatformConfig();

        if (throwIfWrongType)
            throw new InvalidConfigException("Unknown platform: " + type);

        return null;
    }
}
