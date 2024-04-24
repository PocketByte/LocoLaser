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
            override fun parse(
                resourceObject: Any?,
                workDir: File?,
                throwIfWrongType: Boolean
            ): ResourcesConfig {
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
        val map1 = HashMap<String, Any>()
        map1[ConfigParser.FORCE_IMPORT] = true

        val map2 = HashMap<String, Any>()
        map2[ConfigParser.FORCE_IMPORT] = false

        val file = prepareMockFileWithArray(map1, map2)

        val configs = mConfigParser.fromFile(file)

        assertEquals(2, configs.size)

        assertNotNull(configs[0])
        assertNotNull(configs[1])

        assertEquals(true, configs[0].forceImport)
        assertEquals(false, configs[1].forceImport)

    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testDefaultValues() {
        val file = prepareMockFile(null)
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)

        val config = configs[0]

        assertNotNull(config)
        assertEquals(config.file?.canonicalPath, file.canonicalPath)
        assertFalse(config.forceImport)
        assertFalse(config.duplicateComments)
        assertEquals(file.parentFile.canonicalPath, config.workDir?.canonicalPath)
        assertEquals(KEEP_NEW_PLATFORM, config.conflictStrategy)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonForceImport() {
        val map = HashMap<String, Any>()
        map[ConfigParser.FORCE_IMPORT] = true

        var file = prepareMockFile(map)
        var configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertTrue(configs[0].forceImport)

        map[ConfigParser.FORCE_IMPORT] = false

        file = prepareMockFile(map)
        configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertFalse(configs[0].forceImport)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonDuplicateComments() {
        val map = HashMap<String, Any>()
        map[ConfigParser.DUPLICATE_COMMENTS] = true

        var file = prepareMockFile(map)
        var configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertTrue(configs[0].duplicateComments)

        map[ConfigParser.DUPLICATE_COMMENTS] = false

        file = prepareMockFile(map)
        configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertFalse(configs[0].duplicateComments)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testJsonWorkDir() {
        val newWorkDir = "./new/work/dir"
        val map = HashMap<String, Any>()
        map[ConfigParser.WORK_DIR] = newWorkDir

        val file = prepareMockFile(map)
        val configs = mConfigParser.fromFile(file)

        assertEquals(File(file.parentFile, newWorkDir).canonicalPath, configs[0].workDir?.canonicalPath)
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
        assertEquals(tempDir, configs[0].tempDir)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testDefaultTempDir() {
        val file = prepareMockFile(HashMap())
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertNull(configs[0].tempDir)
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
        val map = HashMap<String, Any>()
        map[ConfigParser.FORCE_IMPORT] = true

        val file = prepareMockFile(map)
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertEquals(true, configs[0].forceImport)
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

        var configs = mConfigParser.fromArguments(arrayOf(file.absolutePath, "--force"))
        assertEquals(1, configs.size)
        assertTrue(configs[0].forceImport)

        configs = mConfigParser.fromArguments(arrayOf(file.absolutePath, "--f"))
        assertEquals(1, configs.size)
        assertTrue(configs[0].forceImport)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentConflictStrategy() {
        val map = HashMap<String, Any>()
        map[ConfigParser.CONFLICT_STRATEGY] = KEEP_NEW_PLATFORM.strValue

        val file = prepareMockFile(map)

        var configs = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "-cs", EXPORT_NEW_PLATFORM.strValue))
        assertEquals(1, configs.size)
        assertEquals(EXPORT_NEW_PLATFORM, configs[0].conflictStrategy)

        configs = mConfigParser.fromArguments(arrayOf(file.absolutePath, "-cs ", REMOVE_PLATFORM.strValue))
        assertEquals(1, configs.size)
        assertEquals(REMOVE_PLATFORM, configs[0].conflictStrategy)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentTempDir() {
        val tempDir = "./some_dir/another/temp/"

        val file = prepareMockFile(null)

        val configs = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "-tempDir", tempDir))

        assertEquals(tempDir, configs[0].tempDir)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testArgumentWorkDir() {
        val workDir = "./subDir"

        val file = prepareMockFile(null)

        val configs = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "-workDir", workDir))

        assertEquals(
            File(file.parentFile, workDir).canonicalPath,
            configs[0].workDir?.canonicalPath
        )
    }

    @Test
    fun someTest() {
        val workDir = File("./newFolder")
        println("workDir=${workDir.canonicalPath}")
        System.setProperty("user.dir", workDir.canonicalPath)

        val tempDir = File("./temp")
        println("tempDir=${tempDir.canonicalPath}")
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testManyArguments() {
        val file = prepareMockFile(null)

        val configs = mConfigParser.fromArguments(
                arrayOf(file.absolutePath, "--force", "-cs", EXPORT_NEW_PLATFORM.strValue))

        assertEquals(1, configs.size)

        val config = configs[0]
        assertTrue(config.forceImport)
        assertEquals(EXPORT_NEW_PLATFORM, config.conflictStrategy)
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
