package ru.pocketbyte.locolaser.platform.kotlinmobile.parser;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinInterfacePlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.parser.BaseMobilePlatformConfigParser;

public class KotlinInterfacePlatformConfigParser extends BaseMobilePlatformConfigParser {

    @Override
    protected BasePlatformConfig platformByType(String type, boolean throwIfWrongType) throws InvalidConfigException {
        if (KotlinInterfacePlatformConfig.TYPE.equals(type))
            return new KotlinInterfacePlatformConfig();

        if (throwIfWrongType)
            throw new InvalidConfigException("Unknown platform: " + type);

        return null;
    }
}
