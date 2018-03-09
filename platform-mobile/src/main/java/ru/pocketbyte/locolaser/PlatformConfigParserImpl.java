/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser;

import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.parser.PlatformSetConfigParser;
import ru.pocketbyte.locolaser.platform.mobile.parser.IosClassPlatformConfigParser;
import ru.pocketbyte.locolaser.platform.mobile.parser.MobilePlatformConfigParser;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Denis Shurygin
 */
public class PlatformConfigParserImpl extends PlatformSetConfigParser {

    public PlatformConfigParserImpl() {
        super(parsers());
    }

    private static Set<PlatformConfigParser> parsers() {
        Set<PlatformConfigParser> parsers = new LinkedHashSet<>();
        parsers.add(new MobilePlatformConfigParser());
        parsers.add(new IosClassPlatformConfigParser());
        return parsers;
    }
}
