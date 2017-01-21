/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig;
import ru.pocketbyte.locolaser.config.source.SourceConfig;
import ru.pocketbyte.locolaser.config.source.SourceSetConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig;
import ru.pocketbyte.locolaser.testutils.mock.MockTableSourceConfig;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Denis Shurygin
 */
public class BaseTableSourceConfigParserTest {

    private ConfigParser mConfigParser;

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Before
    public void init() {
        BaseTableSourceConfigParser<MockTableSourceConfig>
                sourceConfigParser = new BaseTableSourceConfigParser<MockTableSourceConfig>() {

            @Override
            protected MockTableSourceConfig sourceByType(String type) throws InvalidConfigException {
                return new MockTableSourceConfig();
            }
        };
        PlatformConfigParser platformConfigParser = new PlatformConfigParser() {
            @Override
            public PlatformConfig parse(Object platformObject) throws InvalidConfigException {
                return new MockPlatformConfig();
            }
        };
        mConfigParser = new ConfigParser(sourceConfigParser, platformConfigParser);
    }

    @Test
    public void testMinimalSource() throws IOException, ParseException, InvalidConfigException {
        Map<String, Object> sourceMap = prepareMinimalSourceMap();
        File file = prepareMockFile(null, sourceMap);
        mConfigParser.fromFile(file);
    }

    @Test(expected=InvalidConfigException.class)
    public void testNoKeyColumn() throws IOException, ParseException, InvalidConfigException {
        Map<String, Object> sourceMap = prepareMinimalSourceMap();
        sourceMap.remove(BaseTableSourceConfigParser.COLUMN_KEY);
        File file = prepareMockFile(null, sourceMap);
        mConfigParser.fromFile(file);
    }

    @Test(expected=InvalidConfigException.class)
    public void testNoLocaleColumns() throws IOException, ParseException, InvalidConfigException {
        Map<String, Object> sourceMap = prepareMinimalSourceMap();
        sourceMap.remove(BaseTableSourceConfigParser.COLUMN_LOCALES);
        File file = prepareMockFile(null, sourceMap);
        mConfigParser.fromFile(file);
    }

    @Test(expected=InvalidConfigException.class)
    public void testEmptyLocaleColumns() throws IOException, ParseException, InvalidConfigException {
        Map<String, Object> sourceMap = prepareMinimalSourceMap();
        sourceMap.put(BaseTableSourceConfigParser.COLUMN_LOCALES, new ArrayList<>());
        File file = prepareMockFile(null, sourceMap);
        mConfigParser.fromFile(file);
    }

    @Test
    public void testConfigClass() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFile(null, prepareMinimalSourceMap());
        Config config = mConfigParser.fromFile(file);

        SourceConfig sourceConfig = config.getSourceConfig();
        assertTrue(sourceConfig instanceof BaseTableSourceConfig);
    }

    @Test
    public void testKeyColumn() throws IOException, ParseException, InvalidConfigException {
        String[] keyColumns = {"key_1", "key_2"};
        for (String keyColumn : keyColumns) {
            Map<String, Object> sourceMap = prepareMinimalSourceMap();
            sourceMap.put(BaseTableSourceConfigParser.COLUMN_KEY, keyColumn);
            File file = prepareMockFile(null, sourceMap);
            Config config = mConfigParser.fromFile(file);

            BaseTableSourceConfig sourceConfig = (BaseTableSourceConfig) config.getSourceConfig();
            assertEquals(keyColumn, sourceConfig.getKeyColumn());
        }
    }

    @Test
    public void testQuantityColumn() throws IOException, ParseException, InvalidConfigException {
        String[] quantityColumns = {"quantity_1", "quantity_2"};
        for (String quantityColumn : quantityColumns) {
            Map<String, Object> sourceMap = prepareMinimalSourceMap();
            sourceMap.put(BaseTableSourceConfigParser.COLUMN_QUANTITY, quantityColumn);
            File file = prepareMockFile(null, sourceMap);
            Config config = mConfigParser.fromFile(file);

            BaseTableSourceConfig sourceConfig = (BaseTableSourceConfig) config.getSourceConfig();
            assertEquals(quantityColumn, sourceConfig.getQuantityColumn());
        }
    }

    @Test
    public void testCommentColumn() throws IOException, ParseException, InvalidConfigException {
        String[] commentColumns = {"comment_1", "comment_2"};
        for (String commentColumn : commentColumns) {
            Map<String, Object> sourceMap = prepareMinimalSourceMap();
            sourceMap.put(BaseTableSourceConfigParser.COLUMN_COMMENT, commentColumn);
            File file = prepareMockFile(null, sourceMap);
            Config config = mConfigParser.fromFile(file);

            BaseTableSourceConfig sourceConfig = (BaseTableSourceConfig) config.getSourceConfig();
            assertEquals(commentColumn, sourceConfig.getCommentColumn());
        }
    }

    @Test
    public void testLocaleColumns() throws IOException, ParseException, InvalidConfigException {
        String[][] input = {
                {"locale_1", "locale_3"},
                {"locale_2"}
        };
        for (String[] localeColumns : input) {
            Map<String, Object> sourceMap = prepareMinimalSourceMap();
            sourceMap.put(BaseTableSourceConfigParser.COLUMN_LOCALES, Arrays.asList(localeColumns));
            File file = prepareMockFile(null, sourceMap);
            Config config = mConfigParser.fromFile(file);

            BaseTableSourceConfig sourceConfig = (BaseTableSourceConfig) config.getSourceConfig();
            assertEquals(localeColumns.length, sourceConfig.getLocaleColumns().size());

            for (int i = 0; i < localeColumns.length; i++) {
                assertTrue(sourceConfig.getLocaleColumns().contains(localeColumns[i]));
            }
        }
    }

    @Test
    public void testMultiSource() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMultiSourceMockFile(null, Arrays.asList(
                prepareMinimalSourceMap(),
                prepareMinimalSourceMap()));
        Config config = mConfigParser.fromFile(file);

        assertEquals(SourceSetConfig.class, config.getSourceConfig().getClass());
    }

    private Map<String, Object> prepareMinimalSourceMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(BaseTableSourceConfigParser.COLUMN_KEY, "key");
        map.put(BaseTableSourceConfigParser.COLUMN_LOCALES, Collections.singletonList("en"));
        return map;
    }

    private File prepareMockFile(Map<String, Object> configMap, Map<String, Object> sourceMap) throws IOException {
        File file = tempFolder.newFile();
        JSONObject json = configMap != null ? new JSONObject(configMap) : new JSONObject();
        json.put(ConfigParser.PLATFORM, "mock");

        if (sourceMap != null) {
            JSONObject sourceJson = new JSONObject(sourceMap);
            sourceJson.put(BaseTableSourceConfigParser.TYPE, "mock");
            json.put(ConfigParser.SOURCE, sourceJson);
        }
        else {
            json.put(ConfigParser.SOURCE, "mock");
        }

        PrintWriter writer = new PrintWriter(file);
        writer.write(json.toJSONString());
        writer.flush();
        writer.close();
        return file;
    }

    private File prepareMultiSourceMockFile(Map<String, Object> configMap, List<Map<String, Object>> sourceMaps) throws IOException {
        File file = tempFolder.newFile();
        JSONObject json = configMap != null ? new JSONObject(configMap) : new JSONObject();
        json.put(ConfigParser.PLATFORM, "mock");

        if (sourceMaps != null) {
            JSONArray sourceArray = new JSONArray();
            for (Map<String, Object> sourceMap: sourceMaps) {
                JSONObject sourceJson = new JSONObject(sourceMap);
                sourceJson.put(BaseTableSourceConfigParser.TYPE, "mock");
                sourceArray.add(sourceJson);
            }
            json.put(ConfigParser.SOURCE, sourceArray);
        }
        else {
            json.put(ConfigParser.SOURCE, "mock");
        }

        PrintWriter writer = new PrintWriter(file);
        writer.write(json.toJSONString());
        writer.flush();
        writer.close();
        return file;
    }
}
