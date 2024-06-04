/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.ExtraParams
import ru.pocketbyte.locolaser.resource.entity.ResMap
import ru.pocketbyte.locolaser.resource.entity.merge
import ru.pocketbyte.locolaser.utils.LogUtils
import ru.pocketbyte.locolaser.utils.PluralUtils
import java.io.IOException

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

        LogUtils.info("Conflict strategy: " + config.conflictStrategy.toString())

        val sourceConfig = config.source ?: throw IllegalArgumentException("Config source must be not null")
        val platformConfig = config.platform ?: throw IllegalArgumentException("Config platform must be not null")

        val resources = platformConfig.resources
        val source = sourceConfig.resources

        // =================================
        // Read source values
        val sourceResMap = source.read(config.locales, ExtraParams())

        if (sourceResMap == null && config.conflictStrategy !== Config.ConflictStrategy.EXPORT_NEW_PLATFORM) {
            LogUtils.info("Source is empty. Localization stopped.")
            return true
        }

        var mergedResMap: ResMap? = null
        // =================================
        // Export local resources if needed
        if (config.conflictStrategy !== Config.ConflictStrategy.REMOVE_PLATFORM) {

            // Read local files
            val localResMap = resources.read(config.locales, ExtraParams())

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

            if (config.trimUnsupportedQuantities) {
                mergedResMap?.trimUnsupportedQuantities()
            }

            if (mergedResMap != null && config.conflictStrategy.isExportStrategy) {
                source.write(mergedResMap, null)
            }
        } else {
            mergedResMap = sourceResMap

            if (config.trimUnsupportedQuantities) {
                mergedResMap?.trimUnsupportedQuantities()
            }
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
        }

        return true
    }

    private fun ResMap.trimUnsupportedQuantities() {
        forEach { (locale, localeMap) ->
            PluralUtils.quantitiesForLocale(locale)?.let { supportedQuantities ->
                localeMap.forEach { _, resItem ->
                    for (i in resItem.values.size - 1 downTo 0) {
                        val value = resItem.values[i]
                        if (!supportedQuantities.contains(value.quantity)) {
                            resItem.removeValue(value)
                        }
                    }
                }
            }
        }
    }
}
