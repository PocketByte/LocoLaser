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
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.testutils.mock.MockResourcesConfig
import ru.pocketbyte.locolaser.testutils.mock.MockTableResourcesConfig

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.*

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import ru.pocketbyte.locolaser.resource.Resources

/**
 * @author Denis Shurygin
 */
class BaseTableResourcesConfigParserTest {

    private lateinit var mConfigParser: ConfigParser

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Before
    fun init() {
        val sourceConfigParser = object : BaseTableSourceConfigParser<MockTableResourcesConfig>() {

            @Throws(InvalidConfigException::class)
            override fun sourceByType(type: String?, throwIfWrongType: Boolean): MockTableResourcesConfig {
                return MockTableResourcesConfig()
            }
        }
        val platformConfigParser = object : ResourcesConfigParser<ResourcesConfig> {
            @Throws(InvalidConfigException::class)
            override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): ResourcesConfig {
                return MockResourcesConfig()
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

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testConfigClass() {
        val file = prepareMockFile(null, prepareMinimalSourceMap())
        val configs = mConfigParser.fromFile(file)

        assertEquals(1, configs.size)

        val sourceConfig = configs[0].build().source
        assertTrue(sourceConfig is BaseTableResourcesConfig)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testKeyColumn() {
        val keyColumns = arrayOf("key_1", "key_2")
        for (keyColumn in keyColumns) {
            val sourceMap = prepareMinimalSourceMap()
            sourceMap[BaseTableSourceConfigParser.COLUMN_KEY] = keyColumn
            val file = prepareMockFile(null, sourceMap)
            val configs = mConfigParser.fromFile(file)

            assertEquals(1, configs.size)

            val sourceConfig = configs[0].build().source as BaseTableResourcesConfig?
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

            val sourceConfig = configs[0].build().source as BaseTableResourcesConfig?
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

            val sourceConfig = configs[0].build().source as BaseTableResourcesConfig?
            assertEquals(commentColumn, sourceConfig!!.commentColumn)
        }
    }

    private fun prepareMinimalSourceMap(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        map.put(BaseTableSourceConfigParser.COLUMN_KEY, "key")
        return map
    }

    @Throws(IOException::class)
    private fun prepareMockFile(configMap: Map<String, Any>?, sourceMap: Map<String, Any>?): File {
        val file = tempFolder.newFile()
        val json = if (configMap != null) JSONObject(configMap) else JSONObject()
        json[ConfigParser.PLATFORM] = "mock"

        if (sourceMap != null) {
            val sourceJson = JSONObject(sourceMap)
            sourceJson[BaseTableSourceConfigParser.TYPE] = "mock"
            json[ConfigParser.SOURCE] = sourceJson
        } else {
            json[ConfigParser.SOURCE] = "mock"
        }

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
}
