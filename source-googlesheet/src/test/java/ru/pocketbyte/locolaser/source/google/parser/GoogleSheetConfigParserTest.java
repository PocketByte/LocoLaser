/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.source.google.parser;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.config.parser.BaseTableSourceConfigParser;
import ru.pocketbyte.locolaser.config.parser.ConfigParser;
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.config.source.SourceConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.source.google.sheet.GoogleSheetConfig;
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Denis Shurygin
 */
public class GoogleSheetConfigParserTest {

    private ConfigParser mConfigParser;

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Before
    public void init() {
        GoogleSheetConfigParser sourceConfigParser = new GoogleSheetConfigParser();

        PlatformConfigParser platformConfigParser = new PlatformConfigParser() {
            @Override
            public PlatformConfig parse(Object platformObject, boolean throwIfWrongType) throws InvalidConfigException {
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

    @Test(expected = InvalidConfigException.class)
    public void testWrongType() throws IOException, ParseException, InvalidConfigException {
        Map<String, Object> sourceMap = prepareMinimalSourceMap();
        sourceMap.put(BaseTableSourceConfigParser.TYPE, "wrong");
        File file = prepareMockFile(null, sourceMap);
        mConfigParser.fromFile(file);
    }

    @Test(expected=InvalidConfigException.class)
    public void testNoSheetId() throws IOException, ParseException, InvalidConfigException {
        Map<String, Object> sourceMap = prepareMinimalSourceMap();
        sourceMap.remove(GoogleSheetConfigParser.SHEET_ID);
        File file = prepareMockFile(null, sourceMap);
        mConfigParser.fromFile(file);
    }

    @Test
    public void testConfigClass() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFile(null, prepareMinimalSourceMap());
        Config[] config = mConfigParser.fromFile(file);

        assertEquals(1, config.length);

        SourceConfig sourceConfig = config[0].getSourceConfig();
        assertTrue(sourceConfig instanceof GoogleSheetConfig);
    }

    @Test
    public void testSheetId() throws IOException, ParseException, InvalidConfigException {
        String[] values = {"id_1", "id_2"};
        for (String expectedValue : values) {
            Map<String, Object> sourceMap = prepareMinimalSourceMap();
            sourceMap.put(GoogleSheetConfigParser.SHEET_ID, expectedValue);
            File file = prepareMockFile(null, sourceMap);
            Config[] config = mConfigParser.fromFile(file);

            assertEquals(1, config.length);

            GoogleSheetConfig sourceConfig = (GoogleSheetConfig) config[0].getSourceConfig();
            assertEquals(expectedValue, sourceConfig.getId());
        }
    }

    @Test
    public void testWorkSheet() throws IOException, ParseException, InvalidConfigException {
        String[] values = {"worksheet_1", "worksheet_2"};
        for (String expectedValue : values) {
            Map<String, Object> sourceMap = prepareMinimalSourceMap();
            sourceMap.put(GoogleSheetConfigParser.SHEET_WORKSHEET_TITLE, expectedValue);
            File file = prepareMockFile(null, sourceMap);
            Config[] config = mConfigParser.fromFile(file);

            assertEquals(1, config.length);

            GoogleSheetConfig sourceConfig = (GoogleSheetConfig) config[0].getSourceConfig();
            assertEquals(expectedValue, sourceConfig.getWorksheetTitle());
        }
    }

    @Test
    public void testCredentialFile() throws IOException, ParseException, InvalidConfigException {
        String[] values = {"file_1.txt", "file_2.json"};
        for (String expectedValue : values) {
            Map<String, Object> sourceMap = prepareMinimalSourceMap();
            sourceMap.put(GoogleSheetConfigParser.SHEET_CREDENTIAL_FILE, expectedValue);
            File file = prepareMockFile(null, sourceMap);
            Config[] config = mConfigParser.fromFile(file);

            assertEquals(1, config.length);

            GoogleSheetConfig sourceConfig = (GoogleSheetConfig) config[0].getSourceConfig();
            assertEquals(new File(expectedValue).getCanonicalPath(),
                    sourceConfig.getCredentialFile().getCanonicalPath());
        }
    }

    private Map<String, Object> prepareMinimalSourceMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(BaseTableSourceConfigParser.TYPE, "googlesheet");
        map.put(BaseTableSourceConfigParser.COLUMN_KEY, "key");
        map.put(BaseTableSourceConfigParser.COLUMN_LOCALES, Collections.singletonList("en"));
        map.put(GoogleSheetConfigParser.SHEET_ID, "sheet_id");
        return map;
    }

    private File prepareMockFile(Map<String, Object> configMap, Map<String, Object> sourceMap) throws IOException {
        File file = tempFolder.newFile();
        JSONObject json = configMap != null ? new JSONObject(configMap) : new JSONObject();
        json.put(ConfigParser.PLATFORM, "mock");

        if (sourceMap != null)
            json.put(ConfigParser.SOURCE, new JSONObject(sourceMap));
        else
            json.put(ConfigParser.SOURCE, "mock");

        PrintWriter writer = new PrintWriter(file);
        writer.write(json.toJSONString());
        writer.flush();
        writer.close();
        return file;
    }

}
