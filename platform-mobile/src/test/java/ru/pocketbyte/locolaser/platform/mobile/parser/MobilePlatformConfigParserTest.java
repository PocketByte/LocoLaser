/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.parser;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.platform.mobile.AndroidPlatformConfig;
import ru.pocketbyte.locolaser.platform.mobile.IosPlatformConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Denis Shurygin
 */
public class MobilePlatformConfigParserTest {

    private MobilePlatformConfigParser parser;

    @Before
    public void init() {
        parser = new MobilePlatformConfigParser();
    }

    @Test
    public void testAndroidFromString() throws InvalidConfigException {
        PlatformConfig config = parser.parse(AndroidPlatformConfig.TYPE);

        assertNotNull(config);
        assertEquals(AndroidPlatformConfig.class, config.getClass());
    }

    @Test
    public void testIosFromString() throws InvalidConfigException {
        PlatformConfig config = parser.parse(IosPlatformConfig.TYPE);

        assertNotNull(config);
        assertEquals(IosPlatformConfig.class, config.getClass());
    }

    @Test(expected = InvalidConfigException.class)
    public void testUnknownFromString() throws InvalidConfigException {
        parser.parse("invalid_platform");
    }

    @Test
    public void testFromJson() throws InvalidConfigException, IOException {
        BasePlatformConfig config = parser.parse(prepareTestPlatformJson(AndroidPlatformConfig.TYPE));

        assertNotNull(config);
        assertEquals(AndroidPlatformConfig.class, config.getClass());

        assertEquals("test_res", config.getResourceName());
        assertEquals(new File("test_res_dir").getCanonicalPath(),
                config.getResourcesDir().getCanonicalPath());
        assertEquals(new File("test_temp_dir").getCanonicalPath(),
                config.getTempDir().getCanonicalPath());
    }

    @Test(expected = InvalidConfigException.class)
    public void testFromJsonNoType() throws InvalidConfigException {
        JSONObject json = prepareTestPlatformJson(AndroidPlatformConfig.TYPE);
        json.remove(PlatformConfigParser.PLATFORM_TYPE);
        parser.parse(json);
    }

    @Test
    public void testFromJsonOnlyType() throws InvalidConfigException {
        JSONObject json = new JSONObject();
        json.put(PlatformConfigParser.PLATFORM_TYPE, AndroidPlatformConfig.TYPE);
        BasePlatformConfig config = parser.parse(json);

        assertNotNull(config);
        assertEquals(AndroidPlatformConfig.class, config.getClass());
    }

    @Test(expected = InvalidConfigException.class)
    public void testFromInvalidClass() throws InvalidConfigException {
        parser.parse(new ArrayList<String>());
    }

    private JSONObject prepareTestPlatformJson(String platform) {
        JSONObject json = new JSONObject();
        json.put(PlatformConfigParser.PLATFORM_TYPE, platform);
        json.put(MobilePlatformConfigParser.RESOURCE_NAME, "test_res");
        json.put(MobilePlatformConfigParser.RESOURCES_DIR, "test_res_dir");
        json.put(MobilePlatformConfigParser.TEMP_DIR, "test_temp_dir");
        return json;
    }
}
