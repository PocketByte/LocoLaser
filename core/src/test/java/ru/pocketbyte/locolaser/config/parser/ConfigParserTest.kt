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
import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig
import ru.pocketbyte.locolaser.testutils.mock.MockTableSourceConfig

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.Arrays
import java.util.HashMap

import org.junit.Assert.*
import ru.pocketbyte.locolaser.config.Config.ConflictStrategy.*


/**
 * @author Denis Shurygin
 */
class ConfigParserTest {

    private var mConfigParser: ConfigParser? = null

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Before
    fun init() {
        val sourceConfigParser = object : SourceConfigParser<MockTableSourceConfig> {

            @Throws(InvalidConfigException::class)
            override fun parse(sourceObject: Any, throwIfWrongType: Boolean): MockTableSourceConfig {
                return MockTableSourceConfig()
            }
        }
        val platformConfigParser = object : PlatformConfigParser<PlatformConfig> {
            @Throws(InvalidConfigException::class)
            override fun parse(platformObject: Any, throwIfWrongType: Boolean): PlatformConfig {
                return MockPlatformConfig()
            }
        }
        mConfigParser = ConfigParser(sourceConfigParser, platformConfigParser)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testNoSource() {
        val file = prepareMockFileNoSource()
        mConfigParser!!.fromFile(file)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testNoPlatform() {
        val file = prepareMockFileNoPlatform()
        mConfigParser!!.fromFile(file)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testConfigsArray() {
        val delay1: Long = 141
        val map1 = HashMap<String, Any>()
        map1.put(ConfigParser.DELAY, delay1)

        val delay2: Long = 873
        val map2 = HashMap<String, Any>()
        map2.put(ConfigParser.DELAY, delay2)

        val file = prepareMockFileWithArray(map1, map2)

        val configs = mConfigParser!!.fromFile(file)

        assertEquals(2, configs.size.toLong())

        assertNotNull(configs[0])
        assertNotNull(configs[1])

        assertEquals(delay1 * ConfigParser.DELAY_MULT, configs[0].delay)
        assertEquals(delay2 * ConfigParser.DELAY_MULT, configs[1].delay)

    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testDefaultValues() {
        val file = prepareMockFile(null)
        val configs = mConfigParser!!.fromFile(file)

        assertEquals(1, configs.size.toLong())

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
        map.put(ConfigParser.FORCE_IMPORT, true)

        var file = prepareMockFile(map)
        var configs: List<Config> = mConfigParser!!.fromFile(file)

        assertEquals(1, configs.size.toLong())
        assertTrue(configs[0].isForceImport)

        map.put(ConfigParser.FORCE_IMPORT, false)

        file = prepareMockFile(map)
        configs = mConfigParser!!.fromFile(file)

        assertEquals(1, configs.size.toLong())
        assertFalse(configs[0].isForceImport)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonDuplicateComments() {
        val map = HashMap<String, Any>()
        map.put(ConfigParser.DUPLICATE_COMMENTS, true)

        var file = prepareMockFile(map)
        var configs: List<Config> = mConfigParser!!.fromFile(file)

        assertEquals(1, configs.size.toLong())
        assertTrue(configs[0].isDuplicateComments)

        map.put(ConfigParser.DUPLICATE_COMMENTS, false)

        file = prepareMockFile(map)
        configs = mConfigParser!!.fromFile(file)

        assertEquals(1, configs.size.toLong())
        assertFalse(configs[0].isDuplicateComments)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonWorkDir() {
        val newWorkDir = "./new/work/dir"
        val map = HashMap<String, Any>()
        map.put(ConfigParser.WORK_DIR, newWorkDir)

        val file = prepareMockFile(map)
        mConfigParser!!.fromFile(file)

        assertEquals(File(file.parentFile, newWorkDir).canonicalPath, System.getProperty("user.dir"))
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonTempDir() {
        val tempDir = "./new/work/dir"
        val map = HashMap<String, Any>()
        map.put(ConfigParser.TEMP_DIR, tempDir)

        val file = prepareMockFile(map)
        val configs = mConfigParser!!.fromFile(file)

        assertEquals(1, configs.size.toLong())
        assertEquals(File(tempDir).canonicalPath, configs[0].tempDir!!.canonicalPath)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testDefaultTempDir() {
        val file = prepareMockFile(HashMap())
        val configs = mConfigParser!!.fromFile(file)

        val expected = MockPlatformConfig().defaultTempDir

        assertEquals(1, configs.size.toLong())
        assertEquals(expected.canonicalPath, configs[0].tempDir!!.canonicalPath)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonConflictStrategy() {
        val list = Arrays.asList<Pair<String, Config.ConflictStrategy>>(
                Pair(REMOVE_PLATFORM.strValue, REMOVE_PLATFORM),
                Pair(KEEP_NEW_PLATFORM.strValue, KEEP_NEW_PLATFORM),
                Pair(EXPORT_NEW_PLATFORM.strValue, EXPORT_NEW_PLATFORM)
        )

        for ((first, second) in list) {
            val map = HashMap<String, Any>()
            map.put(ConfigParser.CONFLICT_STRATEGY, first)

            val file = prepareMockFile(map)
            val configs = mConfigParser!!.fromFile(file)

            assertEquals(1, configs.size.toLong())
            assertEquals(second, configs[0].conflictStrategy)
        }
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonDelay() {
        val delay: Long = 1
        val map = HashMap<String, Any>()
        map.put(ConfigParser.DELAY, delay)

        val file = prepareMockFile(map)
        val configs = mConfigParser!!.fromFile(file)

        assertEquals(1, configs.size.toLong())
        assertEquals(delay * ConfigParser.DELAY_MULT, configs[0].delay)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testFromArguments() {
        val file = prepareMockFile(null)
        val configs = mConfigParser!!.fromArguments(arrayOf(file.absolutePath))

        assertEquals(1, configs.size.toLong())
        assertNotNull(configs[0])
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testFromArgumentsNoArguments() {
        mConfigParser!!.fromArguments(arrayOf())
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentForce() {
        val map = HashMap<String, Any>()
        map.put(ConfigParser.FORCE_IMPORT, false)

        val file = prepareMockFile(map)

        var configs: List<Config> = mConfigParser!!.fromArguments(arrayOf(file.absolutePath, "--force"))
        assertEquals(1, configs.size.toLong())
        assertTrue(configs[0].isForceImport)

        configs = mConfigParser!!.fromArguments(arrayOf(file.absolutePath, "--f"))
        assertEquals(1, configs.size.toLong())
        assertTrue(configs[0].isForceImport)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentConflictStrategy() {
        val map = HashMap<String, Any>()
        map.put(ConfigParser.CONFLICT_STRATEGY, KEEP_NEW_PLATFORM.strValue)

        val file = prepareMockFile(map)

        var configs: List<Config> = mConfigParser!!.fromArguments(
                arrayOf(file.absolutePath, "-cs", EXPORT_NEW_PLATFORM.strValue))
        assertEquals(1, configs.size.toLong())
        assertEquals(EXPORT_NEW_PLATFORM, configs[0].conflictStrategy)

        configs = mConfigParser!!.fromArguments(arrayOf(file.absolutePath, "-cs ", REMOVE_PLATFORM.strValue))
        assertEquals(1, configs.size.toLong())
        assertEquals(REMOVE_PLATFORM, configs[0].conflictStrategy)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentDelay() {
        val delay: Long = 1

        val file = prepareMockFile(null)

        val configs = mConfigParser!!.fromArguments(
                arrayOf(file.absolutePath, "-delay", java.lang.Long.toString(delay)))
        assertEquals(1, configs.size.toLong())
        assertEquals(delay * ConfigParser.DELAY_MULT, configs[0].delay)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testManyArguments() {
        val delay: Long = 1

        val file = prepareMockFile(null)

        val configs = mConfigParser!!.fromArguments(
                arrayOf(file.absolutePath, "--force", "-cs", EXPORT_NEW_PLATFORM.strValue,
                        "-delay", java.lang.Long.toString(delay)))

        assertEquals(1, configs.size.toLong())

        val config = configs[0]
        assertTrue(config.isForceImport)
        assertEquals(EXPORT_NEW_PLATFORM, config.conflictStrategy)
        assertEquals(delay * ConfigParser.DELAY_MULT, config.delay)
    }

    @Throws(IOException::class)
    private fun prepareMockFile(configMap: Map<String, Any>?): File {
        val file = tempFolder.newFile()
        val json = if (configMap != null) JSONObject(configMap) else JSONObject()
        json.put(ConfigParser.PLATFORM, "mock")
        json.put(ConfigParser.SOURCE, "mock")

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
            json.put(ConfigParser.PLATFORM, "mock")
            json.put(ConfigParser.SOURCE, "mock")
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
        json.put(ConfigParser.PLATFORM, "mock")

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
        json.put(ConfigParser.SOURCE, "mock")

        PrintWriter(file).apply {
            write(json.toJSONString())
            flush()
            close()
        }
        return file
    }
}
