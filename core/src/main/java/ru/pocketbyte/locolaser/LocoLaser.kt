/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.config.source.Source
import ru.pocketbyte.locolaser.config.source.SourceConfig
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.ResLocale
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.ResValue
import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.summary.Summary
import ru.pocketbyte.locolaser.utils.LogUtils

import java.io.IOException
import java.util.*

/**
 * This class contain logic of localization by specified localization config.
 *
 * @author Denis Shurygin
 */
object LocoLaser {

    /**
     * Build localization files by specified config.
     * @param config Localization config.
     * @return Return true if localization success, false otherwise.
     */
    fun localize(config: Config): Boolean {
        val startTime = System.currentTimeMillis()
        val result = localizeInner(config)
        LogUtils.info("Total time: " + (System.currentTimeMillis() - startTime) + " millis.")
        return result
    }

    private fun localizeInner(config: Config?): Boolean {
        if (config == null)
            throw IllegalArgumentException("Config must be not null")
        if (config.platform == null)
            throw IllegalArgumentException("Config platform must be not null")
        if (config.sourceConfig == null)
            throw IllegalArgumentException("Config source must be not null")

        val platform = config.platform
        val resources = platform!!.resources

        val summary = Summary.loadSummary(config)
        val isConfigFileChanged = summary == null || summary.isConfigFileChanged(config.file!!)

        if (summary != null) {
            if (config.isForceImport)
                LogUtils.info("Force import. All resource files will be refreshed.")
            else if (isConfigFileChanged)
                LogUtils.info("Config file was changed. All resource files will be refreshed.")
            else if (!summary.isDelayPassed(config.delay)) {
                LogUtils.info("Delay is not passed. Localization stopped.")
                return true
            }
        }

        LogUtils.info("Conflict strategy: " + config.conflictStrategy!!.toString())

        var isRefreshAll = config.isForceImport || isConfigFileChanged

        val sourceConfig = config.sourceConfig
        val source = sourceConfig!!.open() ?: return false

        val sourceModifiedDate = source.modifiedDate

        val isSourceChanged = summary == null
                || summary.sourceModifiedDate == 0L
                || sourceModifiedDate != summary.sourceModifiedDate

        if (summary != null)
            summary.sourceModifiedDate = sourceModifiedDate

        if (!isRefreshAll) {
            if (isSourceChanged) {
                LogUtils.info("Source was changed. All resource files will be refreshed.")
                isRefreshAll = true
            } else
                LogUtils.info("Source have not been changed since the last import. Only modified resource files will be refreshed.")
        }

        // Prepare resource files
        var isHaveFileToTranslate = false
        val resourcesToIgnore = HashSet<String>() // set of locales
        val resourcesToLocalize = HashSet<String>() // set of locales
        for (locale in sourceConfig.locales) {
            if (!resourcesToLocalize.contains(locale)) {
                val isResourceFileChanged = summary == null || summary.isResourceLocaleChanged(resources, locale)
                if (isRefreshAll || isResourceFileChanged) {
                    isHaveFileToTranslate = true
                    resourcesToLocalize.add(locale)
                    resourcesToIgnore.remove(locale)
                } else if (!resourcesToLocalize.contains(locale)) { // summary != null && !isResourceFileChanged
                    resourcesToIgnore.add(locale)
                }
            }
        }

        for (ignoredLocale in resourcesToIgnore)
            LogUtils.info("Resource file for "
                    + "locale \"" + ignoredLocale + "\""
                    + " have not been changed since the last import. Ignore this file.")

        if (!isHaveFileToTranslate) {
            LogUtils.info("Nothing to update. Localization stopped.")

            // Override summary
            if (summary != null && config.file != null) {
                summary.save()
            }
        } else {

            // =================================
            // Read source values
            val result = source.read()
            val missedList = result.missedValues
            var sourceResMap = result.items

            if (sourceResMap == null && config.conflictStrategy !== Config.ConflictStrategy.EXPORT_NEW_PLATFORM) {
                LogUtils.info("Source is empty. Localization stopped.")
                return true
            }

            // Remove ignored resources
            for (locale in resourcesToIgnore) {
                sourceResMap!!.remove(locale)
            }

            // =================================
            // Export local resources if needed
            if (config.conflictStrategy !== Config.ConflictStrategy.REMOVE_PLATFORM) {

                // Read local files
                val oldResMap = resources.read(resourcesToLocalize).remove(sourceResMap)

                // First part. Find missed values
                val foundResMaps = HashMap<Source, ResMap>()
                if (missedList != null)
                    for (missed in missedList) {
                        val oldResLocale = oldResMap[missed.locale]
                        if (oldResLocale != null) {
                            val oldResItem = oldResLocale[missed.key]
                            if (oldResItem != null) {

                                val oldResValue = oldResItem.valueForQuantity(missed.quantity)

                                if (oldResValue != null) {
                                    oldResValue.location = missed.location

                                    var foundResMap: ResMap? = foundResMaps[missed.location.source]

                                    if (foundResMap == null) {
                                        foundResMap = ResMap()
                                        foundResMaps.put(missed.location.source, foundResMap)
                                    }
                                    var foundResLocale: ResLocale? = foundResMap[missed.locale]
                                    if (foundResLocale == null) {
                                        foundResLocale = ResLocale()
                                        foundResMap.put(missed.locale, foundResLocale)
                                    }

                                    var foundResItem: ResItem? = foundResLocale[oldResItem.key]
                                    if (foundResItem == null) {
                                        foundResItem = ResItem(oldResItem.key)
                                        foundResLocale.put(foundResItem)
                                    }

                                    foundResItem.addValue(oldResValue)
                                    oldResItem.removeValue(oldResValue)

                                    if (oldResItem.values.isEmpty())
                                        oldResLocale.remove(oldResItem.key)
                                }
                            }
                        }
                    }
                for ((key, value) in foundResMaps) {
                    if (config.conflictStrategy === Config.ConflictStrategy.EXPORT_NEW_PLATFORM && value.size > 0) {
                        // Key is Source
                        key.write(value)
                    }

                    // Save into new map
                    sourceResMap = sourceResMap?.merge(value) ?: value
                }

                // Second part. Export other values into the first source.
                if (config.conflictStrategy === Config.ConflictStrategy.EXPORT_NEW_PLATFORM && oldResMap.size > 0) {
                    source.write(oldResMap)
                }

                // Save other local items into map
                sourceResMap = oldResMap.merge(sourceResMap)
            }

            // =================================
            // Write resource files
            try {
                resources.write(sourceResMap!!, config.writingConfig)
            } catch (e: IOException) {
                LogUtils.err(e)
                return false
            }

            for ((locale) in sourceResMap) {
                // entry.key = locale, entry.value = strings of the locale

                if (resourcesToLocalize.contains(locale)) {

                    // Getting new summary
                    summary?.setResourceSummary(locale, resources.summaryForLocale(locale))
                }
            }

            // =================================
            // Save summary into file.
            if (summary != null && config.file != null) {
                summary.setConfigSummary(config)
                summary.save()
            }
        }

        // =================================
        // Close sources
        source.close()

        return true
    }
}
