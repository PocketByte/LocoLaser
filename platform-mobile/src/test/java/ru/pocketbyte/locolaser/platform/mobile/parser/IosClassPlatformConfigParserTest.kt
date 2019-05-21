package ru.pocketbyte.locolaser.platform.mobile.parser

import org.json.simple.JSONObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.PlatformConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.platform.mobile.IosSwiftPlatformConfig

import java.io.File
import java.io.IOException

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull

class IosClassPlatformConfigParserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private var parser: IosClassPlatformConfigParser? = null

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = IosClassPlatformConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testUnknownFromString() {
        parser!!.parse("invalid_platform", true)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJson() {
        val config = parser!!.parse(prepareTestPlatformJson(IosSwiftPlatformConfig.TYPE), true) as IosSwiftPlatformConfig?

        assertNotNull(config)

        assertEquals("test_res", config!!.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir!!.canonicalPath)
        assertEquals("test_table", config.tableName)
    }

    private fun prepareTestPlatformJson(platform: String): JSONObject {
        val json = JSONObject()
        json[PlatformConfigParser.PLATFORM_TYPE] = platform
        json[BaseMobilePlatformConfigParser.RESOURCE_NAME] = "test_res"
        json[BaseMobilePlatformConfigParser.RESOURCES_DIR] = "test_res_dir"
        json[IosClassPlatformConfigParser.TABLE_NAME] = "test_table"
        return json
    }
}
