/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.merge
import ru.pocketbyte.locolaser.summary.Summary
import ru.pocketbyte.locolaser.summary.plus
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

        val summary = Summary.loadSummary(config)
        val isConfigFileChanged = summary?.isConfigFileChanged(config.file) ?: true
        val isRefreshAll = config.isForceImport || isConfigFileChanged

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

        LogUtils.info("Conflict strategy: " + config.conflictStrategy.toString())

        val sourceConfig = config.sourceConfig ?: throw IllegalArgumentException("Config source must be not null")
        val platformConfig = config.platform ?: throw IllegalArgumentException("Config platform must be not null")

        val resources = platformConfig.resources
        val source = sourceConfig.resources

        // Prepare resource files
        var isHaveFileToTranslate = false
        val resourcesToIgnore = HashSet<String>() // set of locales
        val resourcesToLocalize = HashSet<String>() // set of locales
        for (locale in config.locales) {
            if (!resourcesToLocalize.contains(locale)) {
                val isLocaleChanged =
                        isRefreshAll ||
                        summary == null ||
                        summary.isLocaleChanged(resources, source, locale)
                if (isLocaleChanged) {
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
            val sourceResMap = source.read(config.locales, ExtraParams())

            if (sourceResMap == null && config.conflictStrategy !== Config.ConflictStrategy.EXPORT_NEW_PLATFORM) {
                LogUtils.info("Source is empty. Localization stopped.")
                return true
            }

            // Remove ignored resources
            for (locale in resourcesToIgnore) {
                sourceResMap?.remove(locale)
            }

            var mergedResMap: ResMap? = null
            // =================================
            // Export local resources if needed
            if (config.conflictStrategy !== Config.ConflictStrategy.REMOVE_PLATFORM) {

                // Read local files
                val localResMap = resources.read(resourcesToLocalize, ExtraParams())

                when (config.conflictStrategy) {
                    Config.ConflictStrategy.KEEP_NEW_PLATFORM,
                    Config.ConflictStrategy.EXPORT_NEW_PLATFORM -> {
                        mergedResMap = localResMap.merge(sourceResMap)
                    }
                    Config.ConflictStrategy.KEEP_PLATFORM,
                    Config.ConflictStrategy.EXPORT_PLATFORM -> {
                        mergedResMap = sourceResMap.merge(localResMap)
                    }
                    else -> { /* Do nothing */ }
                }

                if (mergedResMap != null && config.conflictStrategy.isExportStrategy) {
                    source.write(mergedResMap, null)
                }
            } else {
                mergedResMap = sourceResMap
            }

            // =================================
            // Write resource files
            if (mergedResMap != null) {
                try {
                    resources.write(mergedResMap, config.extraParams)
                } catch (e: IOException) {
                    LogUtils.err(e)
                    return false
                }

                for ((locale) in mergedResMap) {
                    // entry.key = locale, entry.value = strings of the locale
                    if (resourcesToLocalize.contains(locale)) {
                        // Getting new summary
                        (resources.summaryForLocale(locale) + source.summaryForLocale(locale))?.let {
                            summary?.setResourceSummary(locale, it)
                        }
                    }
                }
            }

            // =================================
            // Save summary into file.
            if (summary != null && config.file != null) {
                summary.setConfigSummary(config)
                summary.save()
            }
        }

        return true
    }
}
