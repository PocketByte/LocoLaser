/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config.parser

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.ParseException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockResourcesConfig
import ru.pocketbyte.locolaser.testutils.mock.MockTableResourcesConfig

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.HashMap

import org.junit.Assert.*
import ru.pocketbyte.locolaser.config.Config.ConflictStrategy.*
import ru.pocketbyte.locolaser.resource.Resources


/**
 * @author Denis Shurygin
 */
class ConfigParserTest {

    private lateinit var mConfigParser: ConfigParser

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Before
    fun init() {
        val mockParser = object : ResourcesConfigParser<ResourcesConfig> {
            @Throws(InvalidConfigException::class)
            override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): ResourcesConfig {
                return MockResourcesConfig()
            }
        }
        mConfigParser = ConfigParser(mockParser, mockParser)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testNoSource() {
        val file = prepareMockFileNoSource()
        mConfigParser.fromFile(file)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testNoPlatform() {
        val file = prepareMockFileNoPlatform()
        mConfigParser.fromFile(file)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testConfigsArray() {
        val delay1: Long = 141
        val map1 = HashMap<String, Any>()
        map1[ConfigParser.DELAY] = delay1

        val delay2: Long = 873
        val map2 = HashMap<String, Any>()
        map2[ConfigParser.DELAY] = delay2

        val file = prepareMockFileWithArray(map1, map2)

        val configs = mConfigParser.fromFile(file)

        assertEquals(2, configs.size)

        assertNotNull(configs[0])
        assertNotNull(configs[1])

        assertEquals(delay1 * ConfigParser.DELAY_MULT, configs[0].delay)
        assertEquals(delay2 * ConfigParser.DELAY_MULT, configs[1].delay)

    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testDefaultValues() {
        val file = prepareMockFile(null)
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)

        val config = configs[0]

        assertNotNull(config)
        assertEquals(config.file!!.canonicalPath, file.canonicalPath)
        assertFalse(config.isForceImport)
        assertFalse(config.isDuplicateComments)
        assertEquals(file.parentFile.canonicalPath, System.getProperty("user.dir"))
        assertEquals(KEEP_NEW_PLATFORM, config.conflictStrategy)
        assertEquals(0, config.delay)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonForceImport() {
        val map = HashMap<String, Any>()
        map[ConfigParser.FORCE_IMPORT] = true

        var file = prepareMockFile(map)
        var configs: List<Config> = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertTrue(configs[0].isForceImport)

        map[ConfigParser.FORCE_IMPORT] = false

        file = prepareMockFile(map)
        configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertFalse(configs[0].isForceImport)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonDuplicateComments() {
        val map = HashMap<String, Any>()
        map[ConfigParser.DUPLICATE_COMMENTS] = true

        var file = prepareMockFile(map)
        var configs: List<Config> = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertTrue(configs[0].isDuplicateComments)

        map[ConfigParser.DUPLICATE_COMMENTS] = false

        file = prepareMockFile(map)
        configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertFalse(configs[0].isDuplicateComments)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonWorkDir() {
        val newWorkDir = "./new/work/dir"
        val map = HashMap<String, Any>()
        map[ConfigParser.WORK_DIR] = newWorkDir

        val file = prepareMockFile(map)
        mConfigParser.fromFile(file)

        assertEquals(File(file.parentFile, newWorkDir).canonicalPath, System.getProperty("user.dir"))
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonTempDir() {
        val tempDir = "./new/work/dir"
        val map = HashMap<String, Any>()
        map[ConfigParser.TEMP_DIR] = tempDir

        val file = prepareMockFile(map)
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertEquals(File(tempDir).canonicalPath, configs[0].tempDir!!.canonicalPath)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testDefaultTempDir() {
        val file = prepareMockFile(HashMap())
        val configs = mConfigParser.fromFile(file)

        val expected = MockResourcesConfig().defaultTempDir

        assertEquals(1, configs.size)
        assertEquals(expected.canonicalPath, configs[0].tempDir!!.canonicalPath)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonConflictStrategy() {
        val list = listOf(
                Pair(REMOVE_PLATFORM.strValue, REMOVE_PLATFORM),
                Pair(KEEP_NEW_PLATFORM.strValue, KEEP_NEW_PLATFORM),
                Pair(EXPORT_NEW_PLATFORM.strValue, EXPORT_NEW_PLATFORM)
        )

        for ((first, second) in list) {
            val map = HashMap<String, Any>()
            map[ConfigParser.CONFLICT_STRATEGY] = first

            val file = prepareMockFile(map)
            val configs = mConfigParser.fromFile(file)

            assertEquals(1, configs.size)
            assertEquals(second, configs[0].conflictStrategy)
        }
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonDelay() {
        val delay: Long = 1
        val map = HashMap<String, Any>()
        map[ConfigParser.DELAY] = delay

        val file = prepareMockFile(map)
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertEquals(delay * ConfigParser.DELAY_MULT, configs[0].delay)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testFromArguments() {
        val file = prepareMockFile(null)
        val configs = mConfigParser.fromArguments(arrayOf(file.absolutePath))

        assertEquals(1, configs.size)
        assertNotNull(configs[0])
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testFromArgumentsNoArguments() {
        mConfigParser.fromArguments(arrayOf())
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentForce() {
        val map = HashMap<String, Any>()
        map[ConfigParser.FORCE_IMPORT] = false

        val file = prepareMockFile(map)

        var configs: List<Config> = mConfigParser.fromArguments(arrayOf(file.absolutePath, "--force"))
        assertEquals(1, configs.size)
        assertTrue(configs[0].isForceImport)

        configs = mConfigParser.fromArguments(arrayOf(file.absolutePath, "--f"))
        assertEquals(1, configs.size)
        assertTrue(configs[0].isForceImport)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentConflictStrategy() {
        val map = HashMap<String, Any>()
        map[ConfigParser.CONFLICT_STRATEGY] = KEEP_NEW_PLATFORM.strValue

        val file = prepareMockFile(map)

        var configs: List<Config> = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "-cs", EXPORT_NEW_PLATFORM.strValue))
        assertEquals(1, configs.size)
        assertEquals(EXPORT_NEW_PLATFORM, configs[0].conflictStrategy)

        configs = mConfigParser.fromArguments(arrayOf(file.absolutePath, "-cs ", REMOVE_PLATFORM.strValue))
        assertEquals(1, configs.size)
        assertEquals(REMOVE_PLATFORM, configs[0].conflictStrategy)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentDelay() {
        val delay: Long = 1

        val file = prepareMockFile(null)

        val configs = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "-delay", delay.toString()))
        assertEquals(1, configs.size)
        assertEquals(delay * ConfigParser.DELAY_MULT, configs[0].delay)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentTempDir() {
        val tempDir = "./some_dir/another/temp/"

        val file = prepareMockFile(null)

        val configs = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "-tempDir", tempDir))

        assertEquals(File(tempDir).canonicalPath, configs[0].tempDir?.canonicalPath)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentWorkDir() {
        val workDir = "./subDir"
        val expectedTemp = File(workDir, MockResourcesConfig().defaultTempDir.path).canonicalPath

        val file = prepareMockFile(null)

        val configs = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "-workDir", workDir))

        assertEquals(expectedTemp, configs[0].tempDir?.canonicalPath)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testManyArguments() {
        val delay: Long = 1

        val file = prepareMockFile(null)

        val configs = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "--force", "-cs", EXPORT_NEW_PLATFORM.strValue,
                        "-delay", delay.toString()))

        assertEquals(1, configs.size)

        val config = configs[0]
        assertTrue(config.isForceImport)
        assertEquals(EXPORT_NEW_PLATFORM, config.conflictStrategy)
        assertEquals(delay * ConfigParser.DELAY_MULT, config.delay)
    }


    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testNoLocaleColumns() {
        val file = prepareMockFile(mapOf(
            Pair(ConfigParser.LOCALES, null)
        ))
        mConfigParser.fromFile(file)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testEmptyLocaleColumns() {
        val file = prepareMockFile(mapOf(
            Pair(ConfigParser.LOCALES, JSONArray())
        ))
        mConfigParser.fromFile(file)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testLocaleColumns() {
        val input = arrayOf(arrayOf("locale_1", "locale_3"), arrayOf("locale_2"))
        for (localeColumns in input) {
            val file = prepareMockFile(mapOf(
                Pair(ConfigParser.LOCALES, JSONArray().apply {
                    addAll(localeColumns)
                })
            ))

            val configs = mConfigParser.fromFile(file)

            assertEquals(1, configs.size)
            assertEquals(localeColumns.toSet(), configs[0].locales)
        }
    }

    @Throws(IOException::class)
    private fun prepareMockFile(configMap: Map<String, Any?>?): File {
        val file = tempFolder.newFile()
        val json = if (configMap != null) JSONObject(configMap) else JSONObject()
        json[ConfigParser.PLATFORM] = "mock"
        json[ConfigParser.SOURCE] = "mock"

        if (configMap?.contains(ConfigParser.LOCALES) != true) {
            json[ConfigParser.LOCALES] = JSONArray().apply {
                add(Resources.BASE_LOCALE)
            }
        }

        val writer = PrintWriter(file)
        writer.write(json.toJSONString())
        writer.flush()
        writer.close()
        return file
    }

    @Throws(IOException::class)
    private fun prepareMockFileWithArray(vararg configMaps: Map<String, Any>): File {
        val file = tempFolder.newFile()

        val jsonArray = JSONArray()

        for (map in configMaps) {
            val json = JSONObject(map)
            json[ConfigParser.PLATFORM] = "mock"
            json[ConfigParser.SOURCE] = "mock"

            if (!map.contains(ConfigParser.LOCALES)) {
                json[ConfigParser.LOCALES] = JSONArray().apply {
                    add(Resources.BASE_LOCALE)
                }
            }

            jsonArray.add(json)
        }

        PrintWriter(file).apply {
            write(jsonArray.toJSONString())
            flush()
            close()
        }
        return file
    }

    @Throws(IOException::class)
    private fun prepareMockFileNoSource(): File {
        val file = tempFolder.newFile()
        val json = JSONObject()
        json[ConfigParser.PLATFORM] = "mock"

        PrintWriter(file).apply {
            write(json.toJSONString())
            flush()
            close()
        }
        return file
    }

    @Throws(IOException::class)
    private fun prepareMockFileNoPlatform(): File {
        val file = tempFolder.newFile()
        val json = JSONObject()
        json[ConfigParser.SOURCE] = "mock"

        PrintWriter(file).apply {
            write(json.toJSONString())
            flush()
            close()
        }
        return file
    }
}
