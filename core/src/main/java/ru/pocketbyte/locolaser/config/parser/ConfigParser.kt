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
import ru.pocketbyte.locolaser.utils.LogUtils
import java.io.*

import java.util.ArrayList

/**
 * Helper class that parse Config from JSON file or string.
 *
 * @author Denis Shurygin
 */
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
    fun fromArguments(args: Array<String>?): List<Config> {
        if (args != null && args.isNotEmpty()) {

            val argsParser = ConfigArgsParser()
            JCommander(argsParser, *args)

            val file = File(File(args[0]).canonicalPath)
            val workDir = argsParser.workDir?.let { File(File(it).canonicalPath) }
            val configs = fromFile(file, workDir)

            if (workDir == null)
                System.setProperty("user.dir", file.parentFile.canonicalPath)
            else
                System.setProperty("user.dir", workDir.canonicalPath)

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
    fun fromFile(file: File, workDir: File? = null): List<Config> {
        LogUtils.info("Reading config file " + file.canonicalPath)

        @Suppress("NAME_SHADOWING")
        val file = File(file.canonicalPath)

        if (workDir != null)
            System.setProperty("user.dir", workDir.canonicalPath)

        val reader = BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8"))
        val json = JsonParseUtils.JSON_PARSER.parse(reader)
        reader.close()

        if (json is JSONObject) {
            if (workDir == null) JsonParseUtils.getString(json, WORK_DIR)?.let {
                System.setProperty("user.dir", File(file.parentFile, it).canonicalPath)
            } ?: {
                System.setProperty("user.dir", file.parentFile.canonicalPath)
            }()
            return listOf(fromJsonObject(file, json))
        }
        else if (json is JSONArray) {
            return json.indices.map { index ->
                val jsonItem = json[index] as JSONObject

                if (workDir == null) JsonParseUtils.getString(jsonItem, WORK_DIR)?.let {
                    System.setProperty("user.dir", File(file.parentFile, it).canonicalPath)
                } ?: {
                    System.setProperty("user.dir", file.parentFile.canonicalPath)
                }()

                fromJsonObject(file, jsonItem)
            }
        }

        throw InvalidConfigException("Config file must contain JSONObject or JSONArray.")
    }

    @Throws(IOException::class, InvalidConfigException::class)
    private fun fromJsonObject(file: File, configJson: JSONObject): Config {
        val config = Config()
        config.file = file

        JsonParseUtils.getBoolean(configJson, FORCE_IMPORT, null, false)?.let {
            config.isForceImport = it
        }
        JsonParseUtils.getBoolean(configJson, DUPLICATE_COMMENTS, null, false)?.let {
            config.isDuplicateComments = it
        }
        JsonParseUtils.getBoolean(configJson, TRIM_UNSUPPORTED_QUANTITIES, null, false)?.let {
            config.trimUnsupportedQuantities = it
        }
        JsonParseUtils.getLong(configJson, DELAY, null, false)?.let {
            config.delay = it
        }
        config.tempDir = JsonParseUtils.getFile(configJson, TEMP_DIR, null, false)

        config.source = sourceConfigParser.parse(JsonParseUtils.getObject(configJson, SOURCE, null, true), true)
        config.platform = platformConfigParser.parse(JsonParseUtils.getObject(configJson, PLATFORM, null, true), true)

        config.locales = JsonParseUtils.getStrings(configJson, LOCALES, null, true)?.toSet() ?:
                throw InvalidConfigException("\"$LOCALES\" is not set.")

        parseConflictStrategy(JsonParseUtils.getString(configJson, CONFLICT_STRATEGY, null, false))?.let {
            config.conflictStrategy = it
        }

        validate(config)

        return config
    }

    @Throws(InvalidConfigException::class)
    private fun validate(config: Config) {
        if (config.platform == null)
            throw InvalidConfigException("\"$PLATFORM\" is not set.")
        if (config.source == null)
            throw InvalidConfigException("\"$SOURCE\" is not set.")
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
        fun applyFor(config: Config) {
            if (forceImport)
                config.isForceImport = true

            parseConflictStrategy(conflictStrategy)?.let {
                config.conflictStrategy = it
            }

            delay?.let { config.delay = it }
            tempDir?.let { config.tempDir = File(File(it).canonicalPath) }
        }
    }
}
