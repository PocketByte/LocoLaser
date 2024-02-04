package ru.pocketbyte.locolaser.ini.parser

import org.json.simple.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.parser.ResourcesConfigParser
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.ini.IniResourcesConfig
import java.io.File
import java.io.IOException
import java.util.*

class IniResourcesConfigParserTest {

    @JvmField @Rule
    var tempFolder = TemporaryFolder()

    private lateinit var parser: IniResourcesConfigParser
    private lateinit var workDir: File

    @Before
    @Throws(IOException::class)
    fun init() {
        parser = IniResourcesConfigParser()
        workDir = tempFolder.newFolder()
    }

    @Test
    @Throws(InvalidConfigException::class)
    fun testFromString() {
        val config = parser.parse(IniResourcesConfig.TYPE, workDir, true)
            ?: throw NullPointerException()

        assertEquals(IniResourcesConfig::class.java, config.javaClass)
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

        assertEquals(IniResourcesConfig::class.java, config.javaClass)

        assertEquals("test_res", config.resourceName)
        assertEquals(File("test_res_dir").canonicalPath,
                config.resourcesDir.canonicalPath)
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
        json[ResourcesConfigParser.RESOURCE_TYPE] = IniResourcesConfig.TYPE
        val config = parser.parse(json, workDir, true)
            ?: throw NullPointerException()

        assertEquals(IniResourcesConfig::class.java, config.javaClass)
    }

    @Test(expected = InvalidConfigException::class)
    @Throws(InvalidConfigException::class)
    fun testFromInvalidClass() {
        parser.parse(ArrayList<String>(), workDir, true)
    }

    private fun prepareTestPlatformJson(): JSONObject {
        val json = JSONObject()
        json[ResourcesConfigParser.RESOURCE_TYPE] = IniResourcesConfig.TYPE
        json[IniResourcesConfigParser.RESOURCE_NAME] = "test_res"
        json[IniResourcesConfigParser.RESOURCES_DIR] = "test_res_dir"
        return json
    }
}