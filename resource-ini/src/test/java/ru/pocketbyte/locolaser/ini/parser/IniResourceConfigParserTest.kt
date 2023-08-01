package ru.pocketbyte.locolaser.ini.parser

import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.ini.IniResourceConfig
import java.io.File
import java.io.IOException
import java.util.*

class IniResourceConfigParserTest {

    @JvmField @Rule
    var tempFolder = TemporaryFolder()

    private lateinit var parser: IniResourceConfigParser
    private lateinit var parent: Config

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = IniResourceConfigParser()
        parent = Config(tempFolder.newFolder())
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromString() {
        val config = parser.parse(IniResourceConfig.TYPE, true)
            ?: throw NullPointerException()

        assertEquals(IniResourceConfig::class.java, config.javaClass)
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
        parent.platform = config

        assertEquals(IniResourceConfig::class.java, config.javaClass)

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
        json[ResourcesConfigParser.RESOURCE_TYPE] = IniResourceConfig.TYPE
        val config = parser.parse(json, true)
            ?: throw NullPointerException()

        assertEquals(IniResourceConfig::class.java, config.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser.parse(ArrayList<String>(), true)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = IniResourceConfig.TYPE
        json[IniResourceConfigParser.RESOURCE_NAME] = "test_res"
        json[IniResourceConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}
