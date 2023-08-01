/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser

import com.beust.jcommander.JCommander
import org.json.simple.JSONArray
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import org.json.simple.JSONObject
import org.json.simple.parser.ParseException
import ru.pocketbyte.locolaser.utils.json.JsonParseUtils

import com.beust.jcommander.Parameter
import ru.pocketbyte.locolaser.config.ConfigBuilder
import ru.pocketbyte.locolaser.config.resources.ResourcesSetConfig
import ru.pocketbyte.locolaser.utils.LogUtils
import ru.pocketbyte.locolaser.utils.buildFileFrom
import java.io.*

import java.util.ArrayList

/**
 * Helper class that parse Config from JSON file or string.
 *
 * @author Denis Shurygin
 */
@Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
open class ConfigParser
/**
 * Construct new config parser
 * @param sourceConfigParser Parser of the source config.
 * @param platformConfigParser Parser of the platform config.
 */
(
    private val sourceConfigParser: ResourcesConfigParser<*>,
    private val platformConfigParser: ResourcesConfigParser<*>
) {

    companion object {

        const val SOURCE = "source"
        const val PLATFORM = "platform"
        const val WORK_DIR = "work_dir"
        const val TEMP_DIR = "temp_dir"
        const val LOCALES = "locales"

        const val FORCE_IMPORT = "force_import"
        const val CONFLICT_STRATEGY = "conflict_strategy"
        const val DUPLICATE_COMMENTS = "duplicate_comments"
        const val TRIM_UNSUPPORTED_QUANTITIES = "trim_unsupported_quantities"
        const val DELAY = "delay"

        @Throws(InvalidConfigException::class)
        private fun parseConflictStrategy(strategy: String?): Config.ConflictStrategy? {
            if (strategy == null)
                return null

            for (enumItem in Config.ConflictStrategy.values()) {
                if (enumItem.strValue == strategy)
                    return enumItem
            }

            throw InvalidConfigException("Unknown conflict strategy. Strategy = $strategy")
        }
    }

    /**
     * Parse Config from console arguments.
     * @param args Console arguments.
     * @return Parsed config.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     * @throws IOException if occurs an error reading the file.
     * @throws ParseException if occurs an error parsing JSON.
     */
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun fromArguments(args: Array<String>?): List<ConfigBuilder> {
        if (args != null && args.isNotEmpty()) {

            val argsParser = ConfigArgsParser()
            JCommander(argsParser, *args)

            val file = File(File(args[0]).canonicalPath)
            val workDir = argsParser.workDir?.let { File(file.parentFile, it) }
            val configs = fromFile(file, workDir)

            for (config in configs)
                argsParser.applyFor(config)

            return configs
        }
        throw InvalidConfigException("JSON config not defined! Please provide file path for JSON config as a first parameter.")
    }

    /**
     * Parse Config from file.
     * @param file File with JSON config.
     * @param workDir Work directory.
     * @return Parsed config.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     * @throws IOException if occurs an error reading the file.
     * @throws ParseException if occurs an error parsing JSON.
     */
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun fromFile(file: File, workDir: File? = null): List<ConfigBuilder> {
        LogUtils.info("Reading config file " + file.canonicalPath)

        @Suppress("NAME_SHADOWING")
        val file = File(file.canonicalPath)

        val reader = BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8"))
        val json = JsonParseUtils.JSON_PARSER.parse(reader)
        reader.close()

        if (json is JSONObject) {
            return listOf(ConfigBuilder().apply { fillFromJsonObject(file, json, workDir) })
        }
        else if (json is JSONArray) {
            return json.indices.map { index ->
                val jsonItem = json[index] as JSONObject

                ConfigBuilder().apply { fillFromJsonObject(file, jsonItem, workDir) }
            }
        }

        throw InvalidConfigException("Config file must contain JSONObject or JSONArray.")
    }

    fun fillFromFile(config: ConfigBuilder, file: File, workDir: File?) {
        LogUtils.info("Reading config file " + file.canonicalPath)

        @Suppress("NAME_SHADOWING")
        val file = File(file.canonicalPath)

        val reader = BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8"))
        val json = JsonParseUtils.JSON_PARSER.parse(reader)
        reader.close()

        if (json is JSONObject) {
            config.fillFromJsonObject(file, json, workDir)
        } else {
            throw InvalidConfigException("Config file must contain JSONObject.")
        }
    }

    @Throws(IOException::class, InvalidConfigException::class)
    private fun ConfigBuilder.fillFromJsonObject(
        file: File, configJson: JSONObject, workDir: File?
    ) {
        this.workDir = workDir
            ?: JsonParseUtils.getString(configJson, WORK_DIR)?.let {
                buildFileFrom(file.parentFile, it)
            }
            ?: file.parentFile

        this.file = file

        JsonParseUtils.getBoolean(configJson, FORCE_IMPORT, null, false)?.let {
            this.forceImport = it
        }
        JsonParseUtils.getBoolean(configJson, DUPLICATE_COMMENTS, null, false)?.let {
            this.duplicateComments = it
        }
        JsonParseUtils.getBoolean(configJson, TRIM_UNSUPPORTED_QUANTITIES, null, false)?.let {
            this.trimUnsupportedQuantities = it
        }
        JsonParseUtils.getLong(configJson, DELAY, null, false)?.let {
            this.delay = it
        }
        this.tempDir = JsonParseUtils.getString(configJson, TEMP_DIR, null, false)

        sourceConfigParser.parse(JsonParseUtils.getObject(configJson, SOURCE, null, true), true)?.let {
            if (it is ResourcesSetConfig) {
                it.main?.let { main -> this.source.add(main) }
                it.configs.forEach { item ->
                    if (item != it.main) {
                        this.source.add(item)
                    }
                }
            } else {
                this.source.add(it)
            }
        }
        platformConfigParser.parse(JsonParseUtils.getObject(configJson, PLATFORM, null, true), true)?.let {
            if (it is ResourcesSetConfig) {
                it.main?.let { main -> this.platform.add(main) }
                it.configs.forEach { item ->
                    if (item != it.main) {
                        this.platform.add(item)
                    }
                }
            } else {
                this.platform.add(it)
            }
        }

        this.locales = JsonParseUtils.getStrings(configJson, LOCALES, null, true)?.toSet() ?:
                throw InvalidConfigException("\"$LOCALES\" is not set.")

        parseConflictStrategy(JsonParseUtils.getString(configJson, CONFLICT_STRATEGY, null, false))?.let {
            this.conflictStrategy = it
        }

        validate(this)
    }

    @Throws(InvalidConfigException::class)
    private fun validate(config: ConfigBuilder) {
        if (config.locales.isEmpty())
            throw InvalidConfigException("\"$LOCALES\" must contain at least one item.")
    }

    class ConfigArgsParser {

        @Parameter
        private var parameters = ArrayList<String>()

        @Parameter(names = ["--force", "--f"])
        private var forceImport: Boolean = false

        @Parameter(names = ["-cs"])
        private var conflictStrategy: String? = null

        @Parameter(names = ["-delay"])
        private var delay: Long? = null

        @Parameter(names = ["-tempDir"])
        private var tempDir: String? = null

        @Parameter(names = ["-workDir"])
        var workDir: String? = null

        @Throws(InvalidConfigException::class)
        fun applyFor(config: ConfigBuilder) {
            if (forceImport)
                config.forceImport = true

            parseConflictStrategy(conflictStrategy)?.let {
                config.conflictStrategy = it
            }

            delay?.let { config.delay = it }
            tempDir?.let { config.tempDir = it }
        }
    }
}
