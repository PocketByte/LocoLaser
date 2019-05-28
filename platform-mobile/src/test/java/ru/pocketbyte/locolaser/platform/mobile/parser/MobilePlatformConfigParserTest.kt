/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.platform.mobile.parser

import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.config.platform.BasePlatformConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.mobile.AndroidPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.IosPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.IosPlistPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.IosSwiftPlatformConfig
import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author Denis Shurygin
 */
class MobilePlatformConfigParserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var parser: MobilePlatformConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = MobilePlatformConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testAndroidFromString() {
        val config = parser!!.parse(AndroidPlatformConfig.TYPE, true)

        assertNotNull(config)
        assertEquals(AndroidPlatformConfig::class.java, config!!.javaClass)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testIosFromString() {
        val config = parser!!.parse(IosPlatformConfig.TYPE, true)

        assertNotNull(config)
        assertEquals(IosPlatformConfig::class.java, config!!.javaClass)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testIosPlistFromString() {
        val config = parser!!.parse(IosPlistPlatformConfig.TYPE, true)

        assertNotNull(config)
        assertEquals(IosPlistPlatformConfig::class.java, config!!.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testUnknownFromString() {
        parser!!.parse("invalid_platform", true)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJson() {
        val config = parser!!.parse(prepareTestPlatformJson(AndroidPlatformConfig.TYPE), true)

        assertNotNull(config)
        assertEquals(AndroidPlatformConfig::class.java, config!!.javaClass)

        assertEquals("test_res", config.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir!!.canonicalPath)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromJsonNoType() {
        val json = prepareTestPlatformJson(AndroidPlatformConfig.TYPE)
        json.remove(PlatformConfigParser.PLATFORM_TYPE)
        parser!!.parse(json, true)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromJsonOnlyType() {
        val json = JSONObject()
        json[PlatformConfigParser.PLATFORM_TYPE] = AndroidPlatformConfig.TYPE
        val config = parser!!.parse(json, true)

        assertNotNull(config)
        assertEquals(AndroidPlatformConfig::class.java, config!!.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser!!.parse(ArrayList<String>(), true)
    }

    @Test
    fun testConfigResources() {
        testPlatformConfigResource(AndroidPlatformConfig.TYPE)
        testPlatformConfigResource(IosPlatformConfig.TYPE)
        testPlatformConfigResource(IosPlistPlatformConfig.TYPE)
    }

    private fun testPlatformConfigResource(platform: String) {
        val json = prepareTestPlatformJson(platform)
        val resources = (parser?.parse(json, true) as BasePlatformConfig).resources as AbsPlatformResources

        assertEquals("test_res", resources.name)
        assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    private fun prepareTestPlatformJson(platform: String): JSONObject {
        val json = JSONObject()
        json[PlatformConfigParser.PLATFORM_TYPE] = platform
        json[BaseMobilePlatformConfigParser.RESOURCE_NAME] = "test_res"
        json[BaseMobilePlatformConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}
