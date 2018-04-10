/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser;

import com.beust.jcommander.JCommander;
import org.json.simple.JSONArray;
import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.exception.InvalidConfigException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;

import com.beust.jcommander.Parameter;
import ru.pocketbyte.locolaser.utils.LogUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that parse Config from JSON file or string.
 *
 * @author Denis Shurygin
 */
public class ConfigParser {

    public static final String SOURCE = "source";
    public static final String PLATFORM = "platform";
    public static final String WORK_DIR = "work_dir";
    public static final String TEMP_DIR = "temp_dir";

    public static final String FORCE_IMPORT = "force_import";
    public static final String CONFLICT_STRATEGY = "conflict_strategy";
    public static final String DUPLICATE_COMMENTS = "duplicate_comments";
    public static final String DELAY = "delay";

    /**
     * Config delay defined in minutes but code use time in milliseconds.
     * So delay value from config must multiplied by this value.
     * */
    static final long DELAY_MULT = 60000;

    private SourceConfigParser<?> mSourceConfigParser;
    private PlatformConfigParser mPlatformConfigParser;

    /**
     * Construct new config parser
     * @param sourceConfigParser Parser of the source config.
     * @param platformConfigParser Parser of the platform config.
     */
    public ConfigParser(SourceConfigParser<?> sourceConfigParser,
                        PlatformConfigParser platformConfigParser) {
        mSourceConfigParser = sourceConfigParser;
        mPlatformConfigParser = platformConfigParser;
    }

    public SourceConfigParser<?> getSourceConfigParser() {
        return mSourceConfigParser;
    }

    public PlatformConfigParser getPlatformConfigParser() {
        return mPlatformConfigParser;
    }

    /**
     * Parse Config from console arguments.
     * @param args Console arguments.
     * @return Parsed config.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     * @throws IOException if occurs an error reading the file.
     * @throws ParseException if occurs an error parsing JSON.
     */
    public Config[] fromArguments(String[] args) throws IOException, ParseException, InvalidConfigException {
        if(args != null && args.length > 0 && args[0] != null) {
            Config[] configs = fromFile(new File(args[0]));

            ConfigArgsParser argsParser = new ConfigArgsParser();
            new JCommander(argsParser, args);

            for (Config config: configs)
                argsParser.applyFor(config);

            return configs;
        }
        throw new InvalidConfigException("JSON config not defined! Please provide file path for JSON config as a first parameter.");
    }

    /**
     * Parse Config from file.
     * @param file File with JSON config.
     * @return Parsed config.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     * @throws IOException if occurs an error reading the file.
     * @throws ParseException if occurs an error parsing JSON.
     */
    public Config[] fromFile(File file) throws IOException, ParseException, InvalidConfigException {
        LogUtils.info("Reading config file " + file.getCanonicalPath());
        file = new File(file.getCanonicalPath());
        System.setProperty("user.dir", file.getParentFile().getCanonicalPath());

        FileReader reader = new FileReader(file);
        Object json = JsonParseUtils.JSON_PARSER.parse(reader);
        reader.close();

        if (json instanceof JSONObject)
            return new Config[] { fromJsonObject(file, (JSONObject) json) };
        else if (json instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) json;
            Config[] array = new Config[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                array[i] = fromJsonObject(file, (JSONObject) jsonArray.get(i));
            }
            return array;
        }

        throw new InvalidConfigException("Config file must contain JSONObject or JSONArray.");
    }

    private Config fromJsonObject(File file, JSONObject configJson) throws IOException, InvalidConfigException {

        String workDir = JsonParseUtils.getString(configJson, WORK_DIR, null, false);
        if (workDir != null)
            System.setProperty("user.dir", new File(workDir).getCanonicalPath());

        Config config = new Config();
        config.setFile(file);
        config.setForceImport(JsonParseUtils.getBoolean(configJson, FORCE_IMPORT, null, false));
        config.setConflictStrategy(parseConflictStrategy(JsonParseUtils.getString(configJson, CONFLICT_STRATEGY, null, false)));
        config.setDuplicateComments(JsonParseUtils.getBoolean(configJson, DUPLICATE_COMMENTS, null, false));
        config.setDelay(JsonParseUtils.getLong(configJson, DELAY, null, false) * DELAY_MULT);
        config.setTempDir(JsonParseUtils.getFile(configJson, TEMP_DIR, null, false));

        config.setSourceConfig(mSourceConfigParser.parse(JsonParseUtils.getObject(configJson, SOURCE, null, true), true));
        config.setPlatform(mPlatformConfigParser.parse(JsonParseUtils.getObject(configJson, PLATFORM, null, true), true));

        validate(config);

        return config;
    }

    protected void validate(Config config) throws InvalidConfigException {
        if (config.getPlatform() == null)
            throw new InvalidConfigException("\"" + ConfigParser.PLATFORM + "\" is not set.");
        if (config.getSourceConfig() == null)
            throw new InvalidConfigException("\"" + ConfigParser.SOURCE + "\" is not set.");
    }

    private static Config.ConflictStrategy parseConflictStrategy(String strategy) throws InvalidConfigException {
        if (strategy == null)
            return null;

        for (Config.ConflictStrategy enumItem : Config.ConflictStrategy.values()) {
            if (enumItem.name.equals(strategy))
                return enumItem;
        }

        throw new InvalidConfigException("Unknown conflict strategy. Strategy = " + strategy);
    }

    public static class ConfigArgsParser {

        @Parameter
        private List<String> parameters = new ArrayList<>();

        @Parameter(names = { "--force", "--f" })
        private boolean forceImport;

        @Parameter(names = "-cs")
        private String conflictStrategy;

        @Parameter(names = "-delay")
        private Long delay;

        public void applyFor(Config config) throws InvalidConfigException {
            if (forceImport)
                config.setForceImport(true);
            if (conflictStrategy != null) {
                config.setConflictStrategy(parseConflictStrategy(conflictStrategy));
            }
            if (delay != null)
                config.setDelay(delay * DELAY_MULT);
        }
    }
}
