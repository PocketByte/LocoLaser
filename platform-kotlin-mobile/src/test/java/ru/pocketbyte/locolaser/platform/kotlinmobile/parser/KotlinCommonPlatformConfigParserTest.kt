package ru.pocketbyte.locolaser.platform.kotlinmobile.parser

import org.json.simple.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.platform.kotlinmobile.KotlinCommonPlatformConfig
import ru.pocketbyte.locolaser.platform.mobile.parser.BaseMobilePlatformConfigParser
import ru.pocketbyte.locolaser.resource.AbsPlatformResources
import java.io.File
import java.io.IOException

class KotlinCommonPlatformConfigParserTest {

    @Rule
    @JvmField
    var tempFolder = TemporaryFolder()

    private var parser: KotlinCommonPlatformConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = KotlinCommonPlatformConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    fun testConfigResources() {
        val json = prepareTestPlatformJson()
        val resources = (parser?.parse(json, true) as KotlinCommonPlatformConfig).resources as AbsPlatformResources

        Assert.assertEquals("com.test_res", resources.name)
        Assert.assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[PlatformConfigParser.PLATFORM_TYPE] = KotlinCommonPlatformConfig.TYPE
        json[BaseMobilePlatformConfigParser.RESOURCE_NAME] = "com.test_res"
        json[BaseMobilePlatformConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}