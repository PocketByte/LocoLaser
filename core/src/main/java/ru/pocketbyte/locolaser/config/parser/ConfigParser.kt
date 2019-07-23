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
import ru.pocketbyte.locolaser.utils.JsonParseUtils

import com.beust.jcommander.Parameter
import ru.pocketbyte.locolaser.utils.LogUtils

import java.io.File
import java.io.FileReader
import java.io.IOException
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
    private val sourceConfigParser: SourceConfigParser<*>,
    private val platformConfigParser: PlatformConfigParser<*>
) {

    companion object {

        const val SOURCE = "source"
        const val PLATFORM = "platform"
        const val WORK_DIR = "work_dir"
        const val TEMP_DIR = "temp_dir"

        const val FORCE_IMPORT = "force_import"
        const val CONFLICT_STRATEGY = "conflict_strategy"
        const val DUPLICATE_COMMENTS = "duplicate_comments"
        const val DELAY = "delay"

        /**
         * Config delay defined in minutes but code use time in milliseconds.
         * So delay value from config must multiplied by this value.
         */
        internal const val DELAY_MULT: Long = 60000

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
            val configs = fromFile(File(args[0]))

            val argsParser = ConfigArgsParser()
            JCommander(argsParser, *args)

            for (config in configs)
                argsParser.applyFor(config)

            return configs
        }
        throw InvalidConfigException("JSON config not defined! Please provide file path for JSON config as a first parameter.")
    }

    /**
     * Parse Config from file.
     * @param file File with JSON config.
     * @return Parsed config.
     * @throws InvalidConfigException if config has some logic errors or doesn't contain some required fields.
     * @throws IOException if occurs an error reading the file.
     * @throws ParseException if occurs an error parsing JSON.
     */
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun fromFile(file: File): List<Config> {
        var file = file
        LogUtils.info("Reading config file " + file.canonicalPath)
        file = File(file.canonicalPath)
        System.setProperty("user.dir", file.parentFile.canonicalPath)

        val reader = FileReader(file)
        val json = JsonParseUtils.JSON_PARSER.parse(reader)
        reader.close()

        if (json is JSONObject)
            return listOf(fromJsonObject(file, json))
        else if (json is JSONArray) {
            return json.indices.map { index ->
                fromJsonObject(file, json[index] as JSONObject)
            }
        }

        throw InvalidConfigException("Config file must contain JSONObject or JSONArray.")
    }

    @Throws(IOException::class, InvalidConfigException::class)
    private fun fromJsonObject(file: File, configJson: JSONObject): Config {

        val workDir = JsonParseUtils.getString(configJson, WORK_DIR, null, false)
        if (workDir != null)
            System.setProperty("user.dir", File(workDir).canonicalPath)

        val config = Config()
        config.file = file
        config.isForceImport = JsonParseUtils.getBoolean(configJson, FORCE_IMPORT, null, false)
        config.conflictStrategy = parseConflictStrategy(JsonParseUtils.getString(configJson, CONFLICT_STRATEGY, null, false))
        config.isDuplicateComments = JsonParseUtils.getBoolean(configJson, DUPLICATE_COMMENTS, null, false)
        config.delay = JsonParseUtils.getLong(configJson, DELAY, null, false) * DELAY_MULT
        config.tempDir = JsonParseUtils.getFile(configJson, TEMP_DIR, null, false)

        config.sourceConfig = sourceConfigParser.parse(JsonParseUtils.getObject(configJson, SOURCE, null, true), true)
        config.platform = platformConfigParser.parse(JsonParseUtils.getObject(configJson, PLATFORM, null, true), true)

        validate(config)

        return config
    }

    @Throws(InvalidConfigException::class)
    private fun validate(config: Config) {
        if (config.platform == null)
            throw InvalidConfigException("\"$PLATFORM\" is not set.")
        if (config.sourceConfig == null)
            throw InvalidConfigException("\"$SOURCE\" is not set.")
    }

    class ConfigArgsParser {

        @Parameter
        private val parameters = ArrayList<String>()

        @Parameter(names = ["--force", "--f"])
        private val forceImport: Boolean = false

        @Parameter(names = ["-cs"])
        private val conflictStrategy: String? = null

        @Parameter(names = ["-delay"])
        private val delay: Long? = null

        @Parameter(names = ["-tempDir"])
        private val tempDir: String? = null

        @Throws(InvalidConfigException::class)
        fun applyFor(config: Config) {
            if (forceImport)
                config.isForceImport = true

            if (conflictStrategy != null)
                config.conflictStrategy = parseConflictStrategy(conflictStrategy)

            if (delay != null)
                config.delay = delay * DELAY_MULT

            if (tempDir != null)
                config.tempDir = File(File(tempDir).canonicalPath)
        }
    }
}
