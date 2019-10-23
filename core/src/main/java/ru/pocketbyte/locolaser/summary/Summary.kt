/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.summary

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils
import org.json.simple.JSONObject
import org.json.simple.parser.ParseException

import java.io.*
import java.util.HashMap

/**
 * Helper class to work with summary files which remember last localization results.
 * It help don't run localization if that is unnecessary.
 *
 * @author Denis Shurygin
 */
class Summary private constructor(file: File?) {

    companion object {
        const val SOURCE_MODIFIED_DATE = "SOURCE_MODIFIED_DATE"
        const val CONFIG_FILE = "CONFIG_FILE"
        const val RESOURCE_FILES = "RESOURCE_FILES"

        const val SUMMARY_FILE_NAME = "locolaser.summary"

        private var sFactory: Factory? = null

        internal fun setFactory(factory: Factory?) {
            sFactory = factory
        }

        /**
         * Load summary file for specified config.
         * @param config Localization config.
         * @return Localization summary or null if there is no opportunity to build summary for current config.
         */
        fun loadSummary(config: Config?): Summary? {
            val factory = sFactory
            if (factory != null)
                return factory.loadSummary(config)

            var summary: Summary? = null
            // Config file should be not null. Summary is not valid without config file.
            if (config?.file != null) {
                var summaryFile: File? = null
                val tempDir = config.tempDir
                if (tempDir!!.exists() || tempDir.mkdirs())
                    summaryFile = File(tempDir, SUMMARY_FILE_NAME)

                if (summaryFile != null)
                    try {
                        val reader = BufferedReader(InputStreamReader(FileInputStream(summaryFile), "UTF-8"))
                        summary = Summary(summaryFile, JsonParseUtils.JSON_PARSER.parse(reader) as JSONObject)
                        reader.close()
                    } catch (e: ParseException) {
                        summary = Summary(summaryFile)
                    } catch (e: FileNotFoundException) {
                        summary = Summary(summaryFile)
                    } catch (e: IOException) {
                        // do nothing
                    }

            }
            return summary
        }
    }

    interface Factory {
        fun loadSummary(config: Config?): Summary?
    }

    internal val file: File?
    var configSummary: FileSummary? = null
        private set

    var sourceModifiedDate: Long = 0
    private val mResourceSummaries = HashMap<String, FileSummary>()

    init {
        if (file == null)
            throw IllegalArgumentException("Summary file must be not null")

        var canonicalFile: File? = file

        try {
            canonicalFile = File(canonicalFile!!.canonicalPath)
        } catch (e: IOException) {
            throw IllegalArgumentException("Invalid summary file: Failed to get canonical path")
        }

        this.file = canonicalFile
    }

    constructor(file: File?, json: JSONObject?) : this(file) {

        if (json != null) {
            sourceModifiedDate = (json[SOURCE_MODIFIED_DATE] as? Number)?.toLong() ?: sourceModifiedDate
            configSummary = (json[CONFIG_FILE] as? JSONObject)?.let { FileSummary(it) } ?: configSummary

            (json[RESOURCE_FILES] as? JSONObject)?.entries
                    ?.map { it as Map.Entry<*, *> }
                    ?.filter { it.value is JSONObject }
                    ?.forEach { mResourceSummaries[it.key as String] = FileSummary(it.value as JSONObject) }
        }
    }

    fun getResourceSummary(locale: String?): FileSummary? {
        return mResourceSummaries[locale ?: "null"]
    }

    fun isDelayPassed(delay: Long): Boolean {
        if (delay == 0L)
            return true

        if (file != null && file.exists()) {
            val currentDelay = System.currentTimeMillis() - file.lastModified()
            return currentDelay > delay
        }
        return true
    }

    fun isResourceLocaleChanged(resources: PlatformResources, locale: String): Boolean {
        val fileSummary = getResourceSummary(locale)
        return fileSummary == null || fileSummary != resources.summaryForLocale(locale)
    }

    fun isConfigFileChanged(configFile: File): Boolean {
        return configSummary == null || !configSummary!!.equalsToFile(configFile)
    }

    fun setResourceSummary(locale: String, summary: FileSummary) {
        mResourceSummaries[locale] = summary
    }

    fun setConfigSummary(config: Config?) {
        if (config?.file != null)
            this.configSummary = FileSummary(config.file)
        else
            this.configSummary = null
    }

    /**
     * Save summary into file.
     */
    fun save() {
        try {
            file!!.parentFile.mkdirs()
            val writer = FileWriter(file)
            writer.write(toJson().toJSONString())
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    internal fun toJson(): JSONObject {
        val json = JSONObject()

        json[CONFIG_FILE] = configSummary!!.toJson()
        json[SOURCE_MODIFIED_DATE] = sourceModifiedDate

        val resourceFilesJson = JSONObject()
        for (local in mResourceSummaries.keys) {
            val fileSummary = mResourceSummaries[local]
            if (fileSummary != null)
                resourceFilesJson[local] = fileSummary.toJson()
        }
        json[RESOURCE_FILES] = resourceFilesJson
        return json
    }
}
