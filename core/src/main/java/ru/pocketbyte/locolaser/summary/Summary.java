/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.summary;

import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.utils.JsonParseUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;

/**
 * Helper class to work with summary files which remember last localization results.
 * It help don't run localization if that is unnecessary.
 *
 * @author Denis Shurygin
 */
public class Summary {

    public static final String SOURCE_MODIFIED_DATE = "SOURCE_MODIFIED_DATE";
    public static final String CONFIG_FILE = "CONFIG_FILE";
    public static final String RESOURCE_FILES = "RESOURCE_FILES";

    public static final String SUMMARY_FILE_NAME = "locolaser.summary";


    public interface Factory {
        Summary loadSummary(Config config);
    }

    private static Factory sFactory;

    public static void setFactory(Factory factory) {
        sFactory = factory;
    }

    /**
     * Load summary file for specified config.
     * @param config Localization config.
     * @return Localization summary or null if there is no opportunity to build summary for current config.
     */
    public static Summary loadSummary(Config config) {
        Factory factory = sFactory;
        if (factory != null)
            return factory.loadSummary(config);

        Summary summary = null;
        // Config file should be not null. Summary is not valid without config file.
        if (config != null && config.getFile() != null) {
            File summaryFile = null;
            File tempDir = config.getTempDir();
            if (tempDir.exists() || tempDir.mkdirs())
                summaryFile = new File(tempDir, SUMMARY_FILE_NAME);

            if (summaryFile != null) try {
                FileReader reader = new FileReader(summaryFile);
                summary = new Summary(summaryFile, (JSONObject) JsonParseUtils.JSON_PARSER.parse(reader));
                reader.close();
            } catch (ParseException | FileNotFoundException e) {
                summary = new Summary(summaryFile);
            } catch (IOException e) {
                // do nothing
            }
        }
        return summary;
    }

    final File file;
    private FileSummary mConfigFileSummary;
    private long mSourceModifiedDate;
    private HashMap<String, FileSummary> mResourceSummaries = new HashMap<>();

    private Summary(File file) {
        if (file == null)
            throw new IllegalArgumentException("Summary file must be not null");

        try {
            file = new File(file.getCanonicalPath());
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid summary file: Failed to get canonical path");
        }

        this.file = file;
    }

    public Summary(File file, JSONObject json) {
        this(file);

        if (json != null) {
            Object object = json.get(SOURCE_MODIFIED_DATE);
            if (object instanceof Number) {
                mSourceModifiedDate = ((Number) object).longValue();
            }

            object = json.get(CONFIG_FILE);
            if (object instanceof JSONObject) {
                mConfigFileSummary = new FileSummary((JSONObject) object);
            }

            object = json.get(RESOURCE_FILES);
            if (object instanceof JSONObject) {
                JSONObject resourceFilesJson = (JSONObject) object;
                for (Object locale : resourceFilesJson.keySet()) {
                    if (locale instanceof String) {
                        object = resourceFilesJson.get(locale);
                        if (object instanceof JSONObject) {
                            mResourceSummaries.put((String) locale, new FileSummary((JSONObject) object));
                        }
                    }
                }
            }
        }
    }

    // =================================================================================================================
    // Getters
    // =================================================================================================================

    public long getSourceModifiedDate() {
        return mSourceModifiedDate;
    }

    public FileSummary getResourceSummary(String locale) {
        return mResourceSummaries.get(locale==null ? "null" : locale);
    }

    public FileSummary getConfigSummary() {
        return mConfigFileSummary;
    }

    public boolean isDelayPassed(long delay) {
        if (delay == 0)
            return true;

        if (file != null && file.exists()) {
            long currentDelay =  System.currentTimeMillis() - file.lastModified();
            return currentDelay > delay ;
        }
        return true;
    }

    public boolean isResourceLocaleChanged(PlatformResources resources, String locale) {
        FileSummary fileSummary = getResourceSummary(locale);
        return fileSummary == null || !fileSummary.equals(resources.summaryForLocale(locale));
    }

    public boolean isConfigFileChanged(File configFile) {
        return mConfigFileSummary == null || !mConfigFileSummary.equalsToFile(configFile);
    }

    // =================================================================================================================
    // Setters
    // =================================================================================================================

    public void setSourceModifiedDate(long sourceModifiedDate) {
        mSourceModifiedDate = sourceModifiedDate;
    }

    public void setResourceSummary(String locale, FileSummary summary) {
        mResourceSummaries.put(locale, summary);
    }

    public void setConfigSummary(Config config) {
        if (config != null && config.getFile() != null)
            this.mConfigFileSummary = new FileSummary(config.getFile());
        else
            this.mConfigFileSummary = null;
    }

    /**
     * Save summary into file.
     */
    public void save() {
        try {
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write(toJson().toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put(CONFIG_FILE, mConfigFileSummary.toJson());
        json.put(SOURCE_MODIFIED_DATE, mSourceModifiedDate);

        JSONObject resourceFilesJson = new JSONObject();
        for (String local : mResourceSummaries.keySet()) {
            FileSummary fileSummary = mResourceSummaries.get(local);
            if (fileSummary != null)
                resourceFilesJson.put(local, fileSummary.toJson());
        }
        json.put(RESOURCE_FILES, resourceFilesJson);
        return json;
    }
}
