package ru.pocketbyte.locolaser.platform.kotlinmpp.parser

import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.platform.kotlinmpp.*
import ru.pocketbyte.locolaser.platform.mobile.parser.BaseMobilePlatformConfigParser
import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import ru.pocketbyte.locolaser.resource.formatting.JavaFormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType
import java.io.File
import java.io.IOException

class KotlinImplementationPlatformConfigParserTest {

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    private var parser: KotlinImplementationPlatformConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = KotlinImplementationPlatformConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    fun testConfigResources() {
        testPlatformConfigResource(KotlinAndroidPlatformConfig.TYPE)
        testPlatformConfigResource(KotlinIosPlatformConfig.TYPE)
        testPlatformConfigResource(KotlinJsPlatformConfig.TYPE)
        testPlatformConfigResource(KotlinAbsKeyValuePlatformConfig.TYPE)
    }

    @Test
    fun testKotlinAbsKeyValuePlatformConfig() {
        var json = prepareTestPlatformJson(KotlinAbsKeyValuePlatformConfig.TYPE)
        assertSame(
            NoFormattingType,
            (parser?.parse(json, true) as KotlinAbsKeyValuePlatformConfig).formattingType
        )
        json = prepareTestPlatformJson(KotlinAbsKeyValuePlatformConfig.TYPE).apply {
            this[KotlinImplementationPlatformConfigParser.FORMATTING_TYPE] = "java"
        }
        assertSame(
            JavaFormattingType,
            (parser?.parse(json, true) as KotlinAbsKeyValuePlatformConfig).formattingType
        )
    }

    private fun testPlatformConfigResource(platform: String) {
        val json = prepareTestPlatformJson(platform)
        val resources = (parser?.parse(json, true) as KotlinBaseImplPlatformConfig).resources as AbsPlatformResources

        assertEquals("com.test_res", resources.name)
        assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    private fun prepareTestPlatformJson(platform: String): JSONObject {
        val json = JSONObject()
        json[PlatformConfigParser.PLATFORM_TYPE] = platform
        json[KotlinImplementationPlatformConfigParser.INTERFACE] = "com.test_interface"
        json[BaseMobilePlatformConfigParser.RESOURCE_NAME] = "com.test_res"
        json[BaseMobilePlatformConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}