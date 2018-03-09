/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser;

import javafx.util.Pair;
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
            public MockTableSourceConfig parse(Object sourceObject) throws InvalidConfigException {
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
    public void testDefaultValues() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFile(null);
        Config config = mConfigParser.fromFile(file);

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
        Config config = mConfigParser.fromFile(file);

        assertTrue(config.isForceImport());

        map.put(ConfigParser.FORCE_IMPORT, false);

        file = prepareMockFile(map);
        config = mConfigParser.fromFile(file);

        assertFalse(config.isForceImport());
    }

    @Test
    public void testJsonDuplicateComments() throws IOException, ParseException, InvalidConfigException {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.DUPLICATE_COMMENTS, true);

        File file = prepareMockFile(map);
        Config config = mConfigParser.fromFile(file);

        assertTrue(config.isDuplicateComments());

        map.put(ConfigParser.DUPLICATE_COMMENTS, false);

        file = prepareMockFile(map);
        config = mConfigParser.fromFile(file);

        assertFalse(config.isDuplicateComments());
    }

    @Test
    public void testJsonWorkDir() throws IOException, ParseException, InvalidConfigException {
        String newWorkDir = "./new/work/dir";
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.WORK_DIR, newWorkDir);

        File file = prepareMockFile(map);
        Config config = mConfigParser.fromFile(file);

        assertEquals(new File(file.getParentFile(), newWorkDir).getCanonicalPath(), System.getProperty("user.dir"));
    }

    @Test
    public void testJsonTempDir() throws IOException, ParseException, InvalidConfigException {
        String tempDir = "./new/work/dir";
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.TEMP_DIR, tempDir);

        File file = prepareMockFile(map);
        Config config = mConfigParser.fromFile(file);

        assertEquals(new File(tempDir).getCanonicalPath(), config.getTempDir().getCanonicalPath());
    }

    @Test
    public void testDefaultTempDir() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFile(new HashMap<String, Object>());
        Config config = mConfigParser.fromFile(file);

        File expected = new MockPlatformConfig().getDefaultTempDir();

        assertEquals(expected.getCanonicalPath(), config.getTempDir().getCanonicalPath());
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
            Config config = mConfigParser.fromFile(file);

            assertEquals(pair.getValue(), config.getConflictStrategy());
        }
    }

    @Test
    public void testJsonDelay() throws IOException, ParseException, InvalidConfigException {
        long delay = 1;
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.DELAY, delay);

        File file = prepareMockFile(map);
        Config config = mConfigParser.fromFile(file);

        assertEquals(delay * ConfigParser.DELAY_MULT, config.getDelay());
    }

    @Test
    public void testFromArguments() throws IOException, ParseException, InvalidConfigException {
        File file = prepareMockFile(null);
        Config config = mConfigParser.fromArguments(new String[]{file.getAbsolutePath()});
        assertNotNull(config);
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

        Config config = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(), "--force"});
        assertTrue(config.isForceImport());

        config = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(), "--f"});
        assertTrue(config.isForceImport());
    }

    @Test
    public void testArgumentConflictStrategy() throws IOException, ParseException, InvalidConfigException {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ConfigParser.CONFLICT_STRATEGY, Config.ConflictStrategy.KEEP_NEW_PLATFORM.name);

        File file = prepareMockFile(map);

        Config config = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(),
                "-cs", Config.ConflictStrategy.EXPORT_NEW_PLATFORM.name});
        assertEquals(Config.ConflictStrategy.EXPORT_NEW_PLATFORM, config.getConflictStrategy());

        config = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(),
                "-cs ", Config.ConflictStrategy.REMOVE_PLATFORM.name});
        assertEquals(Config.ConflictStrategy.REMOVE_PLATFORM, config.getConflictStrategy());
    }

    @Test
    public void testArgumentDelay() throws IOException, ParseException, InvalidConfigException {
        long delay = 1;

        File file = prepareMockFile(null);

        Config config = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(),
                "-delay", Long.toString(delay)});
        assertEquals(delay * ConfigParser.DELAY_MULT, config.getDelay());
    }

    @Test
    public void testManyArguments() throws IOException, ParseException, InvalidConfigException {
        long delay = 1;

        File file = prepareMockFile(null);

        Config config = mConfigParser.fromArguments(new String[]{file.getAbsolutePath(),
                "--force",
                "-cs", Config.ConflictStrategy.EXPORT_NEW_PLATFORM.name,
                "-delay",Long.toString(delay)});

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
