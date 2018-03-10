package ru.pocketbyte.locolaser.platform.kotlinmobile.parser;

import org.json.simple.JSONObject;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinAndroidPlatformConfig;
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinBaseImplementationPlatformConfig;
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinIosPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.parser.BaseMobilePlatformConfigParser;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

public class KotlinImplementationPlatformConfigParser extends BaseMobilePlatformConfigParser {

    public static final String INTERFACE = "implements";
    public static final String APP_PACKAGE = "app_id";


    @Override
    protected BasePlatformConfig parseJSONObject(JSONObject platformJSON, boolean throwIfWrongType) throws InvalidConfigException {
        KotlinBaseImplementationPlatformConfig config = (KotlinBaseImplementationPlatformConfig)
                super.parseJSONObject(platformJSON, throwIfWrongType);

        if (config != null) {

            config.setInterfaceName(JsonParseUtils.getString(
                    platformJSON, INTERFACE, ConfigParser.PLATFORM, false));

            if (KotlinAndroidPlatformConfig.TYPE.equals(config.getType())) {
                ((KotlinAndroidPlatformConfig) config).setAppPackage(
                        JsonParseUtils.getString(platformJSON, APP_PACKAGE, ConfigParser.PLATFORM, true)
                );
            }
        }

        return config;
    }

    @Override
    protected BasePlatformConfig platformByType(String type, boolean throwIfWrongType) throws InvalidConfigException {
        if (KotlinAndroidPlatformConfig.TYPE.equals(type))
            return new KotlinAndroidPlatformConfig();
        if (KotlinIosPlatformConfig.TYPE.equals(type))
            return new KotlinIosPlatformConfig();

        if (throwIfWrongType)
            throw new InvalidConfigException("Unknown platform: " + type);

        return null;
    }
}
