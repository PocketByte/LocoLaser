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
import ru.pocketbyte.locolaser.config.platform.PlatformConfig
import ru.pocketbyte.locolaser.config.source.BaseTableSourceConfig
import ru.pocketbyte.locolaser.config.source.SourceSetConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockPlatformConfig
import ru.pocketbyte.locolaser.testutils.mock.MockTableSourceConfig

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.*

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * @author Denis Shurygin
 */
class BaseTableSourceConfigParserTest {

    private lateinit var mConfigParser: ConfigParser

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Before
    fun init() {
        val sourceConfigParser = object : BaseTableSourceConfigParser<MockTableSourceConfig>() {

            @Throws(InvalidConfigException::class)
            override fun sourceByType(type: String?): MockTableSourceConfig {
                return MockTableSourceConfig()
            }
        }
        val platformConfigParser = object : PlatformConfigParser<PlatformConfig> {
            @Throws(InvalidConfigException::class)
            override fun parse(platformObject: Any?, throwIfWrongType: Boolean): PlatformConfig {
                return MockPlatformConfig()
            }
        }
        mConfigParser = ConfigParser(sourceConfigParser, platformConfigParser)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testMinimalSource() {
        val sourceMap = prepareMinimalSourceMap()
        val file = prepareMockFile(null, sourceMap)
        mConfigParser.fromFile(file)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testNoKeyColumn() {
        val sourceMap = prepareMinimalSourceMap()
        sourceMap.remove(BaseTableSourceConfigParser.COLUMN_KEY)
        val file = prepareMockFile(null, sourceMap)
        mConfigParser.fromFile(file)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testNoLocaleColumns() {
        val sourceMap = prepareMinimalSourceMap()
        sourceMap.remove(BaseTableSourceConfigParser.COLUMN_LOCALES)
        val file = prepareMockFile(null, sourceMap)
        mConfigParser.fromFile(file)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testEmptyLocaleColumns() {
        val sourceMap = prepareMinimalSourceMap()
        sourceMap.put(BaseTableSourceConfigParser.COLUMN_LOCALES, ArrayList<Any>())
        val file = prepareMockFile(null, sourceMap)
        mConfigParser.fromFile(file)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testConfigClass() {
        val file = prepareMockFile(null, prepareMinimalSourceMap())
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)

        val sourceConfig = configs[0].sourceConfig
        assertTrue(sourceConfig is BaseTableSourceConfig)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testKeyColumn() {
        val keyColumns = arrayOf("key_1", "key_2")
        for (keyColumn in keyColumns) {
            val sourceMap = prepareMinimalSourceMap()
            sourceMap.put(BaseTableSourceConfigParser.COLUMN_KEY, keyColumn)
            val file = prepareMockFile(null, sourceMap)
            val configs = mConfigParser.fromFile(file)

            assertEquals(1, configs.size)

            val sourceConfig = configs[0].sourceConfig as BaseTableSourceConfig?
            assertEquals(keyColumn, sourceConfig!!.keyColumn)
        }
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testQuantityColumn() {
        val quantityColumns = arrayOf("quantity_1", "quantity_2")
        for (quantityColumn in quantityColumns) {
            val sourceMap = prepareMinimalSourceMap()
            sourceMap.put(BaseTableSourceConfigParser.COLUMN_QUANTITY, quantityColumn)
            val file = prepareMockFile(null, sourceMap)
            val configs = mConfigParser.fromFile(file)

            assertEquals(1, configs.size)

            val sourceConfig = configs[0].sourceConfig as BaseTableSourceConfig?
            assertEquals(quantityColumn, sourceConfig!!.quantityColumn)
        }
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testCommentColumn() {
        val commentColumns = arrayOf("comment_1", "comment_2")
        for (commentColumn in commentColumns) {
            val sourceMap = prepareMinimalSourceMap()
            sourceMap.put(BaseTableSourceConfigParser.COLUMN_COMMENT, commentColumn)
            val file = prepareMockFile(null, sourceMap)
            val configs = mConfigParser.fromFile(file)

            assertEquals(1, configs.size)

            val sourceConfig = configs[0].sourceConfig as BaseTableSourceConfig?
            assertEquals(commentColumn, sourceConfig!!.commentColumn)
        }
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testLocaleColumns() {
        val input = arrayOf(arrayOf("locale_1", "locale_3"), arrayOf("locale_2"))
        for (localeColumns in input) {
            val sourceMap = prepareMinimalSourceMap()
            sourceMap[BaseTableSourceConfigParser.COLUMN_LOCALES] = listOf(*localeColumns)
            val file = prepareMockFile(null, sourceMap)
            val configs = mConfigParser.fromFile(file)

            assertEquals(1, configs.size)

            val sourceConfig = configs[0].sourceConfig as BaseTableSourceConfig?
            assertEquals(localeColumns.size, sourceConfig?.localeColumns?.size)

            for (i in localeColumns.indices) {
                assertTrue(sourceConfig?.localeColumns?.contains(localeColumns[i]) ?: false)
            }
        }
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testMultiSource() {
        val file = prepareMultiSourceMockFile(null, Arrays.asList<Map<String, Any>>(
                prepareMinimalSourceMap(),
                prepareMinimalSourceMap()))
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)
        assertEquals(SourceSetConfig::class.java, configs[0].sourceConfig!!.javaClass)
    }

    private fun prepareMinimalSourceMap(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        map.put(BaseTableSourceConfigParser.COLUMN_KEY, "key")
        map.put(BaseTableSourceConfigParser.COLUMN_LOCALES, listOf("en"))
        return map
    }

    @Throws(IOException::class)
    private fun prepareMockFile(configMap: Map<String, Any>?, sourceMap: Map<String, Any>?): File {
        val file = tempFolder.newFile()
        val json = if (configMap != null) JSONObject(configMap) else JSONObject()
        json.put(ConfigParser.PLATFORM, "mock")

        if (sourceMap != null) {
            val sourceJson = JSONObject(sourceMap)
            sourceJson.put(BaseTableSourceConfigParser.TYPE, "mock")
            json.put(ConfigParser.SOURCE, sourceJson)
        } else {
            json.put(ConfigParser.SOURCE, "mock")
        }

        val writer = PrintWriter(file)
        writer.write(json.toJSONString())
        writer.flush()
        writer.close()
        return file
    }

    @Throws(IOException::class)
    private fun prepareMultiSourceMockFile(configMap: Map<String, Any>?, sourceMaps: List<Map<String, Any>>?): File {
        val file = tempFolder.newFile()
        val json = if (configMap != null) JSONObject(configMap) else JSONObject()
        json.put(ConfigParser.PLATFORM, "mock")

        if (sourceMaps != null) {
            val sourceArray = JSONArray()
            for (sourceMap in sourceMaps) {
                val sourceJson = JSONObject(sourceMap)
                sourceJson.put(BaseTableSourceConfigParser.TYPE, "mock")
                sourceArray.add(sourceJson)
            }
            json.put(ConfigParser.SOURCE, sourceArray)
        } else {
            json.put(ConfigParser.SOURCE, "mock")
        }

        val writer = PrintWriter(file)
        writer.write(json.toJSONString())
        writer.flush()
        writer.close()
        return file
    }
}
