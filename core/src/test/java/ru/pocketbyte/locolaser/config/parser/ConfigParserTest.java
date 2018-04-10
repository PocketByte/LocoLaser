/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser;

import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig;
import ru.pocketbyte.locolaser.testutils.mock.MockTableSourceConfig;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * @author Denis Shurygin
 */
public class ConfigParserTest {

    private ConfigParser mConfigParser;

    @Rule
    public TemporaryFolder tempFolder= new TemporaryFolder();

    @Before
    public void init() {
        SourceConfigParser<MockTableSourceConfig> sourceConfigParser = new SourceConfigParser<MockTableSourceConfig>() {

            @Override
            public MockTableSourceConfig parse(Object sourceObject, boolean throwIfWrongType) throws InvalidConfigException {
                return new MockTableSourceConfig();
            }
        };
        PlatformConfigParser platformConfigParser = new PlatformConfigParser() {
            @Override
            public PlatformConfig parse(Object platformObject, boolean throwIfWrongType) throws InvalidConfigException {
                return new MockPlatformConfig();
            }
        };
        mConfigParser = new ConfigParser(sourceConfigParser, platformConfigParser);
    }

    @Test(expected=InvalidConfigException.class)
    public void testNoSource() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFileNoSource();
        mConfigParser.fromFile(file);
    }

    @Test(expected=InvalidConfigException.class)
    public void testNoPlatform() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFileNoPlatform();
        mConfigParser.fromFile(file);
    }

    @Test
    public void testConfigsArray() throws IOException, ParseException, InvalidConfigException {
        long delay1 = 141;
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put(ConfigParser.DELAY, delay1);

        long delay2 = 873;
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put(ConfigParser.DELAY, delay2);

        File file = prepareMockFileWithArray(map1, map2);

        Config[] configs = mConfigParser.fromFile(file);

        assertEquals(2, configs.length);

        assertNotNull(configs[0]);
        assertNotNull(configs[1]);

        assertEquals(delay1 * ConfigParser.DELAY_MULT, configs[0].getDelay());
        assertEquals(delay2 * ConfigParser.DELAY_MULT, configs[1].getDelay());

    }

    @Test
    public void testDefaultValues() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFile(null);
        Config[] configs = mConfigParser.fromFile(file);

        assertEquals(1, configs.length);

        Config config = configs[0];

        assertNotNull(config);
        assertEquals(config.getFile().getCanonicalPath(), file.getCanonicalPath());
        assertFalse(config.isForceImport());
        assertFalse(config.isDuplicateComments());
        assertEquals(file.getParentFile().getCanonicalPath(), System.getProperty("user.dir"));
        assertEquals(Config.ConflictStrategy.KEEP_NEW_PLATFORM, config.getConflictStrategy());
        assertEquals(0, config.getDelay());
    }

    @Test
    public void testJsonForceImport() throws IOException, ParseException, InvalidConfigException {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.FORCE_IMPORT, true);

        File file = prepareMockFile(map);
        Config[] configs = mConfigParser.fromFile(file);

        assertEquals(1, configs.length);
        assertTrue(configs[0].isForceImport());

        map.put(ConfigParser.FORCE_IMPORT, false);

        file = prepareMockFile(map);
        configs = mConfigParser.fromFile(file);

        assertEquals(1, configs.length);
        assertFalse(configs[0].isForceImport());
    }

    @Test
    public void testJsonDuplicateComments() throws IOException, ParseException, InvalidConfigException {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.DUPLICATE_COMMENTS, true);

        File file = prepareMockFile(map);
        Config[] configs = mConfigParser.fromFile(file);

        assertEquals(1, configs.length);
        assertTrue(configs[0].isDuplicateComments());

        map.put(ConfigParser.DUPLICATE_COMMENTS, false);

        file = prepareMockFile(map);
        configs = mConfigParser.fromFile(file);

        assertEquals(1, configs.length);
        assertFalse(configs[0].isDuplicateComments());
    }

    @Test
    public void testJsonWorkDir() throws IOException, ParseException, InvalidConfigException {
        String newWorkDir = "./new/work/dir";
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.WORK_DIR, newWorkDir);

        File file = prepareMockFile(map);
        Config[] configs = mConfigParser.fromFile(file);

        assertEquals(new File(file.getParentFile(), newWorkDir).getCanonicalPath(), System.getProperty("user.dir"));
    }

    @Test
    public void testJsonTempDir() throws IOException, ParseException, InvalidConfigException {
        String tempDir = "./new/work/dir";
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.TEMP_DIR, tempDir);

        File file = prepareMockFile(map);
        Config[] configs = mConfigParser.fromFile(file);

        assertEquals(1, configs.length);
        assertEquals(new File(tempDir).getCanonicalPath(), configs[0].getTempDir().getCanonicalPath());
    }

    @Test
    public void testDefaultTempDir() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFile(new HashMap<String, Object>());
        Config[] configs = mConfigParser.fromFile(file);

        File expected = new MockPlatformConfig().getDefaultTempDir();

        assertEquals(1, configs.length);
        assertEquals(expected.getCanonicalPath(), configs[0].getTempDir().getCanonicalPath());
    }

    @Test
    public void testJsonConflictStrategy() throws IOException, ParseException, InvalidConfigException {
        List<Pair<String, Config.ConflictStrategy>> list = Arrays.asList(
                new Pair<>(Config.ConflictStrategy.REMOVE_PLATFORM.name, Config.ConflictStrategy.REMOVE_PLATFORM),
                new Pair<>(Config.ConflictStrategy.KEEP_NEW_PLATFORM.name, Config.ConflictStrategy.KEEP_NEW_PLATFORM),
                new Pair<>(Config.ConflictStrategy.EXPORT_NEW_PLATFORM.name, Config.ConflictStrategy.EXPORT_NEW_PLATFORM)
        );

        for (Pair<String, Config.ConflictStrategy> pair: list) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ConfigParser.CONFLICT_STRATEGY, pair.getKey());

            File file = prepareMockFile(map);
            Config[] configs = mConfigParser.fromFile(file);

            assertEquals(1, configs.length);
            assertEquals(pair.getValue(), configs[0].getConflictStrategy());
        }
    }

    @Test
    public void testJsonDelay() throws IOException, ParseException, InvalidConfigException {
        long delay = 1;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.DELAY, delay);

        File file = prepareMockFile(map);
        Config[] configs = mConfigParser.fromFile(file);

        assertEquals(1, configs.length);
        assertEquals(delay * ConfigParser.DELAY_MULT, configs[0].getDelay());
    }

    @Test
    public void testFromArguments() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFile(null);
        Config[] configs = mConfigParser.fromArguments(new String[]{file.getAbsolutePath()});

        assertEquals(1, configs.length);
        assertNotNull(configs[0]);
    }

    @Test(expected=InvalidConfigException.class)
    public void testFromArgumentsNoArguments() throws IOException, ParseException, InvalidConfigException {
        mConfigParser.fromArguments(new String[]{});
    }

    @Test
    public void testArgumentForce() throws IOException, ParseException, InvalidConfigException {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.FORCE_IMPORT, false);

        File file = prepareMockFile(map);

        Config[] configs = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(), "--force"});
        assertEquals(1, configs.length);
        assertTrue(configs[0].isForceImport());

        configs = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(), "--f"});
        assertEquals(1, configs.length);
        assertTrue(configs[0].isForceImport());
    }

    @Test
    public void testArgumentConflictStrategy() throws IOException, ParseException, InvalidConfigException {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.CONFLICT_STRATEGY, Config.ConflictStrategy.KEEP_NEW_PLATFORM.name);

        File file = prepareMockFile(map);

        Config[] configs = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(),
                "-cs", Config.ConflictStrategy.EXPORT_NEW_PLATFORM.name});
        assertEquals(1, configs.length);
        assertEquals(Config.ConflictStrategy.EXPORT_NEW_PLATFORM, configs[0].getConflictStrategy());

        configs = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(),
                "-cs ", Config.ConflictStrategy.REMOVE_PLATFORM.name});
        assertEquals(1, configs.length);
        assertEquals(Config.ConflictStrategy.REMOVE_PLATFORM, configs[0].getConflictStrategy());
    }

    @Test
    public void testArgumentDelay() throws IOException, ParseException, InvalidConfigException {
        long delay = 1;

        File file = prepareMockFile(null);

        Config[] configs = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(),
                "-delay", Long.toString(delay)});
        assertEquals(1, configs.length);
        assertEquals(delay * ConfigParser.DELAY_MULT, configs[0].getDelay());
    }

    @Test
    public void testManyArguments() throws IOException, ParseException, InvalidConfigException {
        long delay = 1;

        File file = prepareMockFile(null);

        Config[] configs = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(),
                "--force",
                "-cs", Config.ConflictStrategy.EXPORT_NEW_PLATFORM.name,
                "-delay",Long.toString(delay)});

        assertEquals(1, configs.length);

        Config config = configs[0];
        assertTrue(config.isForceImport());
        assertEquals(Config.ConflictStrategy.EXPORT_NEW_PLATFORM, config.getConflictStrategy());
        assertEquals(delay * ConfigParser.DELAY_MULT, config.getDelay());
    }

    private File prepareMockFile(Map<String, Object> configMap) throws IOException {
        File file = tempFolder.newFile();
        JSONObject json = configMap != null ? new JSONObject(configMap) : new JSONObject();
        json.put(ConfigParser.PLATFORM, "mock");
        json.put(ConfigParser.SOURCE, "mock");

        PrintWriter writer = new PrintWriter(file);
        writer.write(json.toJSONString());
        writer.flush();
        writer.close();
        return file;
    }

    private File prepareMockFileWithArray(Map<String, Object>... configMaps) throws IOException {
        File file = tempFolder.newFile();

        JSONArray jsonArray = new JSONArray();

        for (Map<String, Object> map: configMaps) {
            JSONObject json = map != null ? new JSONObject(map) : new JSONObject();
            json.put(ConfigParser.PLATFORM, "mock");
            json.put(ConfigParser.SOURCE, "mock");
            jsonArray.add(json);
        }

        PrintWriter writer = new PrintWriter(file);
        writer.write(jsonArray.toJSONString());
        writer.flush();
        writer.close();
        return file;
    }

    private File prepareMockFileNoSource() throws IOException {
        File file = tempFolder.newFile();
        JSONObject json = new JSONObject();
        json.put(ConfigParser.PLATFORM, "mock");
        PrintWriter writer = new PrintWriter(file);
        writer.write(json.toJSONString());
        writer.flush();
        writer.close();
        return file;
    }

    private File prepareMockFileNoPlatform() throws IOException {
        File file = tempFolder.newFile();
        JSONObject json = new JSONObject();
        json.put(ConfigParser.SOURCE, "mock");
        PrintWriter writer = new PrintWriter(file);
        writer.write(json.toJSONString());
        writer.flush();
        writer.close();
        return file;
    }
}
