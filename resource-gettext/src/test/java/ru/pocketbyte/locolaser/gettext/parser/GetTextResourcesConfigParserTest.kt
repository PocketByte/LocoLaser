package ru.pocketbyte.locolaser.gettext.parser

import org.json.simple.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException

import java.io.File
import java.io.IOException
import java.util.ArrayList

import ru.pocketbyte.locolaser.gettext.GetTextResourcesConfig
import ru.pocketbyte.locolaser.resource.AbsResources

class GetTextResourcesConfigParserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private lateinit var parser: GetTextResourcesConfigParser

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = GetTextResourcesConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromString() {
        val config = parser.parse(GetTextResourcesConfig.TYPE, true)
            ?: throw NullPointerException()

        assertEquals(GetTextResourcesConfig::class.java, config.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testUnknownFromString() {
        parser.parse("invalid_resource", true)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJson() {
        val config = parser.parse(prepareTestPlatformJson(), true)
            ?: throw NullPointerException()

        assertEquals(GetTextResourcesConfig::class.java, config.javaClass)

        assertNull(config.filter)
        assertEquals("test_res", config.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir.canonicalPath)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJsonNoType() {
        val json = prepareTestPlatformJson()
        json.remove(ResourcesConfigParser.RESOURCE_TYPE)

        parser.parse(json, true)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromJsonOnlyType() {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = GetTextResourcesConfig.TYPE
        val config = parser.parse(json, true)
            ?: throw NullPointerException()

        assertEquals(GetTextResourcesConfig::class.java, config.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser.parse(ArrayList<String>(), true)
    }

    @Test
    fun testConfigResources() {
        val json = prepareTestPlatformJson()
        val resources = (parser.parse(json, true) as GetTextResourcesConfig).resources as AbsResources

        assertEquals("test_res", resources.name)
        assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFilter() {
        val json = prepareTestPlatformJson().apply {
            this[GetTextResourcesConfigParser.FILTER] = "test_filter"
        }

        assertNotNull(parser.parse(json, true)?.filter)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = GetTextResourcesConfig.TYPE
        json[GetTextResourcesConfigParser.RESOURCE_NAME] = "test_res"
        json[GetTextResourcesConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}
