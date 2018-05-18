package ru.pocketbyte.locolaser.platform.kotlinmobile.parser;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinCommonPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.parser.BaseMobilePlatformConfigParser;

public class KotlinCommonPlatformConfigParser extends BaseMobilePlatformConfigParser {

    @Override
    protected BasePlatformConfig platformByType(String type, boolean throwIfWrongType) throws InvalidConfigException {
        if (KotlinCommonPlatformConfig.TYPE.equals(type))
            return new KotlinCommonPlatformConfig();

        if (throwIfWrongType)
            throw new InvalidConfigException("Unknown platform: " + type);

        return null;
    }
}
