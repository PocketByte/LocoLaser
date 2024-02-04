package ru.pocketbyte.locolaser.json.parser

import org.json.simple.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException

import java.io.File
import java.io.IOException
import java.util.ArrayList

import ru.pocketbyte.locolaser.json.JsonResourcesConfig
import ru.pocketbyte.locolaser.resource.AbsResources

class JsonResourcesConfigParserTest {

    @Rule @JvmField
    var tempFolder = TemporaryFolder()

    private lateinit var parser: JsonResourcesConfigParser
    private lateinit var workDir: File

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = JsonResourcesConfigParser()
        workDir = tempFolder.newFolder()
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromString() {
        val config = parser.parse(JsonResourcesConfig.TYPE, workDir, true)
            ?: throw NullPointerException()

        assertEquals(JsonResourcesConfig::class.java, config.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testUnknownFromString() {
        parser.parse("invalid_platform", workDir, true)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJson() {
        val config = parser.parse(prepareTestPlatformJson(), workDir, true)
            ?: throw NullPointerException()

        assertEquals(JsonResourcesConfig::class.java, config.javaClass)

        assertNull(config.filter)
        assertEquals("test_res", config.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir.canonicalPath)
        assertEquals(5, (config as? JsonResourcesConfig)?.indent)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJsonNoType() {
        val json = prepareTestPlatformJson()
        json.remove(ResourcesConfigParser.RESOURCE_TYPE)

        parser.parse(json, workDir, true)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromJsonOnlyType() {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = JsonResourcesConfig.TYPE
        val config = parser.parse(json, workDir, true)
            ?: throw NullPointerException()

        assertEquals(JsonResourcesConfig::class.java, config.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser.parse(ArrayList<String>(), workDir, true)
    }

    @Test
    fun testConfigResources() {
        val json = prepareTestPlatformJson()
        val config = parser.parse(json, workDir, true)
        val resources = (config as JsonResourcesConfig).resources as AbsResources

        assertEquals("test_res", resources.name)
        assertEquals(File("test_res_dir").canonicalPath, resources.directory.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFilter() {
        val json = prepareTestPlatformJson().apply {
            this[JsonResourcesConfigParser.FILTER] = "test_filter"
        }

        assertNotNull(parser.parse(json, workDir, true)?.filter)
    }

    @Test
    fun testIndent() {
        val json = prepareTestPlatformJson()

        json[JsonResourcesConfigParser.INDENT] = 5L
        var config = parser.parse(json, workDir, true) as JsonResourcesConfig
        assertEquals(5, config.indent)

        json[JsonResourcesConfigParser.INDENT] = 1L
        config = parser.parse(json, workDir, true) as JsonResourcesConfig
        assertEquals(1, config.indent)

        // default -1
        json.remove(JsonResourcesConfigParser.INDENT)
        config = parser.parse(json, workDir, true) as JsonResourcesConfig
        assertEquals(-1, config.indent)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = JsonResourcesConfig.TYPE
        json[JsonResourcesConfigParser.RESOURCE_NAME] = "test_res"
        json[JsonResourcesConfigParser.RESOURCES_DIR] = "test_res_dir"
        json[JsonResourcesConfigParser.INDENT] = 5L
        return json
    }
}
