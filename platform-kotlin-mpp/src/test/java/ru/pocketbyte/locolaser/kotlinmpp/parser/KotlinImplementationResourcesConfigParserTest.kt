package ru.pocketbyte.locolaser.kotlinmpp.parser

import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.mobile.parser.BaseMobileResourcesConfigParser
import ru.pocketbyte.locolaser.resource.AbsResources
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import java.io.File
import java.io.IOException

class KotlinImplementationResourcesConfigParserTest {

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    private var parser: KotlinImplementationResourcesConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = KotlinImplementationResourcesConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    fun testConfigResources() {
        testPlatformConfigResource(ru.pocketbyte.locolaser.kotlinmpp.KotlinAndroidResourcesConfig.TYPE)
        testPlatformConfigResource(ru.pocketbyte.locolaser.kotlinmpp.KotlinIosResourcesConfig.TYPE)
        testPlatformConfigResource(ru.pocketbyte.locolaser.kotlinmpp.KotlinJsResourcesConfig.TYPE)
        testPlatformConfigResource(ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsKeyValueResourcesConfig.TYPE)
    }

    @Test
    fun testKotlinAbsKeyValuePlatformConfig() {
        var json = prepareTestPlatformJson(ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsKeyValueResourcesConfig.TYPE)
        assertSame(
            NoFormattingType,
            (parser?.parse(json, true) as ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsKeyValueResourcesConfig).formattingType
        )
        json = prepareTestPlatformJson(ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsKeyValueResourcesConfig.TYPE).apply {
            this[KotlinImplementationResourcesConfigParser.FORMATTING_TYPE] = "java"
        }
        assertSame(
            JavaFormattingType,
            (parser?.parse(json, true) as ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsKeyValueResourcesConfig).formattingType
        )
    }

    private fun testPlatformConfigResource(platform: String) {
        val json = prepareTestPlatformJson(platform)
        val resources = (parser?.parse(json, true) as ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig).resources as AbsResources

        assertEquals("com.test_res", resources.name)
        assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    private fun prepareTestPlatformJson(platform: String): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = platform
        json[KotlinImplementationResourcesConfigParser.INTERFACE] = "com.test_interface"
        json[BaseMobileResourcesConfigParser.RESOURCE_NAME] = "com.test_res"
        json[BaseMobileResourcesConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}