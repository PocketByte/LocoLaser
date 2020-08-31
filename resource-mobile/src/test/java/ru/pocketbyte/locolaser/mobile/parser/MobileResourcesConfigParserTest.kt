/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.mobile.parser

import org.json.simple.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.mobile.AndroidResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosPlistResourcesConfig
import ru.pocketbyte.locolaser.resource.AbsResources
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author Denis Shurygin
 */
class MobileResourcesConfigParserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var parser: MobileResourcesConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = MobileResourcesConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testAndroidFromString() {
        val config = parser!!.parse(AndroidResourcesConfig.TYPE, true)

        assertNotNull(config)
        assertEquals(AndroidResourcesConfig::class.java, config!!.javaClass)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testIosFromString() {
        val config = parser!!.parse(IosResourcesConfig.TYPE, true)

        assertNotNull(config)
        assertEquals(IosResourcesConfig::class.java, config!!.javaClass)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testIosPlistFromString() {
        val config = parser!!.parse(IosPlistResourcesConfig.TYPE, true)

        assertNotNull(config)
        assertEquals(IosPlistResourcesConfig::class.java, config!!.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testUnknownFromString() {
        parser!!.parse("invalid_platform", true)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJson() {
        val config = parser!!.parse(prepareTestPlatformJson(AndroidResourcesConfig.TYPE), true)

        assertNotNull(config)
        assertEquals(AndroidResourcesConfig::class.java, config!!.javaClass)

        assertNull(config.filter)
        assertEquals("test_res", config.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir!!.canonicalPath)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromJsonNoType() {
        val json = prepareTestPlatformJson(AndroidResourcesConfig.TYPE)
        json.remove(ResourcesConfigParser.RESOURCE_TYPE)
        parser!!.parse(json, true)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromJsonOnlyType() {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = AndroidResourcesConfig.TYPE
        val config = parser!!.parse(json, true)

        assertNotNull(config)
        assertEquals(AndroidResourcesConfig::class.java, config!!.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser!!.parse(ArrayList<String>(), true)
    }

    @Test
    fun testConfigResources() {
        testPlatformConfigResource(AndroidResourcesConfig.TYPE)
        testPlatformConfigResource(IosResourcesConfig.TYPE)
        testPlatformConfigResource(IosPlistResourcesConfig.TYPE)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFilter() {
        val json = prepareTestPlatformJson(AndroidResourcesConfig.TYPE).apply {
            this[BaseMobileResourcesConfigParser.FILTER] = "test_filter"
        }

        val config = parser!!.parse(json, true)

        assertNotNull(config)
        assertEquals(AndroidResourcesConfig::class.java, config!!.javaClass)

        assertNotNull(config.filter)
    }

    private fun testPlatformConfigResource(platform: String) {
        val json = prepareTestPlatformJson(platform)
        val resources = (parser?.parse(json, true) as BaseResourcesConfig).resources as AbsResources

        assertEquals("test_res", resources.name)
        assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    private fun prepareTestPlatformJson(platform: String): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = platform
        json[BaseMobileResourcesConfigParser.RESOURCE_NAME] = "test_res"
        json[BaseMobileResourcesConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}
