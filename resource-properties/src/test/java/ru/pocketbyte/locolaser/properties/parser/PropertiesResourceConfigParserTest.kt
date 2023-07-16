package ru.pocketbyte.locolaser.properties.parser

import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.properties.PropertiesResourceConfig
import java.io.File
import java.io.IOException
import java.util.*

class PropertiesResourceConfigParserTest {

    @JvmField @Rule
    var tempFolder = TemporaryFolder()

    private lateinit var parser: PropertiesResourceConfigParser

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = PropertiesResourceConfigParser()

        val workDir = tempFolder.newFolder()
        System.setProperty("user.dir", workDir.canonicalPath)
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromString() {
        val config = parser.parse(PropertiesResourceConfig.TYPE, true)
            ?: throw NullPointerException()

        assertEquals(PropertiesResourceConfig::class.java, config.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testUnknownFromString() {
        parser.parse("invalid_platform", true)
    }

    @Test
    @Throws(InvalidConfigException::class, IOException::class)
    fun testFromJson() {
        val config = parser.parse(prepareTestPlatformJson(), true)
            ?: throw NullPointerException()

        assertEquals(PropertiesResourceConfig::class.java, config.javaClass)

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
        json[ResourcesConfigParser.RESOURCE_TYPE] = PropertiesResourceConfig.TYPE
        val config = parser.parse(json, true)
            ?: throw NullPointerException()

        assertEquals(PropertiesResourceConfig::class.java, config.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser.parse(ArrayList<String>(), true)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = PropertiesResourceConfig.TYPE
        json[PropertiesResourceConfigParser.RESOURCE_NAME] = "test_res"
        json[PropertiesResourceConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}
