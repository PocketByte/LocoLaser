package ru.pocketbyte.locolaser.kotlinmpp.parser

import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsKeyValueResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinAndroidResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinIosResourcesConfig
import ru.pocketbyte.locolaser.kotlinmpp.KotlinJsResourcesConfig
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
    private lateinit var workDir: File

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = KotlinImplementationResourcesConfigParser()
        workDir = tempFolder.newFolder()
    }

    @Test
    fun testConfigResources() {
        testPlatformConfigResource(KotlinAndroidResourcesConfig.TYPE)
        testPlatformConfigResource(KotlinIosResourcesConfig.TYPE)
        testPlatformConfigResource(KotlinJsResourcesConfig.TYPE)
        testPlatformConfigResource(KotlinAbsKeyValueResourcesConfig.TYPE)
    }

    @Test
    fun testKotlinAbsKeyValuePlatformConfig() {
        var json = prepareTestPlatformJson(KotlinAbsKeyValueResourcesConfig.TYPE)
        assertSame(
            NoFormattingType,
            (parser?.parse(json, workDir, true) as KotlinAbsKeyValueResourcesConfig).formattingType
        )
        json = prepareTestPlatformJson(KotlinAbsKeyValueResourcesConfig.TYPE).apply {
            this[KotlinImplementationResourcesConfigParser.FORMATTING_TYPE] = "java"
        }
        assertSame(
            JavaFormattingType,
            (parser?.parse(json, workDir, true) as KotlinAbsKeyValueResourcesConfig).formattingType
        )
    }

    private fun testPlatformConfigResource(platform: String) {
        val json = prepareTestPlatformJson(platform)
        val config = parser?.parse(json, workDir, true)
        val resources = (config as KotlinBaseResourcesConfig).resources as AbsResources

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