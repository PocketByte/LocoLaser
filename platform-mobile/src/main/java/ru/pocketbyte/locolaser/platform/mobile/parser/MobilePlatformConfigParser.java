/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.parser;

import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.mobile.AndroidPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.IosPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.WpPlatformConfig;

/**
 * @author Denis Shurygin
 */
public class MobilePlatformConfigParser extends BaseMobilePlatformConfigParser {

    @Override
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
