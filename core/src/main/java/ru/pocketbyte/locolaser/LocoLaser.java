/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser;

import ru.pocketbyte.locolaser.config.Config;
import ru.pocketbyte.locolaser.config.platform.PlatformConfig;
import ru.pocketbyte.locolaser.config.source.Source;
import ru.pocketbyte.locolaser.config.source.SourceConfig;
import ru.pocketbyte.locolaser.resource.PlatformResources;
import ru.pocketbyte.locolaser.resource.entity.ResLocale;
import ru.pocketbyte.locolaser.resource.entity.ResMap;
import ru.pocketbyte.locolaser.resource.entity.ResValue;
import ru.pocketbyte.locolaser.resource.entity.ResItem;
import ru.pocketbyte.locolaser.summary.Summary;
import ru.pocketbyte.locolaser.utils.LogUtils;

import java.io.IOException;
import java.util.*;

/**
 * This class contain logic of localization by specified localization config.
 *
 * @author Denis Shurygin
 */
public class LocoLaser {

    /**
     * Build localization files by specified config.
     * @param config Localization config.
     * @return Return true if localization success, false otherwise.
     */
    public static boolean localize(Config config) {
        long startTime = System.currentTimeMillis();
        boolean result = localizeInner(config);
        LogUtils.info("Total time: " + (System.currentTimeMillis() - startTime) + " millis.");
        return result;
    }

    private static boolean localizeInner(Config config) {
        if (config == null)
            throw new IllegalArgumentException("Config must be not null");
        if (config.getPlatform() == null)
            throw new IllegalArgumentException("Config platform must be not null");
        if (config.getSourceConfig() == null)
            throw new IllegalArgumentException("Config source must be not null");

        PlatformConfig platform = config.getPlatform();
        PlatformResources resources = platform.getResources();

        Summary summary = Summary.loadSummary(config);
        boolean isConfigFileChanged = summary == null || summary.isConfigFileChanged(config.getFile());

        if (summary != null) {
            if (config.isForceImport())
                LogUtils.info("Force import. All resource files will be refreshed.");
            else if (isConfigFileChanged)
                LogUtils.info("Config file was changed. All resource files will be refreshed.");
            else if (!summary.isDelayPassed(config.getDelay())) {
                LogUtils.info("Delay is not passed. Localization stopped.");
                return true;
            }
        }

        LogUtils.info("Conflict strategy: " + config.getConflictStrategy().toString());

        boolean isRefreshAll = config.isForceImport() || isConfigFileChanged;

        SourceConfig sourceConfig = config.getSourceConfig();
        Source source = sourceConfig.open();
        if (source == null)
            return false;

        long sourceModifiedDate = source.getModifiedDate();

        boolean isSourceChanged = summary == null
                || summary.getSourceModifiedDate() == 0
                || sourceModifiedDate != summary.getSourceModifiedDate();

        if (summary != null)
            summary.setSourceModifiedDate(sourceModifiedDate);

        if (!isRefreshAll) {
            if (isSourceChanged) {
                LogUtils.info("Source was changed. All resource files will be refreshed.");
                isRefreshAll = true;
            } else
                LogUtils.info("Source have not been changed since the last import. Only modified resource files will be refreshed.");
        }

        // Prepare resource files
        boolean isHaveFileToTranslate = false;
        Set<String> resourcesToIgnore = new HashSet<>(); // set of locales
        Set<String> resourcesToLocalize = new HashSet<>(); // set of locales
        for (String locale : sourceConfig.getLocales()) {
            if (!resourcesToLocalize.contains(locale)) {
                boolean isResourceFileChanged = summary == null || summary.isResourceLocaleChanged(resources, locale);
                if (isRefreshAll || isResourceFileChanged) {
                    isHaveFileToTranslate = true;
                    resourcesToLocalize.add(locale);
                    resourcesToIgnore.remove(locale);
                } else if (!resourcesToLocalize.contains(locale)) { // summary != null && !isResourceFileChanged
                    resourcesToIgnore.add(locale);
                }
            }
        }

        for (String ignoredLocale: resourcesToIgnore)
            LogUtils.info("Resource file for "
                    + "locale \"" + ignoredLocale + "\""
                    + " have not been changed since the last import. Ignore this file.");

        if (!isHaveFileToTranslate) {
            LogUtils.info("Nothing to update. Localization stopped.");

            // Override summary
            if (summary != null && config.getFile() != null) {
                summary.save();
            }
        }
        else {

            // =================================
            // Read source values
            Source.ReadResult result = source.read();
            List<Source.MissedValue> missedList = result.missedValues;
            ResMap sourceResMap = result.items;

            if (sourceResMap == null && config.getConflictStrategy() != Config.ConflictStrategy.EXPORT_NEW_PLATFORM) {
                LogUtils.info("Source is empty. Localization stopped.");
                return true;
            }

            // Remove ignored resources
            for (String locale: resourcesToIgnore) {
                sourceResMap.remove(locale);
            }

            // =================================
            // Export local resources if needed
            if (config.getConflictStrategy() != Config.ConflictStrategy.REMOVE_PLATFORM) {

                // Read local files
                ResMap oldResMap = resources.read(resourcesToLocalize).remove(sourceResMap);

                // First part. Find missed values
                Map<Source, ResMap> foundResMaps = new HashMap<>();
                if (missedList != null) for (Source.MissedValue missed : missedList) {
                    ResLocale oldResLocale = oldResMap.get(missed.locale);
                    if (oldResLocale != null) {
                        ResItem oldResItem = oldResLocale.get(missed.key);
                        if (oldResItem != null) {

                            ResValue oldResValue = oldResItem.valueForQuantity(missed.quantity);

                            if (oldResValue != null) {
                                oldResValue.setLocation(missed.location);

                                ResMap foundResMap = foundResMaps.get(missed.location.source);

                                if (foundResMap == null) {
                                    foundResMap = new ResMap();
                                    foundResMaps.put(missed.location.source, foundResMap);
                                }
                                ResLocale foundResLocale = foundResMap.get(missed.locale);
                                if (foundResLocale == null) {
                                    foundResLocale = new ResLocale();
                                    foundResMap.put(missed.locale, foundResLocale);
                                }

                                ResItem foundResItem = foundResLocale.get(oldResItem.key);
                                if (foundResItem == null) {
                                    foundResItem = new ResItem(oldResItem.key);
                                    foundResLocale.put(foundResItem);
                                }

                                foundResItem.addValue(oldResValue);
                                oldResItem.removeValue(oldResValue);

                                if (oldResItem.values.size() == 0)
                                    oldResLocale.remove(oldResItem.key);
                            }
                        }
                    }
                }
                for (Map.Entry<Source, ResMap> entry: foundResMaps.entrySet()) {
                    if (config.getConflictStrategy() == Config.ConflictStrategy.EXPORT_NEW_PLATFORM
                                                                                    && entry.getValue().size() > 0) {
                        // Key is Source
                        entry.getKey().write(entry.getValue());
                    }

                    // Save into new map
                    if (sourceResMap != null)
                        sourceResMap = sourceResMap.merge(entry.getValue());
                    else
                        sourceResMap = entry.getValue();
                }

                // Second part. Export other values into the first source.
                if (config.getConflictStrategy() == Config.ConflictStrategy.EXPORT_NEW_PLATFORM && oldResMap.size() > 0) {
                    source.write(oldResMap);
                }

                // Save other local items into map
                if (oldResMap != null)
                    sourceResMap = oldResMap.merge(sourceResMap);
            }

            // =================================
            // Write resource files
            try {
                resources.write(sourceResMap, config.writingConfig);
            } catch (IOException e) {
                LogUtils.err(e);
                return false;
            }
            for (Map.Entry<String, ResLocale> entry : sourceResMap.entrySet()) {
                // entry.key = locale, entry.value = strings of the locale

                String locale = entry.getKey();
                if (resourcesToLocalize.contains(locale)) {

                    // Getting new summary
                    if (summary != null) {
                        summary.setResourceSummary(locale, resources.summaryForLocale(locale));
                    }
                }
            }

            // =================================
            // Save summary into file.
            if (summary != null && config.getFile() != null) {
                summary.setConfigSummary(config);
                summary.save();
            }
        }

        // =================================
        // Close sources
        source.close();

        return true;
    }
}
