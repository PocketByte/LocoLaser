/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.google.parser

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.ParseException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.BaseTableSourceConfigParser
import ru.pocketbyte.locolaser.config.parser.ConfigParser
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.resources.ResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.google.sheet.GoogleSheetConfig
import ru.pocketbyte.locolaser.testutils.mock.MockResourcesConfig
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.*

/**
 * @author Denis Shurygin
 */
class GoogleSheetConfigParserTest {

    private var mConfigParser: ConfigParser? = null

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    @Before
    fun init() {
        val sourceConfigParser = GoogleSheetConfigParser()

        val platformConfigParser = object : ResourcesConfigParser<ResourcesConfig> {
            override fun parse(resourceObject: Any?, throwIfWrongType: Boolean): ResourcesConfig? {
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
        mConfigParser!!.fromFile(file)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testWrongType() {
        val sourceMap = prepareMinimalSourceMap()
        sourceMap[BaseTableSourceConfigParser.TYPE] = "wrong"
        val file = prepareMockFile(null, sourceMap)
        mConfigParser!!.fromFile(file)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testNoSheetId() {
        val sourceMap = prepareMinimalSourceMap()
        sourceMap.remove(GoogleSheetConfigParser.SHEET_ID)
        val file = prepareMockFile(null, sourceMap)
        mConfigParser!!.fromFile(file)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testConfigClass() {
        val file = prepareMockFile(null, prepareMinimalSourceMap())
        val config = mConfigParser!!.fromFile(file)

        assertEquals(1, config.size)

        val sourceConfig = config[0].source.build()
        assertTrue(sourceConfig is GoogleSheetConfig)
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testSheetId() {
        val values = arrayOf("id_1", "id_2")
        for (expectedValue in values) {
            val sourceMap = prepareMinimalSourceMap()
            sourceMap[GoogleSheetConfigParser.SHEET_ID] = expectedValue
            val file = prepareMockFile(null, sourceMap)
            val config = mConfigParser!!.fromFile(file)

            assertEquals(1, config.size)

            val sourceConfig = config[0].source.build() as GoogleSheetConfig
            assertEquals(expectedValue, sourceConfig.id)
        }
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testWorkSheet() {
        val values = arrayOf("worksheet_1", "worksheet_2")
        for (expectedValue in values) {
            val sourceMap = prepareMinimalSourceMap()
            sourceMap[GoogleSheetConfigParser.SHEET_WORKSHEET_TITLE] = expectedValue
            val file = prepareMockFile(null, sourceMap)
            val config = mConfigParser!!.fromFile(file)

            assertEquals(1, config.size)

            val sourceConfig = config[0].source.build() as GoogleSheetConfig
            assertEquals(expectedValue, sourceConfig.worksheetTitle)
        }
    }

    @Test
    @Throws(IOException::class, ParseException::class, InvalidConfigException::class)
    fun testCredentialFile() {
        val values = arrayOf("file_1.txt", "file_2.json")
        for (expectedValue in values) {
            val sourceMap = prepareMinimalSourceMap()
            sourceMap[GoogleSheetConfigParser.SHEET_CREDENTIAL_FILE] = expectedValue
            val file = prepareMockFile(null, sourceMap)
            val config = mConfigParser!!.fromFile(file)

            assertEquals(1, config.size)

            val sourceConfig = config[0].source.build() as GoogleSheetConfig
            assertEquals(expectedValue, sourceConfig.credentialFile)
        }
    }

    private fun prepareMinimalSourceMap(): MutableMap<String, Any> {
        val map = HashMap<String, Any>()
        map[BaseTableSourceConfigParser.TYPE] = "googlesheet"
        map[BaseTableSourceConfigParser.COLUMN_KEY] = "key"
        map[GoogleSheetConfigParser.SHEET_ID] = "sheet_id"
        return map
    }

    @Throws(IOException::class)
    private fun prepareMockFile(configMap: Map<String, Any>?, sourceMap: Map<String, Any>?): File {
        val file = tempFolder.newFile()
        val json = configMap?.let { JSONObject(it) } ?: JSONObject()
        json[ConfigParser.PLATFORM] = "mock"

        if (sourceMap != null)
            json[ConfigParser.SOURCE] = JSONObject(sourceMap)
        else
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

}
