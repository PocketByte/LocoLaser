package ru.pocketbyte.locolaser.mobile.parser

import org.json.simple.JSONObject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.mobile.IosSwiftResourcesConfig

import java.io.File
import java.io.IOException

import org.junit.Assert.assertEquals
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.mobile.IosObjectiveCResourcesConfig
import ru.pocketbyte.locolaser.resource.AbsResources

class IosClassResourcesConfigParserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private lateinit var parser: IosClassResourcesConfigParser
    private lateinit var workDir: File

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = IosClassResourcesConfigParser()
        workDir = tempFolder.newFolder()
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testUnknownFromString() {
        parser.parse("invalid_platform", workDir, true)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJson() {
        val config = parser.parse(prepareTestPlatformJson(IosSwiftResourcesConfig.TYPE), workDir, true) as? IosSwiftResourcesConfig
            ?: throw NullPointerException()

        assertEquals("test_res", config.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir.canonicalPath)
        assertEquals("test_table", config.tableName)
    }

    @Test
    fun testConfigResources() {
        testPlatformConfigResource(IosSwiftResourcesConfig.TYPE)
        testPlatformConfigResource(IosObjectiveCResourcesConfig.TYPE)
    }

    private fun testPlatformConfigResource(platform: String) {
        val json = prepareTestPlatformJson(platform)
        val config = parser.parse(json, workDir, true)
        val resources = (config as BaseResourcesConfig).resources as AbsResources

        assertEquals("test_res", resources.name)
        assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    private fun prepareTestPlatformJson(platform: String): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = platform
        json[BaseMobileResourcesConfigParser.RESOURCE_NAME] = "test_res"
        json[BaseMobileResourcesConfigParser.RESOURCES_DIR] = "test_res_dir"
        json[IosClassResourcesConfigParser.TABLE_NAME] = "test_table"
        return json
    }
}
